/*
 * AbstractController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SystemConfigurationService;
import domain.Actor;
import domain.Brotherhood;
import domain.SystemConfiguration;

@Controller
public class AbstractController {

	@Autowired
	SystemConfigurationService	systemConfigurationService;

	@Autowired
	ActorService				actorService;


	// Panic handler ----------------------------------------------------------

	@ExceptionHandler(Throwable.class)
	@ModelAttribute
	public ModelAndView panic(final Throwable oops) {
		ModelAndView result;

		result = new ModelAndView("misc/panic");
		result.addObject("name", ClassUtils.getShortName(oops.getClass()));
		result.addObject("exception", oops.getMessage());
		result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));

		return result;
	}

	@ModelAttribute
	public void systemConfiguration(final Model model) {
		SystemConfiguration systemConfiguration;
		final String language = LocaleContextHolder.getLocale().getLanguage();

		systemConfiguration = this.systemConfigurationService.getConfiguration();

		model.addAttribute("nameSystem", systemConfiguration.getNameSystem());
		model.addAttribute("bannerUrl", systemConfiguration.getBannerUrl());
		if (language.equals("en"))
			model.addAttribute("welcomeMessage", systemConfiguration.getWelcomeMessageEnglish());
		else
			model.addAttribute("welcomeMessage", systemConfiguration.getWelcomeMessageSpanish());

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			final Actor actorLogged = this.actorService.findActorLogged();
			if (actorLogged instanceof Brotherhood) {
				final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;
				boolean showArea = false;
				if (brotherhoodLogged.getArea() == null) {
					showArea = true;
					model.addAttribute("showArea", showArea);
				}

			}
		}

	}

}
