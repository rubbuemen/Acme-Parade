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

import services.MemberService;
import services.ParadeService;
import services.RequestMarchService;
import controllers.AbstractController;
import domain.Parade;
import domain.RequestMarch;

@Controller
@RequestMapping("/requestMarch/member")
public class MemberRequestMarchController extends AbstractController {

	@Autowired
	RequestMarchService	requestMarchService;

	@Autowired
	MemberService		memberService;

	@Autowired
	ParadeService	paradeService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int paradeId) {
		ModelAndView result;
		Collection<RequestMarch> requestsMarch;
		boolean hasPendingOrApprovedRequests;

		requestsMarch = this.requestMarchService.findRequestsMarchByParadeMember(paradeId);
		hasPendingOrApprovedRequests = this.requestMarchService.memberHasPendingOrApprovedRequestToParade(paradeId);

		result = new ModelAndView("requestMarch/list");

		result.addObject("requestsMarch", requestsMarch);
		result.addObject("paradeId", paradeId);
		result.addObject("hasPendingOrApprovedRequests", hasPendingOrApprovedRequests);
		result.addObject("requestURI", "requestMarch/member/list.do");

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView remove(@RequestParam final int paradeId, @RequestParam final int requestMarchId) {
		ModelAndView result;
		Collection<RequestMarch> requestsMarch;
		boolean hasPendingOrApprovedRequests;

		final RequestMarch requestMarch = this.requestMarchService.findOne(requestMarchId);
		this.requestMarchService.delete(requestMarch);

		requestsMarch = this.requestMarchService.findRequestsMarchByParadeMember(paradeId);
		hasPendingOrApprovedRequests = this.requestMarchService.memberHasPendingOrApprovedRequestToParade(paradeId);

		result = new ModelAndView("redirect:/requestMarch/member/list.do");

		result.addObject("requestsMarch", requestsMarch);
		result.addObject("paradeId", paradeId);
		result.addObject("hasPendingOrApprovedRequests", hasPendingOrApprovedRequests);
		result.addObject("requestURI", "requestMarch/member/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int paradeId) {
		ModelAndView result;
		Collection<RequestMarch> requestsMarch;
		boolean hasPendingOrApprovedRequests;

		RequestMarch requestMarch = this.requestMarchService.create();

		final Parade parade = this.paradeService.findOne(paradeId);
		requestMarch = this.requestMarchService.save(requestMarch, parade);

		final Collection<RequestMarch> requestsMarchParade = parade.getRequestsMarch();
		requestsMarchParade.add(requestMarch);
		parade.setRequestsMarch(requestsMarchParade);
		this.paradeService.saveForRequestMarch(parade);

		final Collection<RequestMarch> requestsMarchMember = requestMarch.getMember().getRequestsMarch();
		requestsMarchMember.add(requestMarch);
		requestMarch.getMember().setRequestsMarch(requestsMarchMember);
		this.memberService.save(requestMarch.getMember());

		requestsMarch = this.requestMarchService.findRequestsMarchByParadeMember(paradeId);
		hasPendingOrApprovedRequests = this.requestMarchService.memberHasPendingOrApprovedRequestToParade(paradeId);

		result = new ModelAndView("redirect:/requestMarch/member/list.do");

		result.addObject("requestsMarch", requestsMarch);
		result.addObject("paradeId", paradeId);
		result.addObject("hasPendingOrApprovedRequests", hasPendingOrApprovedRequests);
		result.addObject("requestURI", "requestMarch/member/list.do");

		return result;
	}

}
