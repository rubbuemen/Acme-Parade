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

import security.Authority;
import services.ActorService;
import services.ChapterService;
import domain.Chapter;

@Controller
@RequestMapping("/chapter")
public class ChapterController extends AbstractController {

	@Autowired
	ChapterService	chapterService;

	@Autowired
	ActorService	actorService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listChapters() {
		ModelAndView result;
		Collection<Chapter> chapters;

		chapters = this.chapterService.findAll();

		result = new ModelAndView("chapter/list");

		result.addObject("chapters", chapters);
		result.addObject("requestURI", "chapter/list.do");

		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView showChapter(@RequestParam final int chapterId) {
		ModelAndView result;
		Chapter actor;

		actor = this.chapterService.findOne(chapterId);

		result = new ModelAndView("actor/show");

		result.addObject("actor", actor);
		result.addObject("authority", Authority.CHAPTER);

		return result;
	}

}
