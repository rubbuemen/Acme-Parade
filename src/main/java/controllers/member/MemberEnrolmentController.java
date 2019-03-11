/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.member;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.EnrolmentService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Enrolment;

@Controller
@RequestMapping("/enrolment/member")
public class MemberEnrolmentController extends AbstractController {

	@Autowired
	EnrolmentService	enrolmentService;

	@Autowired
	BrotherhoodService	brotherhoodService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listByBrotherhood(@RequestParam final int brotherhoodId) {
		ModelAndView result;

		Collection<Enrolment> enrolments = null;

		try {
			enrolments = this.enrolmentService.findEnrolmentsByBrotherhoodId(brotherhoodId);
			result = this.createEditModelAndView(enrolments);

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(enrolments, "hacking.logged.error");
			else
				result = this.createEditModelAndView(enrolments, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/enroll", method = RequestMethod.GET)
	public ModelAndView enroll(@RequestParam final int brotherhoodId) {
		ModelAndView result;

		Enrolment enrolment = this.enrolmentService.create();

		final Brotherhood brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		enrolment = this.enrolmentService.save(enrolment, brotherhood);

		brotherhood.getEnrolments().add(enrolment);
		this.brotherhoodService.saveAuxiliar(brotherhood);

		result = new ModelAndView("redirect:/brotherhood/listGeneric.do");

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Collection<Enrolment> enrolments) {
		ModelAndView result;
		result = this.createEditModelAndView(enrolments, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Enrolment> enrolments, final String message) {
		ModelAndView result;

		if (enrolments == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("enrolment/list");

		final String language = LocaleContextHolder.getLocale().getLanguage();
		result.addObject("language", language);
		result.addObject("enrolments", enrolments);
		result.addObject("actionURL", "enrolment/member/list.do");
		result.addObject("message", message);

		return result;
	}

}
