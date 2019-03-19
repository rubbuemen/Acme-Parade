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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ChapterService;
import services.ProclaimService;
import controllers.AbstractController;
import domain.Proclaim;

@Controller
@RequestMapping("/proclaim/chapter")
public class ChapterProclaimController extends AbstractController {

	@Autowired
	ProclaimService	proclaimService;

	@Autowired
	ChapterService	chapterService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Proclaim proclaimE;

		proclaimE = this.proclaimService.create();

		result = this.createEditModelAndView(proclaimE);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView create(Proclaim proclaim, final BindingResult binding) {
		ModelAndView result;

		try {
			proclaim = this.proclaimService.reconstruct(proclaim, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(proclaim);
			else {
				this.proclaimService.save(proclaim);
				result = new ModelAndView("redirect:/proclaim/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(proclaim, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(proclaim, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Proclaim proclaim) {
		ModelAndView result;
		result = this.createEditModelAndView(proclaim, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Proclaim proclaim, final String message) {
		ModelAndView result;

		if (proclaim == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("proclaim/create");

		result.addObject("proclaim", proclaim);
		result.addObject("actionURL", "proclaim/chapter/edit.do");
		result.addObject("message", message);

		return result;
	}

}
