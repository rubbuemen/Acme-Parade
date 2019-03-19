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
import org.springframework.web.servlet.ModelAndView;

import services.ProclaimService;
import domain.Proclaim;

@Controller
@RequestMapping("/proclaim")
public class ProclaimController extends AbstractController {

	@Autowired
	ProclaimService	proclaimService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listProclaims() {
		ModelAndView result;
		Collection<Proclaim> proclaims;

		proclaims = this.proclaimService.findAll();

		result = new ModelAndView("proclaim/list");

		result.addObject("proclaims", proclaims);
		result.addObject("requestURI", "proclaim/list.do");

		return result;
	}

}
