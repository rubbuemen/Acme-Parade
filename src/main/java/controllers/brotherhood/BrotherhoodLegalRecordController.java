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
import services.LegalRecordService;
import controllers.AbstractController;
import domain.LegalRecord;

@Controller
@RequestMapping("/legalRecord/brotherhood")
public class BrotherhoodLegalRecordController extends AbstractController {

	@Autowired
	LegalRecordService	legalRecordService;

	@Autowired
	BrotherhoodService	brotherhoodService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		LegalRecord legalRecord;

		legalRecord = this.legalRecordService.create();

		result = this.createEditModelAndView(legalRecord);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int legalRecordId) {
		ModelAndView result;
		LegalRecord legalRecord = null;

		try {
			legalRecord = this.legalRecordService.findLegalRecordBrotherhoodLogged(legalRecordId);
			result = this.createEditModelAndView(legalRecord);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(legalRecord, "hacking.logged.error");
			else
				result = this.createEditModelAndView(legalRecord, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(LegalRecord legalRecord, final BindingResult binding) {
		ModelAndView result;

		try {
			legalRecord = this.legalRecordService.reconstruct(legalRecord, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(legalRecord);
			else {
				this.legalRecordService.save(legalRecord);
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(legalRecord, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(legalRecord, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int legalRecordId) {
		ModelAndView result;

		final LegalRecord legalRecord = this.legalRecordService.findLegalRecordBrotherhoodLogged(legalRecordId);

		try {
			this.legalRecordService.delete(legalRecord);
			result = new ModelAndView("redirect:/history/brotherhood/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(legalRecord, "hacking.logged.error");
			else
				result = this.createEditModelAndView(legalRecord, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(legalRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord, final String message) {
		ModelAndView result;

		if (legalRecord == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (legalRecord.getId() == 0)
			result = new ModelAndView("legalRecord/create");
		else
			result = new ModelAndView("legalRecord/edit");

		result.addObject("legalRecord", legalRecord);
		result.addObject("actionURL", "legalRecord/brotherhood/edit.do");
		result.addObject("message", message);

		return result;
	}

}
