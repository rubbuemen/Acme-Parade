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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.AdministratorService;
import services.BrotherhoodService;
import services.ChapterService;
import services.MemberService;
import services.SponsorService;
import services.UserAccountService;
import domain.Actor;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Sponsor;
import forms.BrotherhoodForm;
import forms.ChapterForm;
import forms.MemberForm;
import forms.SponsorForm;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	@Autowired
	ActorService			actorService;

	@Autowired
	BrotherhoodService		brotherhoodService;

	@Autowired
	MemberService			memberService;

	@Autowired
	AdministratorService	administratorService;

	@Autowired
	UserAccountService		userAccountService;

	@Autowired
	ChapterService			chapterService;

	@Autowired
	SponsorService			sponsorService;


	@RequestMapping(value = "/register-brotherhood", method = RequestMethod.GET)
	public ModelAndView registerBrotherhood() {
		ModelAndView result;
		Brotherhood actor;

		actor = this.brotherhoodService.create();

		final BrotherhoodForm actorForm = new BrotherhoodForm(actor);

		result = new ModelAndView("actor/register");

		result.addObject("authority", Authority.BROTHERHOOD);
		result.addObject("actionURL", "actor/register-brotherhood.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/register-member", method = RequestMethod.GET)
	public ModelAndView registerMember() {
		ModelAndView result;
		Member actor;

		actor = this.memberService.create();

		final MemberForm actorForm = new MemberForm(actor);

		result = new ModelAndView("actor/register");

		result.addObject("actionURL", "actor/register-member.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/register-chapter", method = RequestMethod.GET)
	public ModelAndView registerChapter() {
		ModelAndView result;
		Chapter actor;

		actor = this.chapterService.create();

		final ChapterForm actorForm = new ChapterForm(actor);

		result = new ModelAndView("actor/register");

		result.addObject("authority", Authority.CHAPTER);
		result.addObject("actionURL", "actor/register-chapter.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/register-sponsor", method = RequestMethod.GET)
	public ModelAndView registerSponsor() {
		ModelAndView result;
		Sponsor actor;

		actor = this.sponsorService.create();

		final SponsorForm actorForm = new SponsorForm(actor);

		result = new ModelAndView("actor/register");

		result.addObject("actionURL", "actor/register-sponsor.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/register-brotherhood", method = RequestMethod.POST, params = "save")
	public ModelAndView registerBrotherhood(@ModelAttribute("actorForm") BrotherhoodForm actorForm, final BindingResult binding) {
		ModelAndView result;

		actorForm = this.brotherhoodService.reconstruct(actorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm.getActor());
		else
			try {
				Assert.isTrue(actorForm.getActor().getUserAccount().getPassword().equals(actorForm.getPasswordCheck()), "Password does not match");
				Assert.isTrue(actorForm.getTermsConditions(), "The terms and conditions must be accepted");
				this.brotherhoodService.save(actorForm.getActor());
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

	@RequestMapping(value = "/register-member", method = RequestMethod.POST, params = "save")
	public ModelAndView registerMember(@ModelAttribute("actorForm") MemberForm actorForm, final BindingResult binding) {
		ModelAndView result;

		actorForm = this.memberService.reconstruct(actorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm.getActor());
		else
			try {
				Assert.isTrue(actorForm.getActor().getUserAccount().getPassword().equals(actorForm.getPasswordCheck()), "Password does not match");
				Assert.isTrue(actorForm.getTermsConditions(), "The terms and conditions must be accepted");
				this.memberService.save(actorForm.getActor());
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

	@RequestMapping(value = "/register-chapter", method = RequestMethod.POST, params = "save")
	public ModelAndView registerChapter(@ModelAttribute("actorForm") ChapterForm actorForm, final BindingResult binding) {
		ModelAndView result;

		actorForm = this.chapterService.reconstruct(actorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm.getActor());
		else
			try {
				Assert.isTrue(actorForm.getActor().getUserAccount().getPassword().equals(actorForm.getPasswordCheck()), "Password does not match");
				Assert.isTrue(actorForm.getTermsConditions(), "The terms and conditions must be accepted");
				this.chapterService.save(actorForm.getActor());
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

	@RequestMapping(value = "/register-sponsor", method = RequestMethod.POST, params = "save")
	public ModelAndView registerSponsor(@ModelAttribute("actorForm") SponsorForm actorForm, final BindingResult binding) {
		ModelAndView result;

		actorForm = this.sponsorService.reconstruct(actorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm.getActor());
		else
			try {
				Assert.isTrue(actorForm.getActor().getUserAccount().getPassword().equals(actorForm.getPasswordCheck()), "Password does not match");
				Assert.isTrue(actorForm.getTermsConditions(), "The terms and conditions must be accepted");
				this.sponsorService.save(actorForm.getActor());
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
			result = new ModelAndView("actor/register");

		if (actor instanceof Brotherhood)
			result.addObject("authority", Authority.BROTHERHOOD);
		else if (actor instanceof Chapter)
			result.addObject("authority", Authority.CHAPTER);
		result.addObject("actor", actor);
		result.addObject("message", message);

		return result;
	}

}
