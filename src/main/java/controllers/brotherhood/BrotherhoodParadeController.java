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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.FloatService;
import services.ParadeService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Float;
import domain.Parade;
import domain.Sponsorship;

@Controller
@RequestMapping("/parade/brotherhood")
public class BrotherhoodParadeController extends AbstractController {

	@Autowired
	ParadeService		paradeService;

	@Autowired
	BrotherhoodService	brotherhoodService;

	@Autowired
	FloatService		floatService;

	@Autowired
	SponsorshipService	sponsorshipService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Parade> parades;

		parades = this.paradeService.findParadesByBrotherhoodLogged();

		result = new ModelAndView("parade/list");

		if (parades != null)
			if (!parades.isEmpty()) {
				final Map<Parade, Sponsorship> randomSponsorship = new HashMap<>();
				for (final Parade p : parades) {
					final Sponsorship sponsorship = this.sponsorshipService.findRandomSponsorShip(p);
					if (sponsorship != null)
						randomSponsorship.put(p, sponsorship);

				}
				result.addObject("randomSponsorship", randomSponsorship);
			}

		result.addObject("parades", parades);
		result.addObject("requestURI", "parade/brotherhood/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Parade parade;

		parade = this.paradeService.create();

		result = this.createEditModelAndView(parade);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade = null;

		try {
			parade = this.paradeService.findParadeBrotherhoodLogged(paradeId);
			result = this.createEditModelAndView(parade);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(parade, "hacking.logged.error");
			else
				result = this.createEditModelAndView(parade, "commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Parade parade, final BindingResult binding) {
		ModelAndView result;

		try {
			parade = this.paradeService.reconstruct(parade, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(parade);
			else {
				this.paradeService.save(parade);
				result = new ModelAndView("redirect:/parade/brotherhood/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can not organise any parades until you selected an area"))
				result = this.createEditModelAndView(parade, "parade.error.area");
			else if (oops.getMessage().equals("You can only save parades that are not in final mode"))
				result = this.createEditModelAndView(parade, "parade.error.save.finalMode");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(parade, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(parade, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int paradeId) {
		ModelAndView result;

		final Parade parade = this.paradeService.findParadeBrotherhoodLogged(paradeId);

		try {
			this.paradeService.delete(parade);
			result = new ModelAndView("redirect:/parade/brotherhood/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can only delete parades that are not in final mode"))
				result = this.createEditModelAndView(parade, "parade.error.delete.finalMode");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(parade, "hacking.logged.error");
			else
				result = this.createEditModelAndView(parade, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public ModelAndView changeFinalMode(@RequestParam final int paradeId) {
		ModelAndView result;

		final Parade parade = this.paradeService.findParadeBrotherhoodLogged(paradeId);

		try {
			this.paradeService.changeFinalMode(parade);
			result = new ModelAndView("redirect:/parade/brotherhood/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This parade is already in final mode"))
				result = this.createEditModelAndView(parade, "parade.error.change.finalMode");
			else
				result = this.createEditModelAndView(parade, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/copy", method = RequestMethod.GET)
	public ModelAndView copy(@RequestParam final int paradeId) {
		ModelAndView result;

		final Parade parade = this.paradeService.findParadeBrotherhoodLogged(paradeId);

		try {
			this.paradeService.copyParade(parade.getId());
			result = new ModelAndView("redirect:/parade/brotherhood/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(parade, "hacking.logged.error");
			else
				result = this.createEditModelAndView(parade, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Parade parade) {
		ModelAndView result;
		result = this.createEditModelAndView(parade, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Parade parade, final String message) {
		ModelAndView result;

		if (parade == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			if (parade.getId() == 0)
				result = new ModelAndView("parade/create");
			else
				result = new ModelAndView("parade/edit");
			final Collection<Float> floatsBrotherhoodLogged = this.floatService.findFloatsByBrotherhoodLogged();
			result.addObject("floats", floatsBrotherhoodLogged);
		}

		result.addObject("parade", parade);
		result.addObject("actionURL", "parade/brotherhood/edit.do");
		result.addObject("message", message);

		return result;
	}

}
