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
import services.ParadeService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Parade;
import domain.Sponsorship;

@Controller
@RequestMapping("/parade/chapter")
public class ChapterParadeController extends AbstractController {

	@Autowired
	ParadeService		paradeService;

	@Autowired
	BrotherhoodService	brotherhoodService;

	@Autowired
	SponsorshipService	sponsorshipService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listByBrotherhood(@RequestParam final int brotherhoodId) {
		ModelAndView result;

		Collection<Parade> parades = null;

		try {
			parades = this.paradeService.findParadesFinalModeOrderByStatusByBrotherhoodId(brotherhoodId);
			result = this.createEditModelAndView(parades);

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(parades, "hacking.logged.error");
			else
				result = this.createEditModelAndView(parades, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView approveOrReject(@RequestParam final int paradeId, @RequestParam final String decision) {
		ModelAndView result;

		Parade parade = null;
		try {
			parade = this.paradeService.findParadeSubmittedChapterLogged(paradeId);
			final Brotherhood brotherhoodParade = this.brotherhoodService.findBrotherhoodByParadeId(paradeId);
			if (decision.equals("ACCEPTED")) {
				this.paradeService.acceptParade(parade);
				result = new ModelAndView("redirect:/parade/chapter/list.do?brotherhoodId=" + brotherhoodParade.getId());
			} else
				result = this.createEditModelAndView(parade, decision);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(parade, "hacking.logged.error", decision);
			else if (oops.getMessage().equals("The parade does not have the status submitted"))
				result = this.createEditModelAndView(parade, "parade.error.notSubmmited", decision);
			else
				result = this.createEditModelAndView(parade, "commit.error", decision);
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Parade parade, final BindingResult binding) {
		ModelAndView result;

		try {
			parade = this.paradeService.reconstruct(parade, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(parade, parade.getStatus());
			else {
				this.paradeService.save(parade);
				final Brotherhood brotherhoodParade = this.brotherhoodService.findBrotherhoodByParadeId(parade.getId());
				result = new ModelAndView("redirect:/parade/chapter/list.do?brotherhoodId=" + brotherhoodParade.getId());
				result.addObject("brotherhood", brotherhoodParade);
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can only making decisions on parades that are in final mode"))
				result = this.createEditModelAndView(parade, "parade.error.decisionFinalMode", parade.getStatus());
			else if (oops.getMessage().equals("The chapter must provide an explanation about the parade rejected"))
				result = this.createEditModelAndView(parade, "parade.error.rejectReason", parade.getStatus());
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(parade, "hacking.logged.error", parade.getStatus());
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error", null);
			else
				result = this.createEditModelAndView(parade, "commit.error", parade.getStatus());
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Collection<Parade> parades) {
		ModelAndView result;
		result = this.createEditModelAndView(parades, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Parade> parades, final String message) {
		ModelAndView result;

		if (parades == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("parade/list");

		if (parades != null) {
			if (!parades.isEmpty()) {
				final Map<Parade, Sponsorship> randomSponsorship = new HashMap<>();
				for (final Parade p : parades) {
					final Sponsorship sponsorship = this.sponsorshipService.findRandomSponsorShip(p);
					if (sponsorship != null)
						randomSponsorship.put(p, sponsorship);
				}
				result.addObject("randomSponsorship", randomSponsorship);
			}
			final Brotherhood brotherhoodParade = this.brotherhoodService.findBrotherhoodByParadeId(parades.iterator().next().getId());
			result.addObject("requestURI", "parade/chapter/list.do?brotherhoodId=" + brotherhoodParade.getId());
		}

		result.addObject("parades", parades);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Parade parade, final String decision) {
		ModelAndView result;
		result = this.createEditModelAndView(parade, null, decision);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Parade parade, final String message, final String decision) {
		ModelAndView result;

		if (parade == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			result = new ModelAndView("parade/edit");
			final Brotherhood brotherhoodParade = this.brotherhoodService.findBrotherhoodByParadeId(parade.getId());
			result.addObject("brotherhood", brotherhoodParade);
		}

		result.addObject("decision", decision);
		result.addObject("parade", parade);
		result.addObject("actionURL", "parade/chapter/edit.do");
		result.addObject("message", message);

		return result;
	}

}
