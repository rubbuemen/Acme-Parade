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
import services.PeriodRecordService;
import controllers.AbstractController;
import domain.PeriodRecord;

@Controller
@RequestMapping("/periodRecord/brotherhood")
public class BrotherhoodPeriodRecordController extends AbstractController {

	@Autowired
	PeriodRecordService	periodRecordService;

	@Autowired
	BrotherhoodService	brotherhoodService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		PeriodRecord periodRecord;

		periodRecord = this.periodRecordService.create();

		result = this.createEditModelAndView(periodRecord);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int periodRecordId) {
		ModelAndView result;
		PeriodRecord periodRecord = null;

		try {
			periodRecord = this.periodRecordService.findPeriodRecordBrotherhoodLogged(periodRecordId);
			result = this.createEditModelAndView(periodRecord);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(periodRecord, "hacking.logged.error");
			else
				result = this.createEditModelAndView(periodRecord, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(PeriodRecord periodRecord, final BindingResult binding) {
		ModelAndView result;

		try {
			periodRecord = this.periodRecordService.reconstruct(periodRecord, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(periodRecord);
			else {
				this.periodRecordService.save(periodRecord);
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("Start year must be past"))
				result = this.createEditModelAndView(periodRecord, "periodRecord.error.startYearPast");
			else if (oops.getMessage().equals("End year must be past"))
				result = this.createEditModelAndView(periodRecord, "periodRecord.error.endYearPast");
			else if (oops.getMessage().equals("Start year must be before end year"))
				result = this.createEditModelAndView(periodRecord, "periodRecord.error.startYearLessEndYear");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(periodRecord, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(periodRecord, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int periodRecordId) {
		ModelAndView result;

		final PeriodRecord periodRecord = this.periodRecordService.findPeriodRecordBrotherhoodLogged(periodRecordId);

		try {
			this.periodRecordService.delete(periodRecord);
			result = new ModelAndView("redirect:/history/brotherhood/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(periodRecord, "hacking.logged.error");
			else
				result = this.createEditModelAndView(periodRecord, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(periodRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord, final String message) {
		ModelAndView result;

		if (periodRecord == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (periodRecord.getId() == 0)
			result = new ModelAndView("periodRecord/create");
		else
			result = new ModelAndView("periodRecord/edit");

		result.addObject("periodRecord", periodRecord);
		result.addObject("actionURL", "periodRecord/brotherhood/edit.do");
		result.addObject("message", message);

		return result;
	}

}
