/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.chapter;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.AreaService;
import services.ChapterService;
import controllers.AbstractController;
import domain.Area;

@Controller
@RequestMapping("/area/chapter")
public class ChapterAreaController extends AbstractController {

	@Autowired
	AreaService		areaService;

	@Autowired
	ChapterService	chapterService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Area> areas;

		areas = this.areaService.findAreasToSelfAssign();

		result = new ModelAndView("area/list");

		result.addObject("authority", Authority.CHAPTER);
		result.addObject("areas", areas);
		result.addObject("requestURI", "area/chapter/list.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int areaId) {
		ModelAndView result;

		this.chapterService.selfAssign(areaId);

		result = new ModelAndView("redirect:/welcome/index.do");

		return result;
	}

}
