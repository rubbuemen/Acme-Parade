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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.EnrolmentService;
import services.PositionBrotherhoodService;
import controllers.AbstractController;
import domain.Enrolment;
import domain.PositionBrotherhood;

@Controller
@RequestMapping("/enrolment/brotherhood")
public class BrotherhoodEnrolmentController extends AbstractController {

	@Autowired
	EnrolmentService			enrolmentService;

	@Autowired
	BrotherhoodService			brotherhoodService;

	@Autowired
	PositionBrotherhoodService	positionBrotherhoodService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Enrolment> enrolments;

		enrolments = this.enrolmentService.findEnrolmentsPendingByBrotherhoodLogged();

		result = new ModelAndView("enrolment/list");

		result.addObject("enrolments", enrolments);
		result.addObject("requestURI", "enrolment/brotherhood/list.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView enroll(@RequestParam final int enrolmentId) {
		ModelAndView result;

		Enrolment enrolment = null;

		try {
			enrolment = this.enrolmentService.findEnrolmentPenddingBrotherhoodLogged(enrolmentId);
			result = this.createEditModelAndView(enrolment);

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(enrolment, "hacking.logged.error");
			else
				result = this.createEditModelAndView(enrolment, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Enrolment enrolment, final BindingResult binding) {
		ModelAndView result;

		try {
			enrolment = this.enrolmentService.reconstruct(enrolment, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(enrolment);
			else {
				this.enrolmentService.save(enrolment);
				result = new ModelAndView("redirect:/enrolment/brotherhood/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can not enroll members until you selected an area"))
				result = this.createEditModelAndView(enrolment, "enrolment.error.area");
			else if (oops.getMessage().equals("You must select a position"))
				result = this.createEditModelAndView(enrolment, "enrolment.error.selectPosition");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(enrolment, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(enrolment, "commit.error");

		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Enrolment enrolment) {
		ModelAndView result;
		result = this.createEditModelAndView(enrolment, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Enrolment enrolment, final String message) {
		ModelAndView result;

		if (enrolment == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (enrolment.getId() == 0)
			result = new ModelAndView("enrolment/create");
		else {
			result = new ModelAndView("enrolment/edit");
			final String language = LocaleContextHolder.getLocale().getLanguage();
			final Collection<PositionBrotherhood> positionsBrotherhood = this.positionBrotherhoodService.findAll();
			result.addObject("language", language);
			result.addObject("positionsBrotherhood", positionsBrotherhood);
		}

		result.addObject("enrolment", enrolment);
		result.addObject("actionURL", "enrolment/brotherhood/edit.do");
		result.addObject("message", message);

		return result;
	}

}
