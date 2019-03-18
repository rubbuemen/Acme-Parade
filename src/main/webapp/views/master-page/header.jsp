<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<div>
	<a href="welcome/index.do"><img src="${bannerUrl}" alt="${nameSystem} Co., Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="brotherhood/listGeneric.do"><spring:message code="master.page.brotherhoods" /></a></li>
			<li><a class="fNiv"><spring:message code="master.page.register" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/register-brotherhood.do"><spring:message code="master.page.register.brotherhood" /></a></li>
					<li><a href="actor/register-member.do"><spring:message code="master.page.register.member" /></a></li>
					<li><a href="actor/register-chapter.do"><spring:message code="master.page.register.chapter" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('BROTHERHOOD')">
			<li>
				<a class="fNiv"><spring:message code="master.page.brotherhood" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="float/brotherhood/list.do"><spring:message code="master.page.floats" /></a></li>
					<li><a href="parade/brotherhood/list.do"><spring:message code="master.page.parades" /></a></li>
					<li><a href="member/brotherhood/list.do"><spring:message code="master.page.members" /></a></li>
					<li><a href="enrolment/brotherhood/list.do"><spring:message code="master.page.enrolments" /></a></li>
					<jstl:if test="${showArea eq true}"><li><a href="area/brotherhood/list.do"><spring:message code="master.page.selectArea" /></a></li></jstl:if>
					<li><a href="history/brotherhood/list.do"><spring:message code="master.page.history" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('MEMBER')">
			<li>
				<a class="fNiv"><spring:message code="master.page.member" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="brotherhood/member/list.do"><spring:message code="master.page.brotherhoods" /></a></li>
					<li><a href="finder/member/edit.do"><spring:message code="master.page.finder" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('CHAPTER')">
			<li>
				<a class="fNiv"><spring:message code="master.page.chapter" /></a>
				<ul>
					<li class="arrow"></li>
					<jstl:if test="${showArea eq true}"><li><a href="area/chapter/list.do"><spring:message code="master.page.assignArea" /></a></li></jstl:if>
					<li><a href="brotherhood/chapter/list.do"><spring:message code="master.page.brotherhoods" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		
		<security:authorize access="hasRole('ADMIN')">
			<li>
				<a class="fNiv"><spring:message code="master.page.admin" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/administrator/register-administrator.do"><spring:message code="master.page.register.admin" /></a></li>
					<li><a href="positionBrotherhood/administrator/list.do"><spring:message code="master.page.positionsBrotherhood" /></a></li>	
					<li><a href="area/administrator/list.do"><spring:message code="master.page.areas" /></a></li>	
					<li><a href="dashboard/administrator/show.do"><spring:message code="master.page.dashboard" /></a></li>	
					<li><a href="systemConfiguration/administrator/show.do"><spring:message code="master.page.systemConfiguration" /></a></li>	
					<li><a href="systemConfiguration/administrator/actorsList.do"><spring:message code="master.page.actorsList" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
		<li><a class="fNiv" href="brotherhood/listGeneric.do"><spring:message code="master.page.brotherhoods" /></a></li>
		<li><a class="fNiv" href="socialProfile/list.do"><spring:message code="master.page.socialProfiles" /></a></li>
		<li><a class="fNiv" href="box/list.do"><spring:message code="master.page.boxes" /></a></li>
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('BROTHERHOOD')">
						<li><a href="actor/brotherhood/edit.do"><spring:message code="master.page.edit.profile" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('MEMBER')">
						<li><a href="actor/member/edit.do"><spring:message code="master.page.edit.profile" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="actor/administrator/edit.do"><spring:message code="master.page.edit.profile" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('CHAPTER')">
						<li><a href="actor/chapter/edit.do"><spring:message code="master.page.edit.profile" /></a></li>
					</security:authorize>				
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

