<%--
 * index.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<display:table pagesize="5" class="displaytag" name="enrolments" requestURI="${requestURI}" id="row">
	<security:authorize access="hasRole('BROTHERHOOD')">
		<spring:message code="actor.name" var="name" />
		<display:column property="member.name" title="${name}" />
		
		<spring:message code="actor.surname" var="surname" />
		<display:column property="member.surname" title="${surname}" />
		
		<spring:message code="actor.email" var="email" />
		<display:column property="member.email" title="${email}" />
		
		<spring:message code="text.infoH" var="infoH" />
		<display:column title="${infoH}">
			<acme:button url="member/show.do?memberId=${row.member.id}" code="button.more" />
		</display:column>
		
		<spring:message code="enrolment.enroll" var="enrollH" />
		<display:column title="${enrollH}">
			<acme:button url="enrolment/brotherhood/edit.do?enrolmentId=${row.id}" code="button.enroll" />
		</display:column>
	</security:authorize>
	<security:authorize access="hasRole('MEMBER')">
		<spring:message code="enrolment.momentRegistered" var="momentRegistered" />
		<display:column title="${momentRegistered}">
			<fmt:formatDate var="format" value="${row.momentRegistered}" pattern="dd/MM/YYYY HH:mm" />
			<jstl:out value="${format}" />
		</display:column>
		
		<spring:message code="enrolment.momentDropOut" var="momentDropOut" />
		<display:column title="${momentDropOut}">
			<fmt:formatDate var="format" value="${row.momentDropOut}" pattern="dd/MM/YYYY HH:mm" />
			<jstl:out value="${format}" />
		</display:column>
		
		<spring:message code="enrolment.positionBrotherhood" var="position" />
		<display:column title="${position}">
			<jstl:if test="${language eq 'en'}">
				<jstl:out value="${row.positionBrotherhood.nameEnglish}" />
			</jstl:if>
			<jstl:if test="${language eq 'es'}">
				<jstl:out value="${row.positionBrotherhood.nameSpanish}" />
			</jstl:if>
		</display:column>
	</security:authorize>
		
</display:table>

<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:button url="member/brotherhood/list.do" code="button.back" />
</security:authorize>

<security:authorize access="hasRole('MEMBER')">
	<acme:button url="brotherhood/member/list.do" code="button.back" />
</security:authorize>
