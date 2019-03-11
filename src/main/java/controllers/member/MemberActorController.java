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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MemberService;
import services.UserAccountService;
import controllers.AbstractController;
import domain.Actor;
import domain.Member;
import forms.MemberForm;

@Controller
@RequestMapping("/actor/member")
public class MemberActorController extends AbstractController {

	@Autowired
	ActorService		actorService;

	@Autowired
	MemberService		memberService;

	@Autowired
	UserAccountService	userAccountService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Member actor;

		actor = (Member) this.actorService.findActorLogged();
		final MemberForm actorForm = new MemberForm(actor);

		result = new ModelAndView("actor/edit");

		result.addObject("actionURL", "actor/member/edit.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@ModelAttribute("actorForm") MemberForm actorForm, final BindingResult binding) {
		ModelAndView result;

		try {
			actorForm = this.memberService.reconstruct(actorForm, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(actorForm.getActor());
			else {
				this.memberService.save(actorForm.getActor());
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

		result.addObject("actor", actor);
		result.addObject("message", message);

		return result;
	}

}
