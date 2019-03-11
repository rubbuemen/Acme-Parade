<%--
 * index.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<ul style="list-style-type: disc;">
	<spring:message code="actor.name" var="name" />
	<li><b>${name}:</b> <jstl:out value="${actor.name}" /></li>
	
	<jstl:if test="${not empty actor.middleName}">
		<spring:message code="actor.middleName" var="middleName" />
		<li><b>${middleName}:</b> <jstl:out value="${actor.middleName}" /></li>
	</jstl:if>
	
	<spring:message code="actor.surname" var="surname" />
	<li><b>${surname}:</b> <jstl:out value="${actor.surname}" /></li>
	
	<jstl:if test="${not empty actor.photo}">
		<spring:message code="actor.photo" var="photo" />
		<li><b>${photo}:</b></li>
		<img src="<jstl:out value='${actor.photo}' />" />
	</jstl:if>
	
	<spring:message code="actor.email" var="email" />
	<li><b>${email}:</b> <jstl:out value="${actor.email}" /></li>
	
	<jstl:if test="${not empty actor.phoneNumber}">
		<spring:message code="actor.phoneNumber" var="phoneNumber" />
		<li><b>${phoneNumber}:</b> <jstl:out value="${actor.phoneNumber}" /></li>
	</jstl:if>
	
	<jstl:if test="${not empty actor.address}">
		<spring:message code="actor.address" var="address" />
		<li><b>${address}:</b> <jstl:out value="${actor.address}" /></li>
	</jstl:if>
	
	<jstl:if test="${authority == 'BROTHERHOOD'}">
		<spring:message code="brotherhood.title" var="title" />
		<li><b>${title}:</b> <jstl:out value="${actor.title}" /></li>
		
		<spring:message code="brotherhood.establishmentDate" var="establishmentDate" />
		<fmt:formatDate var="format" value="${actor.establishmentDate}" pattern="dd/MM/YYYY" />
		<li><b>${establishmentDate}:</b> <jstl:out value="${format}" /></li>
		
		<spring:message code="brotherhood.comments" var="comments" />
		<li><b>${comments}:</b> <jstl:out value="${actor.comments}" /></li>
	</jstl:if>
	
	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="actor.isSpammer" var="isSpammer" />
		<li>
		<b>${isSpammer}: </b>
		<jstl:choose>
			<jstl:when test="${actor.isSpammer == true}">
				<spring:message code="actor.yes" var="yes"/>
				<jstl:out value="${yes}" />
			</jstl:when>
			<jstl:when test="${actor.isSpammer == false}">
				<spring:message code="actor.no" var="no"/>
				<jstl:out value="${no}" />
			</jstl:when>
			<jstl:otherwise>
			N/A
			</jstl:otherwise>
		</jstl:choose>
		</li>
		
		<spring:message code="actor.polarityScore" var="polarityScore" />
		<li>
		<b>${polarityScore}: </b>
		<jstl:choose>
			<jstl:when test="${actor.polarityScore != null}">
				<spring:message code="actor.polarityScore" var="polarityScore"/>
				<jstl:out value="${actor.polarityScore}" />
			</jstl:when>
			<jstl:otherwise>
			N/A
			</jstl:otherwise>
		</jstl:choose>
		</li>
	</security:authorize>
	
	<jstl:if test="${authority == 'MEMBER'}">
		<spring:message code="member.positionBrotherhood" var="positionBrotherhoodH" />
		<li><b>${positionBrotherhoodH}:</b> <jstl:out value="${positionBrotherhood}" /></li>
		
		<spring:message code="member.momentRegistered" var="momentRegisteredH" />
		<fmt:formatDate var="format" value="${momentRegistered}" pattern="dd/MM/YYYY HH:mm" />
		<li><b>${momentRegisteredH}:</b> <jstl:out value="${format}" /></li>
	</jstl:if>
</ul>

<jstl:choose>
	<jstl:when test="${authority == 'MEMBER'}">
		<acme:button url="member/brotherhood/list.do" code="button.back" />
	</jstl:when>
		<jstl:when test="${access == 'enrolment'}">
		<acme:button url="enrolment/brotherhood/list.do" code="button.back" />
	</jstl:when>
	<jstl:otherwise>
		<acme:button url="brotherhood/listGeneric.do" code="button.back" />
	</jstl:otherwise>
</jstl:choose>


