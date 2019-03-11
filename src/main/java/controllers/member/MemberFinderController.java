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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.FinderService;
import services.MemberService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Area;
import domain.Finder;

@Controller
@RequestMapping("/finder/member")
public class MemberFinderController extends AbstractController {

	@Autowired
	ParadeService	paradeService;

	@Autowired
	MemberService		memberService;

	@Autowired
	FinderService		finderService;

	@Autowired
	AreaService			areaService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView finderSearch() {
		ModelAndView result;

		Finder finder = null;

		try {
			finder = this.finderService.findFinderMemberLogged();
			final boolean clearCache = this.finderService.cleanCache(finder);
			if (clearCache)
				this.finderService.updateCriteria(finder.getKeyWord(), finder.getMinDate(), finder.getMaxDate(), finder.getArea());
			result = this.createEditModelAndView(finder);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(finder, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/clear", method = RequestMethod.GET)
	public ModelAndView clear() {
		ModelAndView result;

		Finder finder = null;

		try {
			finder = this.finderService.findFinderMemberLogged();
			finder = this.finderService.cleanFinder(finder);
			result = this.createEditModelAndView(finder);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(finder, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(Finder finder, final BindingResult binding) {
		ModelAndView result;

		try {
			finder = this.finderService.reconstruct(finder, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(finder);
			else {
				this.finderService.updateCriteria(finder.getKeyWord(), finder.getMinDate(), finder.getMaxDate(), finder.getArea());
				result = new ModelAndView("redirect:/finder/member/edit.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(finder, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(finder, "commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Finder finder) {
		ModelAndView result;
		result = this.createEditModelAndView(finder, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Finder finder, final String message) {
		ModelAndView result;

		if (finder == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			result = new ModelAndView("finder/edit");
			result.addObject("parades", finder.getParades());
		}

		final Collection<Area> areas = this.areaService.findAll();

		result.addObject("finder", finder);
		result.addObject("areas", areas);
		result.addObject("actionURL", "finder/member/edit.do");
		result.addObject("message", message);

		return result;
	}

}
