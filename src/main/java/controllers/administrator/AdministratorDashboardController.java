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

import java.text.DecimalFormat;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Member;
import domain.Procession;

@Controller
@RequestMapping("/dashboard/administrator")
public class AdministratorDashboardController extends AbstractController {

	@Autowired
	AdministratorService	administratorService;


	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView dashboard() {

		final ModelAndView result;

		result = new ModelAndView("dashboard/show");

		final String language = LocaleContextHolder.getLocale().getLanguage();
		result.addObject("language", language);

		//Query C1
		final String[] queryC1 = this.administratorService.dashboardQueryC1().split(",");
		final String avgQueryC1 = queryC1[0];
		final String minQueryC1 = queryC1[1];
		final String maxQueryC1 = queryC1[2];
		final String stddevQueryC1 = queryC1[3];
		result.addObject("avgQueryC1", avgQueryC1);
		result.addObject("minQueryC1", minQueryC1);
		result.addObject("maxQueryC1", maxQueryC1);
		result.addObject("stddevQueryC1", stddevQueryC1);

		//Query C2
		final Collection<Brotherhood> queryC2 = this.administratorService.dashboardQueryC2();
		result.addObject("queryC2", queryC2);

		//Query C3
		final Collection<Brotherhood> queryC3 = this.administratorService.dashboardQueryC3();
		result.addObject("queryC3", queryC3);

		//Query C4
		final Collection<Object[]> queryC4 = this.administratorService.dashboardQueryC4();
		result.addObject("queryC4", queryC4);

		//Query C5
		final Collection<Procession> queryC5 = this.administratorService.dashboardQueryC5();
		result.addObject("queryC5", queryC5);

		//Query C6
		final Collection<Object[]> queryC6 = this.administratorService.dashboardQueryC6();
		result.addObject("queryC6", queryC6);

		//Query C7
		final Collection<Member> queryC7 = this.administratorService.dashboardQueryC7();
		result.addObject("queryC7", queryC7);

		//Query C8
		final Collection<Object[]> queryC8 = this.administratorService.dashboardQueryC8();
		result.addObject("queryC8", queryC8);

		//Query B1
		final String[] queryB1 = this.administratorService.dashboardQueryB1().split(",");
		final String ratioQueryB1 = queryB1[0];
		final String countQueryB1 = queryB1[1];
		final String minQueryB1 = queryB1[2];
		final String maxQueryB1 = queryB1[3];
		final String avgQueryB1 = queryB1[4];
		final String stddevQueryB1 = queryB1[5];
		result.addObject("ratioQueryB1", ratioQueryB1);
		result.addObject("countQueryB1", countQueryB1);
		result.addObject("avgQueryB1", avgQueryB1);
		result.addObject("minQueryB1", minQueryB1);
		result.addObject("maxQueryB1", maxQueryB1);
		result.addObject("stddevQueryB1", stddevQueryB1);

		//Query B2
		final String[] queryB2 = this.administratorService.dashboardQueryB2().split(",");
		final String minQueryB2 = queryB2[0];
		final String maxQueryB2 = queryB2[1];
		final String avgQueryB2 = queryB2[2];
		final String stddevQueryB2 = queryB1[3];
		result.addObject("minQueryB2", minQueryB2);
		result.addObject("maxQueryB2", maxQueryB2);
		result.addObject("avgQueryB2", avgQueryB2);
		result.addObject("stddevQueryB2", stddevQueryB2);

		//Query B3
		final String ratioQueryB3 = this.administratorService.dashboardQueryB3();
		result.addObject("ratioQueryB3", ratioQueryB3);

		//Query A+1
		final String[] queryAPlus1 = this.administratorService.dashboardQueryAPlus1().split(",");
		final String percentageSpammersQueryAplus1 = queryAPlus1[0];
		final String percentageNotSpammersQueryAplus1 = queryAPlus1[1];
		result.addObject("percentageSpammersQueryAplus1", percentageSpammersQueryAplus1);
		result.addObject("percentageNotSpammersQueryAplus1", percentageNotSpammersQueryAplus1);

		//Query A+2
		final DecimalFormat formatDecimals = new DecimalFormat(".##");
		final Double[] queryAPlus2 = this.administratorService.dashboardQueryAPlus2();
		Double avgPolarityBrotherhoodsQueryAplus2 = null;
		Double avgPolarityMembersQueryAplus2 = null;
		Double avgPolarityAdministratorsQueryAplus2 = null;
		if (queryAPlus2[0] != null)
			avgPolarityBrotherhoodsQueryAplus2 = Double.valueOf(formatDecimals.format(queryAPlus2[0] + 1));
		if (queryAPlus2[1] != null)
			avgPolarityMembersQueryAplus2 = Double.valueOf(formatDecimals.format(queryAPlus2[1] + 1));
		if (queryAPlus2[2] != null)
			avgPolarityAdministratorsQueryAplus2 = Double.valueOf(formatDecimals.format(queryAPlus2[2] + 1));

		result.addObject("avgPolarityBrotherhoodsQueryAplus2", avgPolarityBrotherhoodsQueryAplus2);
		result.addObject("avgPolarityMembersQueryAplus2", avgPolarityMembersQueryAplus2);
		result.addObject("avgPolarityAdministratorsQueryAplus2", avgPolarityAdministratorsQueryAplus2);

		return result;
	}

}
