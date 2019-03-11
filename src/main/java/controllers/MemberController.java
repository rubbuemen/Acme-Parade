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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.BrotherhoodService;
import services.EnrolmentService;
import services.MemberService;
import services.PositionBrotherhoodService;
import domain.Actor;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.PositionBrotherhood;

@Controller
@RequestMapping("/member")
public class MemberController extends AbstractController {

	@Autowired
	MemberService				memberService;

	@Autowired
	BrotherhoodService			brotherhoodService;

	@Autowired
	PositionBrotherhoodService	positionBrotherhoodService;

	@Autowired
	EnrolmentService			enrolmentService;

	@Autowired
	ActorService				actorService;


	@RequestMapping(value = "/listGeneric", method = RequestMethod.GET)
	public ModelAndView listMembersByBrotherhood(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		Collection<Member> members;

		members = this.memberService.findMembersByBrotherhoodId(brotherhoodId);

		result = new ModelAndView("member/listGeneric");

		result.addObject("members", members);
		result.addObject("requestURI", "members/listGeneric.do");

		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView showMember(@RequestParam final int memberId) {
		ModelAndView result;
		Authentication authentication;
		Member actor;
		Enrolment enrolment;
		PositionBrotherhood positionBrotherhood;
		final String language = LocaleContextHolder.getLocale().getLanguage();

		actor = this.memberService.findOne(memberId);

		result = new ModelAndView("actor/show");

		authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			final Actor actorLogged = this.actorService.findActorLogged();
			if (actorLogged instanceof Brotherhood) {
				enrolment = this.enrolmentService.findEnrolmentMemberBrotherhoodLogged(memberId);
				if (enrolment.getMomentRegistered() != null) {
					positionBrotherhood = this.positionBrotherhoodService.findPositionBrotherhoodByMemberIdBrotherhoodLogged(memberId);
					result.addObject("momentRegistered", enrolment.getMomentRegistered());
					result.addObject("authority", Authority.MEMBER);
					if (language.equals("en"))
						result.addObject("positionBrotherhood", positionBrotherhood.getNameEnglish());
					else
						result.addObject("positionBrotherhood", positionBrotherhood.getNameSpanish());
				} else
					result.addObject("access", "enrolment");
			}

		}

		result.addObject("actor", actor);

		return result;
	}
}
