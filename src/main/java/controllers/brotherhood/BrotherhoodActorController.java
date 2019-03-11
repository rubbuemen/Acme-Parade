/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.brotherhood;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.BrotherhoodService;
import services.UserAccountService;
import controllers.AbstractController;
import domain.Actor;
import domain.Brotherhood;
import forms.BrotherhoodForm;

@Controller
@RequestMapping("/actor/brotherhood")
public class BrotherhoodActorController extends AbstractController {

	@Autowired
	ActorService		actorService;

	@Autowired
	BrotherhoodService	brotherhoodService;

	@Autowired
	UserAccountService	userAccountService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Brotherhood actor;

		actor = (Brotherhood) this.actorService.findActorLogged();
		final BrotherhoodForm actorForm = new BrotherhoodForm(actor);

		result = new ModelAndView("actor/edit");

		result.addObject("authority", Authority.BROTHERHOOD);
		result.addObject("actionURL", "actor/brotherhood/edit.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@ModelAttribute("actorForm") BrotherhoodForm actorForm, final BindingResult binding) {
		ModelAndView result;

		try {
			actorForm = this.brotherhoodService.reconstruct(actorForm, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(actorForm.getActor());
			else {
				this.brotherhoodService.save(actorForm.getActor());
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(actorForm.getActor(), "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(actorForm.getActor(), "commit.error");

		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Actor actor) {
		ModelAndView result;
		result = this.createEditModelAndView(actor, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor, final String message) {
		ModelAndView result;
		if (actor == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("actor/edit");

		if (actor instanceof Brotherhood)
			result.addObject("authority", Authority.BROTHERHOOD);
		result.addObject("actor", actor);
		result.addObject("message", message);

		return result;
	}

}
