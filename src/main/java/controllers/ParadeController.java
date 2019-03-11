/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ParadeService;
import domain.Parade;

@Controller
@RequestMapping("/parade")
public class ParadeController extends AbstractController {

	@Autowired
	ParadeService	paradeService;

	@Autowired
	BrotherhoodService	brotherhoodService;


	@RequestMapping(value = "/listGeneric", method = RequestMethod.GET)
	public ModelAndView listParadesByBrotherhood(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		Collection<Parade> parades;

		parades = this.paradeService.findParadesFinalModeByBrotherhoodId(brotherhoodId);

		result = new ModelAndView("parade/listGeneric");

		result.addObject("parades", parades);
		result.addObject("requestURI", "parades/listGeneric.do");

		return result;
	}

}
