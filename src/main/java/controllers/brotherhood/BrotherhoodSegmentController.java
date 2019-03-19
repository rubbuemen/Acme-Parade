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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ParadeService;
import services.SegmentService;
import controllers.AbstractController;
import domain.Parade;
import domain.Segment;

@Controller
@RequestMapping("/segment/brotherhood")
public class BrotherhoodSegmentController extends AbstractController {

	@Autowired
	SegmentService	segmentService;

	@Autowired
	ParadeService	paradeService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int paradeId) {
		ModelAndView result;
		Collection<Segment> segments;
		final Parade parade = this.paradeService.findOne(paradeId);

		try {
			segments = this.segmentService.findSegmentsByParade(paradeId);
			result = new ModelAndView("segment/list");
			result.addObject("segments", segments);
			result.addObject("requestURI", "segment/brotherhood/list.do");
			result.addObject("parade", parade);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(null, "hacking.logged.error", parade);
			else
				result = this.createEditModelAndView(null, "commit.error", parade);
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int paradeId) {
		ModelAndView result;
		Segment segment;

		final Parade parade = this.paradeService.findOne(paradeId);
		final Collection<Segment> segments = this.segmentService.findSegmentsByParade(paradeId);
		segment = this.segmentService.create();

		result = this.createEditModelAndView(segment, parade);
		result.addObject("segments", segments);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int paradeId, @RequestParam final int segmentId) {
		ModelAndView result;
		Segment segment = null;
		final Parade parade = this.paradeService.findOne(paradeId);

		try {
			this.segmentService.findSegmentsByParade(paradeId);
			segment = this.segmentService.findOne(segmentId);
			result = this.createEditModelAndView(segment, parade);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(segment, "hacking.logged.error", parade);
			else
				result = this.createEditModelAndView(segment, "commit.error", parade);
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Segment segment, final BindingResult binding, @RequestParam final int paradeId) {
		ModelAndView result;

		final Parade parade = this.paradeService.findOne(paradeId);

		try {
			segment = this.segmentService.reconstruct(segment, binding);
			this.segmentService.findSegmentsByParade(paradeId);
			final Collection<Segment> segments = this.segmentService.findSegmentsByParade(paradeId);
			if (binding.hasErrors())
				result = this.createEditModelAndView(segment, parade);
			else {
				this.segmentService.save(segment, parade);
				result = new ModelAndView("redirect:/segment/brotherhood/list.do?paradeId=" + paradeId);
			}
			result.addObject("segments", segments);
		} catch (final ValidationException oops) {
			result = this.createEditModelAndView(segment, "commit.error", parade);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The time at which the parade is expected to be reaching the origin must be before than the time at which it's expected to be reaching the destination"))
				result = this.createEditModelAndView(segment, "segment.error.times", parade);
			else if (oops.getMessage().equals("The time at which the parade is expected to be reaching the destination must be before than the time it is expected to reach the destination of the next segment"))
				result = this.createEditModelAndView(segment, "segment.error.timeNextSegment", parade);
			else if (oops.getMessage().equals("The time at which the parade is expected to be reaching the origin must be after than the time it is expected to reach the origin of the previous segment"))
				result = this.createEditModelAndView(segment, "segment.error.timePreviousSegment", parade);
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(segment, "hacking.logged.error", parade);
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error", parade);
			else
				result = this.createEditModelAndView(segment, "commit.error", parade);
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int segmentId, @RequestParam final int paradeId) {
		ModelAndView result;

		final Parade parade = this.paradeService.findOne(paradeId);
		this.segmentService.findSegmentsByParade(paradeId);
		final Segment segment = this.segmentService.findOne(segmentId);

		try {
			this.segmentService.delete(segment, parade);
			result = new ModelAndView("redirect:/segment/brotherhood/list.do?paradeId=" + paradeId);
			result.addObject("parade", parade);

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(segment, "hacking.logged.error", parade);
			else
				result = this.createEditModelAndView(segment, "commit.error", parade);
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Segment segment, final Parade parade) {
		ModelAndView result;
		result = this.createEditModelAndView(segment, null, parade);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Segment segment, final String message, final Parade parade) {
		ModelAndView result;

		if (segment == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (segment.getId() == 0) {
			result = new ModelAndView("segment/create");
			if (parade.getSegments().size() > 0) {
				final List<Segment> segments = new ArrayList<>(parade.getSegments());
				final Segment lastSegment = segments.get(segments.size() - 1);
				final String timeReachOrigin = lastSegment.getTimeReachDestination().toString();
				result.addObject("originLatitude", lastSegment.getDestination().getLatitude());
				result.addObject("originLongitude", lastSegment.getDestination().getLongitude());
				result.addObject("timeReachOrigin", timeReachOrigin.substring(0, 5));
				result.addObject("segments", segments);
			}

		} else
			result = new ModelAndView("segment/edit");

		result.addObject("parade", parade);
		result.addObject("segment", segment);
		result.addObject("actionURL", "segment/brotherhood/edit.do");
		result.addObject("message", message);

		return result;
	}

}
