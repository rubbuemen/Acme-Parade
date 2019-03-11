/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.member;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.EnrolmentService;
import services.MemberService;
import controllers.AbstractController;
import domain.Brotherhood;

@Controller
@RequestMapping("/brotherhood/member")
public class MemberBrotherhoodController extends AbstractController {

	@Autowired
	BrotherhoodService	brotherhoodService;

	@Autowired
	MemberService		memberService;

	@Autowired
	EnrolmentService	enrolmentService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Brotherhood> brotherhoods, brotherhoodsNotBelongs;

		brotherhoods = this.brotherhoodService.findBrotherhoodsByMemberLogged();
		brotherhoodsNotBelongs = this.brotherhoodService.findBrotherhoodsNotBelongsByMemberLogged();

		brotherhoodsNotBelongs.removeAll(brotherhoods);

		result = new ModelAndView("brotherhood/list");

		result.addObject("brotherhoods", brotherhoods);
		result.addObject("brotherhoodsNotBelongs", brotherhoodsNotBelongs);
		result.addObject("requestURI", "brotherhood/member/list.do");

		return result;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public ModelAndView remove(@RequestParam final int brotherhoodId) {
		ModelAndView result;

		final Brotherhood brotherhood = this.brotherhoodService.findBrotherhoodMemberLogged(brotherhoodId);

		try {
			this.enrolmentService.dropOutOfBrotherhood(brotherhoodId);
			result = new ModelAndView("redirect:/brotherhood/member/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(brotherhood, "hacking.logged.error");
			else
				result = this.createEditModelAndView(brotherhood, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Brotherhood brotherhood) {
		ModelAndView result;
		result = this.createEditModelAndView(brotherhood, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Brotherhood brotherhood, final String message) {
		ModelAndView result;

		result = new ModelAndView("redirect:/welcome/index.do");
		result.addObject("brotherhood", brotherhood);
		result.addObject("message", message);

		return result;
	}

}
