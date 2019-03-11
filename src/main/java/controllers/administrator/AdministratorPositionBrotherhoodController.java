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
import services.PositionBrotherhoodService;
import controllers.AbstractController;
import domain.PositionBrotherhood;

@Controller
@RequestMapping("/positionBrotherhood/administrator")
public class AdministratorPositionBrotherhoodController extends AbstractController {

	@Autowired
	PositionBrotherhoodService	positionBrotherhoodService;

	@Autowired
	AdministratorService		administratorService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<PositionBrotherhood> positionBrotherhoods;

		positionBrotherhoods = this.positionBrotherhoodService.findAll();
		final Collection<PositionBrotherhood> positionBrotherhoodsUsed = this.positionBrotherhoodService.findPositionsBrotherhoodUsed();

		result = new ModelAndView("positionBrotherhood/list");

		result.addObject("positionBrotherhoods", positionBrotherhoods);
		result.addObject("positionBrotherhoodsUsed", positionBrotherhoodsUsed);
		result.addObject("requestURI", "positionBrotherhood/administrator/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		PositionBrotherhood positionBrotherhood;

		positionBrotherhood = this.positionBrotherhoodService.create();

		result = this.createEditModelAndView(positionBrotherhood);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int positionBrotherhoodId) {
		ModelAndView result;
		PositionBrotherhood positionBrotherhood = null;

		try {
			positionBrotherhood = this.positionBrotherhoodService.findOne(positionBrotherhoodId);
			result = this.createEditModelAndView(positionBrotherhood);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(positionBrotherhood, "hacking.logged.error");
			else
				result = this.createEditModelAndView(positionBrotherhood, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(PositionBrotherhood positionBrotherhood, final BindingResult binding) {
		ModelAndView result;

		try {
			positionBrotherhood = this.positionBrotherhoodService.reconstruct(positionBrotherhood, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(positionBrotherhood);
			else {
				this.positionBrotherhoodService.save(positionBrotherhood);
				result = new ModelAndView("redirect:/positionBrotherhood/administrator/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
				result = this.createEditModelAndView(positionBrotherhood, "positionBrotherhood.error.duplicate.name");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(positionBrotherhood, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(positionBrotherhood, "commit.error");

		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int positionBrotherhoodId) {
		ModelAndView result;

		final PositionBrotherhood positionBrotherhood = this.positionBrotherhoodService.findOne(positionBrotherhoodId);

		try {
			this.positionBrotherhoodService.delete(positionBrotherhood);
			result = new ModelAndView("redirect:/positionBrotherhood/administrator/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This position can not be deleted because it is in use"))
				result = this.createEditModelAndView(positionBrotherhood, "positionBrotherhood.error.occupied");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(positionBrotherhood, "hacking.logged.error");
			else
				result = this.createEditModelAndView(positionBrotherhood, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final PositionBrotherhood positionBrotherhood) {
		ModelAndView result;
		result = this.createEditModelAndView(positionBrotherhood, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final PositionBrotherhood positionBrotherhood, final String message) {
		ModelAndView result;

		if (positionBrotherhood == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (positionBrotherhood.getId() == 0)
			result = new ModelAndView("positionBrotherhood/create");
		else
			result = new ModelAndView("positionBrotherhood/edit");

		result.addObject("positionBrotherhood", positionBrotherhood);
		result.addObject("actionURL", "positionBrotherhood/administrator/edit.do");
		result.addObject("message", message);

		return result;
	}

}
