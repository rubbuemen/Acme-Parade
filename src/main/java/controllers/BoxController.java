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
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BoxService;
import domain.Box;

@Controller
@RequestMapping("/box")
public class BoxController extends AbstractController {

	@Autowired
	BoxService		boxService;

	@Autowired
	ActorService	actorService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final String parentBoxId) {
		ModelAndView result;
		Collection<Box> boxes = null;

		try {
			if (parentBoxId == null)
				boxes = this.boxService.findRootBoxesByActorLogged();
			else {
				final Box parentBox = this.boxService.findBoxActorLogged(Integer.valueOf(parentBoxId));
				boxes = parentBox.getChildsBox();
			}
			result = new ModelAndView("box/list");
			result.addObject("requestURI", "box/list.do");
			result.addObject("boxes", boxes);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(null, "hacking.logged.error");
			else
				result = this.createEditModelAndView(null, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createBox() {
		ModelAndView result;
		Box box;

		box = this.boxService.create();

		result = this.createEditModelAndView(box);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int boxId) {
		ModelAndView result;
		Box box = null;

		try {
			box = this.boxService.findBoxActorLogged(boxId);
			result = this.createEditModelAndView(box);
			if (box.getParentBox() != null)
				result.addObject("boxParentOld", box.getParentBox().getId());

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(box, "hacking.logged.error");
			else
				result = this.createEditModelAndView(box, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Box box, final BindingResult binding, @RequestParam(required = false) final String boxParentOld) {
		ModelAndView result;

		try {
			box = this.boxService.reconstruct(box, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(box);
			else {
				Box oldParentBox = null;
				if (!boxParentOld.isEmpty())
					oldParentBox = this.boxService.findOne(Integer.valueOf(boxParentOld));
				this.boxService.save(box, oldParentBox);
				result = new ModelAndView("redirect:list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(box, "hacking.logged.error");
			else if (oops.getMessage().equals("The box can not be called the same as the system boxes"))
				result = this.createEditModelAndView(box, "box.error.nameSystem");
			else if (oops.getMessage().equals("It is not allowed to edit the system boxes"))
				result = this.createEditModelAndView(box, "box.error.editBoxSystem");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(box, "commit.error");

		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int boxId) {
		ModelAndView result;

		Box box = null;

		try {
			box = this.boxService.findBoxActorLogged(boxId);
			this.boxService.delete(box);
			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(box, "hacking.logged.error");
			else if (oops.getMessage().equals("It is not allowed to delete the system boxes"))
				result = this.createEditModelAndView(box, "box.error.deleteBoxSystem");
			else if (oops.getMessage().contains("[domain.Box#<null>];") || oops.getMessage().contains("SQL [n/a]"))
				result = this.createEditModelAndView(box, "box.error.delete");
			else
				result = this.createEditModelAndView(box, "commit.error");

		}

		return result;
	}
	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Box box) {
		ModelAndView res;
		res = this.createEditModelAndView(box, null);
		return res;
	}

	protected ModelAndView createEditModelAndView(final Box box, final String message) {
		ModelAndView result;

		if (box == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (box.getId() == 0) {
			final Collection<Box> boxes = this.boxService.findBoxesByActorLogged();
			result = new ModelAndView("box/create");
			result.addObject("boxes", boxes);
		} else {
			final Collection<Box> boxesRec = this.boxService.findBoxesByActorLogged();
			final Collection<Box> boxesToDelete = this.boxService.findBoxesToDelete(box.getId(), new HashSet<Box>());
			boxesRec.removeAll(boxesToDelete);
			boxesRec.remove(box);
			result = new ModelAndView("box/edit");
			result.addObject("boxes", boxesRec);
		}

		result.addObject("box", box);
		result.addObject("actionURL", "box/edit.do");
		result.addObject("message", message);

		return result;
	}
}
