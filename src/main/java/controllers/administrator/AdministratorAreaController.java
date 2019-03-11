/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.AreaService;
import controllers.AbstractController;
import domain.Area;

@Controller
@RequestMapping("/area/administrator")
public class AdministratorAreaController extends AbstractController {

	@Autowired
	AreaService				areaService;

	@Autowired
	AdministratorService	administratorService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Area> areas;

		areas = this.areaService.findAll();
		final Collection<Area> areasUsed = this.areaService.findAreasBrotherhoodUsed();

		result = new ModelAndView("area/list");

		result.addObject("areas", areas);
		result.addObject("areasUsed", areasUsed);
		result.addObject("requestURI", "area/administrator/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Area area;

		area = this.areaService.create();

		result = this.createEditModelAndView(area);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int areaId) {
		ModelAndView result;
		Area area = null;

		try {
			area = this.areaService.findOne(areaId);
			result = this.createEditModelAndView(area);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(area, "hacking.logged.error");
			else
				result = this.createEditModelAndView(area, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Area area, final BindingResult binding) {
		ModelAndView result;

		try {
			area = this.areaService.reconstruct(area, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(area);
			else {
				this.areaService.save(area);
				result = new ModelAndView("redirect:/area/administrator/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(area, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(area, "commit.error");

		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int areaId) {
		ModelAndView result;

		final Area area = this.areaService.findOne(areaId);

		try {
			this.areaService.delete(area);
			result = new ModelAndView("redirect:/area/administrator/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This area can not be deleted because it is in use"))
				result = this.createEditModelAndView(area, "area.error.occupied");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(area, "hacking.logged.error");
			else
				result = this.createEditModelAndView(area, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Area area) {
		ModelAndView result;
		result = this.createEditModelAndView(area, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Area area, final String message) {
		ModelAndView result;

		if (area == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (area.getId() == 0)
			result = new ModelAndView("area/create");
		else
			result = new ModelAndView("area/edit");

		result.addObject("area", area);
		result.addObject("actionURL", "area/administrator/edit.do");
		result.addObject("message", message);

		return result;
	}

}
