/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.sponsor;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ParadeService;
import services.SponsorService;
import services.SponsorshipService;
import services.SystemConfigurationService;
import controllers.AbstractController;
import domain.Parade;
import domain.Sponsorship;

@Controller
@RequestMapping("/sponsorship/sponsor")
public class SponsorSponsorshipController extends AbstractController {

	@Autowired
	SponsorshipService			sponsorshipService;

	@Autowired
	SponsorService				sponsorService;

	@Autowired
	SystemConfigurationService	systemConfigurationService;

	@Autowired
	ParadeService				paradeService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Sponsorship> sponsorships;

		sponsorships = this.sponsorshipService.findSponsorshipsBySponsorLogged();

		result = new ModelAndView("sponsorship/list");

		result.addObject("sponsorships", sponsorships);
		result.addObject("requestURI", "sponsorship/sponsor/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.create();

		result = this.createEditModelAndView(sponsorship);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		Sponsorship sponsorship = null;

		try {
			sponsorship = this.sponsorshipService.findSponsorshipSponsorLogged(sponsorshipId);
			result = this.createEditModelAndView(sponsorship);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(sponsorship, "hacking.logged.error");
			else
				result = this.createEditModelAndView(sponsorship, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;

		try {
			sponsorship = this.sponsorshipService.reconstruct(sponsorship, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(sponsorship);
			else {
				this.sponsorshipService.save(sponsorship);
				result = new ModelAndView("redirect:/sponsorship/sponsor/list.do");
			}
		} catch (final ValidationException oops) {
			result = this.createEditModelAndView(sponsorship, "commit.error");
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("Invalid credit card"))
				result = this.createEditModelAndView(sponsorship, "creditCard.error.invalid");
			else if (oops.getMessage().equals("Expired credit card"))
				result = this.createEditModelAndView(sponsorship, "creditCard.error.expired");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(sponsorship, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(sponsorship, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/deactivate", method = RequestMethod.GET)
	public ModelAndView deactivate(@RequestParam final int sponsorshipId) {
		ModelAndView result;

		final Sponsorship sponsorship = this.sponsorshipService.findSponsorshipSponsorLogged(sponsorshipId);

		try {
			this.sponsorshipService.deactivate(sponsorship);
			result = new ModelAndView("redirect:/sponsorship/sponsor/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(sponsorship, "hacking.logged.error");
			else
				result = this.createEditModelAndView(sponsorship, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/activate", method = RequestMethod.GET)
	public ModelAndView activate(@RequestParam final int sponsorshipId) {
		ModelAndView result;

		final Sponsorship sponsorship = this.sponsorshipService.findSponsorshipSponsorLogged(sponsorshipId);

		try {
			this.sponsorshipService.activate(sponsorship);
			result = new ModelAndView("redirect:/sponsorship/sponsor/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(sponsorship, "hacking.logged.error");
			else
				result = this.createEditModelAndView(sponsorship, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship) {
		ModelAndView result;
		result = this.createEditModelAndView(sponsorship, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String message) {
		ModelAndView result;

		if (sponsorship == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			final Collection<String> creditCardMakes = this.systemConfigurationService.getConfiguration().getCreditCardMakes();
			final Collection<Parade> paradesAccepted = this.paradeService.findParadesFinalModeAccepted();
			if (sponsorship.getId() == 0)
				result = new ModelAndView("sponsorship/create");
			else
				result = new ModelAndView("sponsorship/edit");
			result.addObject("creditCardMakes", creditCardMakes);
			result.addObject("paradesAccepted", paradesAccepted);
		}

		result.addObject("sponsorship", sponsorship);
		result.addObject("actionURL", "sponsorship/sponsor/edit.do");
		result.addObject("message", message);

		return result;
	}

}
