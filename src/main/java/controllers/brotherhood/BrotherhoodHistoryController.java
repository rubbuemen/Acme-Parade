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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.BrotherhoodService;
import services.HistoryService;
import controllers.AbstractController;
import domain.History;

@Controller
@RequestMapping("/history/brotherhood")
public class BrotherhoodHistoryController extends AbstractController {

	@Autowired
	HistoryService		historyService;

	@Autowired
	BrotherhoodService	brotherhoodService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		History history;

		history = this.historyService.findHistoryByBrotherhoodLogged();

		result = new ModelAndView("history/list");

		result.addObject("history", history);
		result.addObject("authority", Authority.BROTHERHOOD);

		if (history != null) {
			result.addObject("inceptionRecord", history.getInceptionRecord());
			result.addObject("periodRecords", history.getPeriodRecords());
			result.addObject("legalRecords", history.getLegalRecords());
			result.addObject("linkRecords", history.getLinkRecords());
			result.addObject("miscellaneousRecords", history.getMiscellaneousRecords());
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		History history;

		history = this.historyService.create();

		result = this.createEditModelAndView(history);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(History history, final BindingResult binding) {
		ModelAndView result;

		try {
			history = this.historyService.reconstruct(history, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(history);
			else {
				this.historyService.save(history);
				result = new ModelAndView("redirect:/history/brotherhood/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(history, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(history, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final History history) {
		ModelAndView result;
		result = this.createEditModelAndView(history, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final History history, final String message) {
		ModelAndView result;

		if (history == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (history.getId() == 0)
			result = new ModelAndView("history/create");
		else
			result = new ModelAndView("history/edit");

		result.addObject("history", history);
		result.addObject("actionURL", "history/brotherhood/edit.do");
		result.addObject("message", message);

		return result;
	}

}
