/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.brotherhood;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.SegmentService;
import controllers.AbstractController;
import domain.Segment;

@Controller
@RequestMapping("/segment/brotherhood")
public class BrotherhoodSegmentController extends AbstractController {

	@Autowired
	SegmentService	segmentService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int paradeId) {
		ModelAndView result;
		Collection<Segment> segments;

		try {
			segments = this.segmentService.findSegmentsByParade(paradeId);
			result = new ModelAndView("segment/list");
			result.addObject("segments", segments);
			result.addObject("requestURI", "segment/brotherhood/list.do");
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(null, "hacking.logged.error");
			else
				result = this.createEditModelAndView(null, "commit.error");
		}

		return result;
	}

	//	@RequestMapping(value = "/create", method = RequestMethod.GET)
	//	public ModelAndView create() {
	//		ModelAndView result;
	//		Float floatE;
	//
	//		floatE = this.floatService.create();
	//
	//		result = this.createEditModelAndView(floatE);
	//
	//		return result;
	//	}
	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	//	public ModelAndView edit(@RequestParam final int floatId) {
	//		ModelAndView result;
	//		Float floatE = null;
	//
	//		try {
	//			floatE = this.floatService.findFloatBrotherhoodLogged(floatId);
	//			result = this.createEditModelAndView(floatE);
	//		} catch (final Throwable oops) {
	//			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
	//				result = this.createEditModelAndView(floatE, "hacking.logged.error");
	//			else
	//				result = this.createEditModelAndView(floatE, "commit.error");
	//		}
	//
	//		return result;
	//	}
	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	//	public ModelAndView createOrEdit(@ModelAttribute("floatE") Float floatE, final BindingResult binding) {
	//		ModelAndView result;
	//
	//		try {
	//			floatE = this.floatService.reconstruct(floatE, binding);
	//			if (binding.hasErrors())
	//				result = this.createEditModelAndView(floatE);
	//			else {
	//				this.floatService.save(floatE);
	//				result = new ModelAndView("redirect:/float/brotherhood/list.do");
	//			}
	//		} catch (final Throwable oops) {
	//			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
	//				result = this.createEditModelAndView(floatE, "hacking.logged.error");
	//			else if (oops.getMessage().equals("This entity does not exist"))
	//				result = this.createEditModelAndView(null, "hacking.notExist.error");
	//			else
	//				result = this.createEditModelAndView(floatE, "commit.error");
	//		}
	//
	//		return result;
	//	}
	//
	//	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	//	public ModelAndView delete(@RequestParam final int floatId) {
	//		ModelAndView result;
	//
	//		final Float floatE = this.floatService.findFloatBrotherhoodLogged(floatId);
	//
	//		try {
	//			this.floatService.delete(floatE);
	//			result = new ModelAndView("redirect:/float/brotherhood/list.do");
	//
	//		} catch (final Throwable oops) {
	//			if (oops.getMessage().equals("You can not eliminate this float because the parade would run out of floats"))
	//				result = this.createEditModelAndView(floatE, "float.error.occupied");
	//			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
	//				result = this.createEditModelAndView(floatE, "hacking.logged.error");
	//			else
	//				result = this.createEditModelAndView(floatE, "commit.error");
	//		}
	//
	//		return result;
	//	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Segment segment) {
		ModelAndView result;
		result = this.createEditModelAndView(segment, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Segment segment, final String message) {
		ModelAndView result;

		if (segment == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (segment.getId() == 0)
			result = new ModelAndView("float/create");
		else
			result = new ModelAndView("float/edit");

		result.addObject("segment", segment);
		result.addObject("actionURL", "segment/brotherhood/edit.do");
		result.addObject("message", message);

		return result;
	}

}
