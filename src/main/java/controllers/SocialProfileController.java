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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SocialProfileService;
import domain.SocialProfile;

@Controller
@RequestMapping("/socialProfile")
public class SocialProfileController extends AbstractController {

	@Autowired
	SocialProfileService	socialProfileService;

	@Autowired
	ActorService			actorService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<SocialProfile> socialProfiles;

		socialProfiles = this.socialProfileService.findSocialProfilesByActorLogged();

		result = new ModelAndView("socialProfile/list");

		result.addObject("socialProfiles", socialProfiles);
		result.addObject("requestURI", "socialProfile/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		SocialProfile socialProfile;

		socialProfile = this.socialProfileService.create();

		result = this.createEditModelAndView(socialProfile);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int socialProfileId) {
		ModelAndView result;
		SocialProfile socialProfile = null;

		try {
			socialProfile = this.socialProfileService.findSocialProfileActorLogged(socialProfileId);
			result = this.createEditModelAndView(socialProfile);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(socialProfile, "hacking.logged.error");
			else
				result = this.createEditModelAndView(socialProfile, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(SocialProfile socialProfile, final BindingResult binding) {
		ModelAndView result;

		try {
			socialProfile = this.socialProfileService.reconstruct(socialProfile, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(socialProfile);
			else {
				this.socialProfileService.save(socialProfile);
				result = new ModelAndView("redirect:/socialProfile/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(socialProfile, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(socialProfile, "commit.error");

		}

		return result;
	}
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int socialProfileId) {
		ModelAndView result;

		SocialProfile socialProfile = null;

		try {
			socialProfile = this.socialProfileService.findSocialProfileActorLogged(socialProfileId);
			this.socialProfileService.delete(socialProfile);
			result = new ModelAndView("redirect:/socialProfile/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(socialProfile, "hacking.logged.error");
			else
				result = this.createEditModelAndView(socialProfile, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final SocialProfile socialProfile) {
		ModelAndView result;
		result = this.createEditModelAndView(socialProfile, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final SocialProfile socialProfile, final String message) {
		ModelAndView result;

		if (socialProfile == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (socialProfile.getId() == 0)
			result = new ModelAndView("socialProfile/create");
		else
			result = new ModelAndView("socialProfile/edit");

		result.addObject("socialProfile", socialProfile);
		result.addObject("actionURL", "socialProfile/edit.do");
		result.addObject("message", message);

		return result;
	}

}
