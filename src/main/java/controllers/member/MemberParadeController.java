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
import services.ParadeService;
import controllers.AbstractController;
import domain.Parade;

@Controller
@RequestMapping("/parade/member")
public class MemberParadeController extends AbstractController {

	@Autowired
	ParadeService		paradeService;

	@Autowired
	BrotherhoodService	brotherhoodService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listByBrotherhood(@RequestParam final int brotherhoodId) {
		ModelAndView result;

		Collection<Parade> parades = null;

		try {
			parades = this.paradeService.findParadesAcceptedByBrotherhoodId(brotherhoodId);
			result = this.createEditModelAndView(parades);

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(parades, "hacking.logged.error");
			else
				result = this.createEditModelAndView(parades, "commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Collection<Parade> parades) {
		ModelAndView result;
		result = this.createEditModelAndView(parades, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Parade> parades, final String message) {
		ModelAndView result;

		if (parades == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("parade/list");

		result.addObject("parades", parades);
		result.addObject("actionURL", "parade/member/list.do");
		result.addObject("message", message);

		return result;
	}

}
