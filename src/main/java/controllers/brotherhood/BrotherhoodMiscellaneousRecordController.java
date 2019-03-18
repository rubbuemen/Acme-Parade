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
import services.MiscellaneousRecordService;
import controllers.AbstractController;
import domain.MiscellaneousRecord;

@Controller
@RequestMapping("/miscellaneousRecord/brotherhood")
public class BrotherhoodMiscellaneousRecordController extends AbstractController {

	@Autowired
	MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	BrotherhoodService			brotherhoodService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		MiscellaneousRecord miscellaneousRecord;

		miscellaneousRecord = this.miscellaneousRecordService.create();

		result = this.createEditModelAndView(miscellaneousRecord);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousRecordId) {
		ModelAndView result;
		MiscellaneousRecord miscellaneousRecord = null;

		try {
			miscellaneousRecord = this.miscellaneousRecordService.findMiscellaneousRecordBrotherhoodLogged(miscellaneousRecordId);
			result = this.createEditModelAndView(miscellaneousRecord);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(miscellaneousRecord, "hacking.logged.error");
			else
				result = this.createEditModelAndView(miscellaneousRecord, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(MiscellaneousRecord miscellaneousRecord, final BindingResult binding) {
		ModelAndView result;

		try {
			miscellaneousRecord = this.miscellaneousRecordService.reconstruct(miscellaneousRecord, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(miscellaneousRecord);
			else {
				this.miscellaneousRecordService.save(miscellaneousRecord);
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(miscellaneousRecord, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(miscellaneousRecord, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int miscellaneousRecordId) {
		ModelAndView result;

		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findMiscellaneousRecordBrotherhoodLogged(miscellaneousRecordId);

		try {
			this.miscellaneousRecordService.delete(miscellaneousRecord);
			result = new ModelAndView("redirect:/history/brotherhood/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(miscellaneousRecord, "hacking.logged.error");
			else
				result = this.createEditModelAndView(miscellaneousRecord, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(miscellaneousRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord, final String message) {
		ModelAndView result;

		if (miscellaneousRecord == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (miscellaneousRecord.getId() == 0)
			result = new ModelAndView("miscellaneousRecord/create");
		else
			result = new ModelAndView("miscellaneousRecord/edit");

		result.addObject("miscellaneousRecord", miscellaneousRecord);
		result.addObject("actionURL", "miscellaneousRecord/brotherhood/edit.do");
		result.addObject("message", message);

		return result;
	}

}
