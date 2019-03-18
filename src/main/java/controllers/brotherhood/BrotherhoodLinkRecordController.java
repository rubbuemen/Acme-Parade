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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.LinkRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.LinkRecord;

@Controller
@RequestMapping("/linkRecord/brotherhood")
public class BrotherhoodLinkRecordController extends AbstractController {

	@Autowired
	LinkRecordService	linkRecordService;

	@Autowired
	BrotherhoodService	brotherhoodService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		LinkRecord linkRecord;

		linkRecord = this.linkRecordService.create();

		result = this.createEditModelAndView(linkRecord);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int linkRecordId) {
		ModelAndView result;
		LinkRecord linkRecord = null;

		try {
			linkRecord = this.linkRecordService.findLinkRecordBrotherhoodLogged(linkRecordId);
			result = this.createEditModelAndView(linkRecord);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(linkRecord, "hacking.logged.error");
			else
				result = this.createEditModelAndView(linkRecord, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(LinkRecord linkRecord, final BindingResult binding) {
		ModelAndView result;

		try {
			linkRecord = this.linkRecordService.reconstruct(linkRecord, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(linkRecord);
			else {
				this.linkRecordService.save(linkRecord);
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(linkRecord, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(linkRecord, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int linkRecordId) {
		ModelAndView result;

		final LinkRecord linkRecord = this.linkRecordService.findLinkRecordBrotherhoodLogged(linkRecordId);

		try {
			this.linkRecordService.delete(linkRecord);
			result = new ModelAndView("redirect:/history/brotherhood/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(linkRecord, "hacking.logged.error");
			else
				result = this.createEditModelAndView(linkRecord, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final LinkRecord linkRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(linkRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final LinkRecord linkRecord, final String message) {
		ModelAndView result;

		if (linkRecord == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (linkRecord.getId() == 0)
			result = new ModelAndView("linkRecord/create");
		else
			result = new ModelAndView("linkRecord/edit");

		if (linkRecord != null) {
			final Collection<Brotherhood> brotherhoodsToLink = this.brotherhoodService.findBrotherhoodsToLink();
			result.addObject("brotherhoodsToLink", brotherhoodsToLink);
		}
		result.addObject("linkRecord", linkRecord);
		result.addObject("actionURL", "linkRecord/brotherhood/edit.do");
		result.addObject("message", message);

		return result;
	}

}
