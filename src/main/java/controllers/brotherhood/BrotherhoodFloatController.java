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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.FloatService;
import controllers.AbstractController;
import domain.Float;

@Controller
@RequestMapping("/float/brotherhood")
public class BrotherhoodFloatController extends AbstractController {

	@Autowired
	FloatService		floatService;

	@Autowired
	BrotherhoodService	brotherhoodService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Float> floats;

		floats = this.floatService.findFloatsByBrotherhoodLogged();

		result = new ModelAndView("float/list");

		result.addObject("floats", floats);
		result.addObject("requestURI", "float/brotherhood/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Float floatE;

		floatE = this.floatService.create();

		result = this.createEditModelAndView(floatE);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int floatId) {
		ModelAndView result;
		Float floatE = null;

		try {
			floatE = this.floatService.findFloatBrotherhoodLogged(floatId);
			result = this.createEditModelAndView(floatE);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(floatE, "hacking.logged.error");
			else
				result = this.createEditModelAndView(floatE, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(@ModelAttribute("floatE") Float floatE, final BindingResult binding) {
		ModelAndView result;

		try {
			floatE = this.floatService.reconstruct(floatE, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(floatE);
			else {
				this.floatService.save(floatE);
				result = new ModelAndView("redirect:/float/brotherhood/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(floatE, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(floatE, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int floatId) {
		ModelAndView result;

		final Float floatE = this.floatService.findFloatBrotherhoodLogged(floatId);

		try {
			this.floatService.delete(floatE);
			result = new ModelAndView("redirect:/float/brotherhood/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can not eliminate this float because the parade would run out of floats"))
				result = this.createEditModelAndView(floatE, "float.error.occupied");
			else if (oops.getMessage().equals("You can only save parades that are not in final mode"))
				result = this.createEditModelAndView(floatE, "float.error.paradeFinalMode");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(floatE, "hacking.logged.error");
			else
				result = this.createEditModelAndView(floatE, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Float floatE) {
		ModelAndView result;
		result = this.createEditModelAndView(floatE, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Float floatE, final String message) {
		ModelAndView result;

		if (floatE == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (floatE.getId() == 0)
			result = new ModelAndView("float/create");
		else
			result = new ModelAndView("float/edit");

		result.addObject("floatE", floatE);
		result.addObject("actionURL", "float/brotherhood/edit.do");
		result.addObject("message", message);

		return result;
	}

}
