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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.HistoryService;
import domain.History;

@Controller
@RequestMapping("/history")
public class HistoryController extends AbstractController {

	@Autowired
	HistoryService	historyService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listHistoryByBrotherhood(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		History history;

		history = this.historyService.findHistoryByBrotherhoodId(brotherhoodId);

		result = new ModelAndView("history/list");

		result.addObject("history", history);

		if (history != null) {
			result.addObject("inceptionRecord", history.getInceptionRecord());
			result.addObject("periodRecords", history.getPeriodRecords());
			result.addObject("legalRecords", history.getLegalRecords());
			result.addObject("linkRecords", history.getLinkRecords());
			result.addObject("miscellaneousRecords", history.getMiscellaneousRecords());
		}

		return result;
	}

}
