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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.BrotherhoodService;
import services.ChapterService;
import domain.Actor;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;

@Controller
@RequestMapping("/brotherhood")
public class BrotherhoodController extends AbstractController {

	@Autowired
	BrotherhoodService	brotherhoodService;

	@Autowired
	ActorService		actorService;

	@Autowired
	ChapterService		chapterService;


	@RequestMapping(value = "/listGeneric", method = RequestMethod.GET)
	public ModelAndView listBrotherhoods(@RequestParam(required = false) final Integer areaId) {
		ModelAndView result;
		Collection<Brotherhood> brotherhoods;
		Authentication authentication;

		Chapter chapter = null;

		if (areaId != null) {
			brotherhoods = this.brotherhoodService.findBrotherhoodsByAreaId(areaId);
			chapter = this.chapterService.findChapterByAreaId(areaId);
		} else
			brotherhoods = this.brotherhoodService.findAll();

		result = new ModelAndView("brotherhood/listGeneric");
		result.addObject("areaId", areaId);
		result.addObject("chapter", chapter);

		authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			final Actor actorLogged = this.actorService.findActorLogged();
			if (actorLogged instanceof Member) {
				final Collection<Brotherhood> brotherhoodsMemberLogged = this.brotherhoodService.findBrotherhoodsAcceptedOrPendingByMemberId(actorLogged.getId());
				result.addObject("brotherhoodsMemberLogged", brotherhoodsMemberLogged);
			}
		}

		result.addObject("brotherhoods", brotherhoods);
		result.addObject("requestURI", "brotherhood/listGeneric.do");

		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView showBrotherhood(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		Brotherhood actor;

		actor = this.brotherhoodService.findOne(brotherhoodId);

		result = new ModelAndView("actor/show");

		result.addObject("actor", actor);
		result.addObject("authority", Authority.BROTHERHOOD);

		return result;
	}

}
