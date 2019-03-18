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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.InceptionRecordService;
import controllers.AbstractController;
import domain.InceptionRecord;

@Controller
@RequestMapping("/inceptionRecord/brotherhood")
public class BrotherhoodInceptionRecordController extends AbstractController {

	@Autowired
	InceptionRecordService	inceptionRecordService;

	@Autowired
	BrotherhoodService		brotherhoodService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int inceptionRecordId) {
		ModelAndView result;
		InceptionRecord inceptionRecord = null;

		try {
			inceptionRecord = this.inceptionRecordService.findInceptionRecordBrotherhoodLogged(inceptionRecordId);
			result = this.createEditModelAndView(inceptionRecord);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(inceptionRecord, "hacking.logged.error");
			else
				result = this.createEditModelAndView(inceptionRecord, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(InceptionRecord inceptionRecord, final BindingResult binding) {
		ModelAndView result;

		try {
			inceptionRecord = this.inceptionRecordService.reconstruct(inceptionRecord, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(inceptionRecord);
			else {
				this.inceptionRecordService.save(inceptionRecord);
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(inceptionRecord, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(inceptionRecord, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(inceptionRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord, final String message) {
		ModelAndView result;

		if (inceptionRecord == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (inceptionRecord.getId() == 0)
			result = new ModelAndView("inceptionRecord/create");
		else
			result = new ModelAndView("inceptionRecord/edit");

		result.addObject("inceptionRecord", inceptionRecord);
		result.addObject("actionURL", "inceptionRecord/brotherhood/edit.do");
		result.addObject("message", message);

		return result;
	}

}
