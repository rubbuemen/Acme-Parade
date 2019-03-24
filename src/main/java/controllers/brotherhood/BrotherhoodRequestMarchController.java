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
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.MemberService;
import services.RequestMarchService;
import services.SystemConfigurationService;
import controllers.AbstractController;
import domain.RequestMarch;

@Controller
@RequestMapping("/requestMarch/brotherhood")
public class BrotherhoodRequestMarchController extends AbstractController {

	@Autowired
	RequestMarchService			requestMarchService;

	@Autowired
	MemberService				memberService;

	@Autowired
	SystemConfigurationService	systemConfigurationService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int paradeId) {
		ModelAndView result;
		Collection<RequestMarch> requestsMarch;

		try {
			requestsMarch = this.requestMarchService.findRequestsMarchByParade(paradeId);
			result = new ModelAndView("requestMarch/list");
			result.addObject("requestsMarch", requestsMarch);
			result.addObject("requestURI", "requestMarch/brotherhood/list.do");
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(null, "hacking.logged.error", null);
			else
				result = this.createEditModelAndView(null, "commit.error", null);
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView approveOrReject(@RequestParam final int requestMarchId, @RequestParam final String decision) {
		ModelAndView result;

		RequestMarch requestMarch = null;
		try {
			requestMarch = this.requestMarchService.findRequestMarchPenddingBrotherhoodLogged(requestMarchId);
			result = this.createEditModelAndView(requestMarch, decision);

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(requestMarch, "hacking.logged.error", decision);
			else
				result = this.createEditModelAndView(requestMarch, "commit.error", decision);
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(RequestMarch requestMarch, final BindingResult binding) {
		ModelAndView result;

		try {
			requestMarch = this.requestMarchService.reconstruct(requestMarch, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(requestMarch, requestMarch.getStatus());
			else {
				this.requestMarchService.save(requestMarch);
				result = new ModelAndView("redirect:/parade/brotherhood/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The brotherhood must provide an explanation about the request march rejected"))
				result = this.createEditModelAndView(requestMarch, "requestMarch.error.rejectReason", requestMarch.getStatus());
			else if (oops.getMessage().equals("You must select a row position"))
				result = this.createEditModelAndView(requestMarch, "requestMarch.error.selectRow", requestMarch.getStatus());
			else if (oops.getMessage().equals("You must select a column position"))
				result = this.createEditModelAndView(requestMarch, "requestMarch.error.selectColumn", requestMarch.getStatus());
			else if (oops.getMessage().equals("Row position must be greater than 0"))
				result = this.createEditModelAndView(requestMarch, "requestMarch.error.rowGreater0", requestMarch.getStatus());
			else if (oops.getMessage().equals("Column position must be greater than 0"))
				result = this.createEditModelAndView(requestMarch, "requestMarch.error.columnGreater0", requestMarch.getStatus());
			else if (oops.getMessage().equals("You have exceeded the maximum number of rows established"))
				result = this.createEditModelAndView(requestMarch, "requestMarch.error.exceededRow", requestMarch.getStatus());
			else if (oops.getMessage().equals("You have exceeded the maximum number of columns established"))
				result = this.createEditModelAndView(requestMarch, "requestMarch.error.exceededColumn", requestMarch.getStatus());
			else if (oops.getMessage().equals("Two members can not march at the same row/column"))
				result = this.createEditModelAndView(requestMarch, "requestMarch.error.repeatedRowColumn", requestMarch.getStatus());
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(requestMarch, "hacking.logged.error", requestMarch.getStatus());
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error", null);
			else
				result = this.createEditModelAndView(requestMarch, "commit.error", requestMarch.getStatus());
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final RequestMarch requestMarch, final String decision) {
		ModelAndView result;
		result = this.createEditModelAndView(requestMarch, null, decision);
		return result;
	}

	protected ModelAndView createEditModelAndView(final RequestMarch requestMarch, final String message, final String decision) {
		ModelAndView result;

		if (requestMarch == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			result = new ModelAndView("requestMarch/edit");
			if (decision.equals("APPROVED")) {
				final Map<Integer, Integer> suggestedPosition = this.systemConfigurationService.suggestedRowColumn(requestMarch.getId());
				result.addObject("rowSuggested", suggestedPosition.keySet().iterator().next());
				result.addObject("columnSuggested", suggestedPosition.values().iterator().next());
			}
		}

		result.addObject("decision", decision);
		result.addObject("requestMarch", requestMarch);
		result.addObject("actionURL", "requestMarch/brotherhood/edit.do");
		result.addObject("message", message);

		return result;
	}

}
