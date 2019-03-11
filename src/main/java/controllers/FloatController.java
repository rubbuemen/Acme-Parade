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
import services.FloatService;
import domain.Float;

@Controller
@RequestMapping("/float")
public class FloatController extends AbstractController {

	@Autowired
	FloatService		floatService;

	@Autowired
	BrotherhoodService	brotherhoodService;


	@RequestMapping(value = "/listGeneric", method = RequestMethod.GET)
	public ModelAndView listFloatsByBrotherhood(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		Collection<Float> floats;

		floats = this.floatService.findFloatsByBrotherhoodId(brotherhoodId);

		result = new ModelAndView("float/listGeneric");

		result.addObject("floats", floats);
		result.addObject("requestURI", "float/listGeneric.do");

		return result;
	}

}
