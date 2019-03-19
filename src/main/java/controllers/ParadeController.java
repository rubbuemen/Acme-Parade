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
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.BrotherhoodService;
import services.ParadeService;
import services.SponsorshipService;
import domain.Area;
import domain.Parade;
import domain.Sponsorship;

@Controller
@RequestMapping("/parade")
public class ParadeController extends AbstractController {

	@Autowired
	ParadeService		paradeService;

	@Autowired
	BrotherhoodService	brotherhoodService;

	@Autowired
	AreaService			areaService;

	@Autowired
	SponsorshipService	sponsorshipService;


	@RequestMapping(value = "/listGeneric", method = RequestMethod.GET)
	public ModelAndView listParadesByBrotherhood(@RequestParam final int brotherhoodId, @RequestParam(required = false) final Integer areaId) {
		ModelAndView result;
		Collection<Parade> parades;

		parades = this.paradeService.findParadesAcceptedByBrotherhoodId(brotherhoodId);
		Area area = null;

		if (areaId != null)
			if (!parades.isEmpty())
				area = this.areaService.findAreaByParadeId(parades.iterator().next().getId());
			else
				area = this.areaService.findOne(areaId);

		result = new ModelAndView("parade/listGeneric");

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
		result.addObject("area", area);
		result.addObject("requestURI", "parades/listGeneric.do");

		return result;
	}
}
