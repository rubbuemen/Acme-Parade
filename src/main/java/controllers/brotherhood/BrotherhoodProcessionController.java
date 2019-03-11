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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.FloatService;
import services.ProcessionService;
import controllers.AbstractController;
import domain.Float;
import domain.Procession;

@Controller
@RequestMapping("/procession/brotherhood")
public class BrotherhoodProcessionController extends AbstractController {

	@Autowired
	ProcessionService	processionService;

	@Autowired
	BrotherhoodService	brotherhoodService;

	@Autowired
	FloatService		floatService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Procession> processions;

		processions = this.processionService.findProcessionsByBrotherhoodLogged();

		result = new ModelAndView("procession/list");

		result.addObject("processions", processions);
		result.addObject("requestURI", "procession/brotherhood/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Procession procession;

		procession = this.processionService.create();

		result = this.createEditModelAndView(procession);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int processionId) {
		ModelAndView result;
		Procession procession = null;

		try {
			procession = this.processionService.findProcessionBrotherhoodLogged(processionId);
			result = this.createEditModelAndView(procession);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(procession, "hacking.logged.error");
			else
				result = this.createEditModelAndView(procession, "commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Procession procession, final BindingResult binding) {
		ModelAndView result;

		try {
			procession = this.processionService.reconstruct(procession, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(procession);
			else {
				this.processionService.save(procession);
				result = new ModelAndView("redirect:/procession/brotherhood/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can not organise any processions until you selected an area"))
				result = this.createEditModelAndView(procession, "procession.error.area");
			else if (oops.getMessage().equals("You can only save processions that are not in final mode"))
				result = this.createEditModelAndView(procession, "procession.error.save.finalMode");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(procession, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(procession, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int processionId) {
		ModelAndView result;

		final Procession procession = this.processionService.findProcessionBrotherhoodLogged(processionId);

		try {
			this.processionService.delete(procession);
			result = new ModelAndView("redirect:/procession/brotherhood/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can only delete processions that are not in final mode"))
				result = this.createEditModelAndView(procession, "procession.error.delete.finalMode");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(procession, "hacking.logged.error");
			else
				result = this.createEditModelAndView(procession, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public ModelAndView changeFinalMode(Procession procession, final BindingResult binding, @RequestParam final int processionId) {
		ModelAndView result;

		procession = this.processionService.findProcessionBrotherhoodLogged(processionId);

		try {
			this.processionService.changeFinalMode(procession);
			result = new ModelAndView("redirect:/procession/brotherhood/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This procession is already in final mode"))
				result = this.createEditModelAndView(procession, "procession.error.change.finalMode");
			else
				result = this.createEditModelAndView(procession, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Procession procession) {
		ModelAndView result;
		result = this.createEditModelAndView(procession, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Procession procession, final String message) {
		ModelAndView result;

		if (procession == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			if (procession.getId() == 0)
				result = new ModelAndView("procession/create");
			else
				result = new ModelAndView("procession/edit");
			final Collection<Float> floatsBrotherhoodLogged = this.floatService.findFloatsByBrotherhoodLogged();
			result.addObject("floats", floatsBrotherhoodLogged);
		}

		result.addObject("procession", procession);
		result.addObject("actionURL", "procession/brotherhood/edit.do");
		result.addObject("message", message);

		return result;
	}

}
