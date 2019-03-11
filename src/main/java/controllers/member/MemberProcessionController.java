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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ProcessionService;
import controllers.AbstractController;
import domain.Procession;

@Controller
@RequestMapping("/procession/member")
public class MemberProcessionController extends AbstractController {

	@Autowired
	ProcessionService	processionService;

	@Autowired
	BrotherhoodService	brotherhoodService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listByBrotherhood(@RequestParam final int brotherhoodId) {
		ModelAndView result;

		Collection<Procession> processions = null;

		try {
			processions = this.processionService.findProcessionsFinalModeByBrotherhoodId(brotherhoodId);
			result = this.createEditModelAndView(processions);

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(processions, "hacking.logged.error");
			else
				result = this.createEditModelAndView(processions, "commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Collection<Procession> processions) {
		ModelAndView result;
		result = this.createEditModelAndView(processions, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Procession> processions, final String message) {
		ModelAndView result;

		if (processions == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("procession/list");

		result.addObject("processions", processions);
		result.addObject("actionURL", "procession/member/list.do");
		result.addObject("message", message);

		return result;
	}

}
