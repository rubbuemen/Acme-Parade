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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import domain.Area;

@Controller
@RequestMapping("/area")
public class AreaController extends AbstractController {

	@Autowired
	AreaService	areaService;


	@RequestMapping(value = "/listGeneric", method = RequestMethod.GET)
	public ModelAndView listAreasByBrotherhood(@RequestParam final int chapterId) {
		ModelAndView result;
		Area area;

		area = this.areaService.findAreaByChapterId(chapterId);

		result = new ModelAndView("area/listGeneric");

		result.addObject("area", area);

		return result;
	}

}
