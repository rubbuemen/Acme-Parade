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

import services.ActorService;
import services.SystemConfigurationService;
import controllers.AbstractController;
import domain.Actor;
import domain.SystemConfiguration;

@Controller
@RequestMapping("/systemConfiguration/administrator")
public class AdministratorSystemConfigurationController extends AbstractController {

	@Autowired
	SystemConfigurationService	systemConfigurationService;

	@Autowired
	ActorService				actorService;				;


	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show() {
		ModelAndView result;
		SystemConfiguration systemConfiguration;

		systemConfiguration = this.systemConfigurationService.getConfiguration();

		result = new ModelAndView("systemConfiguration/show");

		result.addObject("systemConfiguration", systemConfiguration);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		SystemConfiguration systemConfiguration = null;

		try {
			systemConfiguration = this.systemConfigurationService.getConfiguration();
			result = this.createEditModelAndView(systemConfiguration);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(systemConfiguration, "hacking.logged.error");
			else
				result = this.createEditModelAndView(systemConfiguration, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(SystemConfiguration systemConfiguration, final BindingResult binding) {
		ModelAndView result;

		try {
			systemConfiguration = this.systemConfigurationService.reconstruct(systemConfiguration, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(systemConfiguration);
			else {
				this.systemConfigurationService.save(systemConfiguration);
				result = new ModelAndView("redirect:/systemConfiguration/administrator/show.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(systemConfiguration, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(systemConfiguration, "commit.error");

		}

		return result;
	}

	@RequestMapping(value = "/computeSpammer", method = RequestMethod.GET)
	public ModelAndView computeSpammer() {
		ModelAndView result;

		SystemConfiguration systemConfiguration;
		systemConfiguration = this.systemConfigurationService.getConfiguration();

		try {
			this.systemConfigurationService.computeSpammers();
			result = new ModelAndView("systemConfiguration/show");
			result.addObject("systemConfiguration", systemConfiguration);
			result.addObject("message", "successful.action");
		} catch (final Throwable oops) {
			result = new ModelAndView("systemConfiguration/show");
			result.addObject("systemConfiguration", systemConfiguration);
			result.addObject("message", "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/computePolarityScore", method = RequestMethod.GET)
	public ModelAndView computePolarityScore() {
		ModelAndView result;

		SystemConfiguration systemConfiguration;
		systemConfiguration = this.systemConfigurationService.getConfiguration();

		try {
			this.systemConfigurationService.computePolarityScore();
			result = new ModelAndView("systemConfiguration/show");
			result.addObject("systemConfiguration", systemConfiguration);
			result.addObject("message", "successful.action");
		} catch (final Throwable oops) {
			result = new ModelAndView("systemConfiguration/show");
			result.addObject("systemConfiguration", systemConfiguration);
			result.addObject("message", "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/actorsList", method = RequestMethod.GET)
	public ModelAndView actorsList() {
		ModelAndView result;
		Collection<Actor> actorsToBan, actorsBanned;

		actorsToBan = this.actorService.findActorsToBan();
		actorsBanned = this.actorService.findActorsBanned();

		result = new ModelAndView("actor/list");

		result.addObject("actorsToBan", actorsToBan);
		result.addObject("actorsBanned", actorsBanned);

		return result;
	}

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam final int actorId) {
		ModelAndView result;

		final Actor actor = this.actorService.findOne(actorId);
		this.actorService.banActor(actor);
		result = new ModelAndView("redirect:/systemConfiguration/administrator/actorsList.do");

		return result;
	}

	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	public ModelAndView unban(@RequestParam final int actorId) {
		ModelAndView result;

		final Actor actor = this.actorService.findOne(actorId);
		this.actorService.unbanActor(actor);
		result = new ModelAndView("redirect:/systemConfiguration/administrator/actorsList.do");

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final SystemConfiguration systemConfiguration) {
		ModelAndView result;
		result = this.createEditModelAndView(systemConfiguration, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final SystemConfiguration systemConfiguration, final String message) {
		ModelAndView result;

		if (systemConfiguration == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("systemConfiguration/edit");

		result.addObject("systemConfiguration", systemConfiguration);
		result.addObject("actionURL", "systemConfiguration/administrator/edit.do");
		result.addObject("message", message);

		return result;
	}

}
