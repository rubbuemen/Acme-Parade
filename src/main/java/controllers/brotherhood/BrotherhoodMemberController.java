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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.EnrolmentService;
import services.MemberService;
import controllers.AbstractController;
import domain.Member;

@Controller
@RequestMapping("/member/brotherhood")
public class BrotherhoodMemberController extends AbstractController {

	@Autowired
	MemberService		memberService;

	@Autowired
	BrotherhoodService	brotherhoodService;

	@Autowired
	EnrolmentService	enrolmentService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Member> members;

		members = this.memberService.findMembersByBrotherhoodLogged();

		result = new ModelAndView("member/list");

		result.addObject("members", members);
		result.addObject("requestURI", "member/brotherhood/list.do");

		return result;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public ModelAndView remove(@RequestParam final int memberId) {
		ModelAndView result;

		final Member member = this.memberService.findMemberBrotherhoodLogged(memberId);

		try {
			this.enrolmentService.removeMemberOfBrotherhood(memberId);
			result = new ModelAndView("redirect:/member/brotherhood/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(member, "hacking.logged.error");
			else
				result = this.createEditModelAndView(member, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Member member) {
		ModelAndView result;
		result = this.createEditModelAndView(member, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Member member, final String message) {
		ModelAndView result;

		result = new ModelAndView("redirect:/welcome/index.do");
		result.addObject("member", member);
		result.addObject("message", message);

		return result;
	}

}
