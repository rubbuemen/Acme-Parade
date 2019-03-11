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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.UserAccountService;
import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import forms.AdministratorForm;

@Controller
@RequestMapping("/actor/administrator")
public class AdministratorActorController extends AbstractController {

	@Autowired
	ActorService			actorService;

	@Autowired
	AdministratorService	administratorService;

	@Autowired
	UserAccountService		userAccountService;


	@RequestMapping(value = "/register-administrator", method = RequestMethod.GET)
	public ModelAndView registerAdministrator() {
		ModelAndView result;
		Administrator actor;

		actor = this.administratorService.create();

		final AdministratorForm actorForm = new AdministratorForm(actor);

		result = new ModelAndView("actor/register");

		result.addObject("actionURL", "actor/administrator/register-administrator.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/register-administrator", method = RequestMethod.POST, params = "save")
	public ModelAndView registerAdministrator(@ModelAttribute("actorForm") AdministratorForm actorForm, final BindingResult binding) {
		ModelAndView result;

		actorForm = this.administratorService.reconstruct(actorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm.getActor());
		else
			try {
				Assert.isTrue(actorForm.getActor().getUserAccount().getPassword().equals(actorForm.getPasswordCheck()), "Password does not match");
				Assert.isTrue(actorForm.getTermsConditions(), "The terms and conditions must be accepted");
				this.administratorService.save(actorForm.getActor());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("Password does not match"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.password.match");
				else if (oops.getMessage().equals("The terms and conditions must be accepted"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.error.duplicate.user");
				else if (oops.getMessage().equals("This entity does not exist"))
					result = this.createEditModelAndView(null, "hacking.notExist.error");
				else
					result = this.createEditModelAndView(actorForm.getActor(), "commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Administrator actor;

		actor = (Administrator) this.actorService.findActorLogged();
		final AdministratorForm actorForm = new AdministratorForm(actor);

		result = new ModelAndView("actor/edit");

		result.addObject("actionURL", "actor/administrator/edit.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@ModelAttribute("actorForm") AdministratorForm actorForm, final BindingResult binding) {
		ModelAndView result;

		try {
			actorForm = this.administratorService.reconstruct(actorForm, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(actorForm.getActor());
			else {
				this.administratorService.save(actorForm.getActor());
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
		else if (actor.getId() == 0)
			result = new ModelAndView("actor/register");
		else
			result = new ModelAndView("actor/edit");

		result.addObject("actor", actor);
		result.addObject("message", message);

		return result;
	}

}
