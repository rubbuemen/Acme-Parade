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
import services.ProcessionService;
import domain.Procession;

@Controller
@RequestMapping("/procession")
public class ProcessionController extends AbstractController {

	@Autowired
	ProcessionService	processionService;

	@Autowired
	BrotherhoodService	brotherhoodService;


	@RequestMapping(value = "/listGeneric", method = RequestMethod.GET)
	public ModelAndView listProcessionsByBrotherhood(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		Collection<Procession> processions;

		processions = this.processionService.findProcessionsFinalModeByBrotherhoodId(brotherhoodId);

		result = new ModelAndView("procession/listGeneric");

		result.addObject("processions", processions);
		result.addObject("requestURI", "processions/listGeneric.do");

		return result;
	}

}
