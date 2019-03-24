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

<display:table pagesize="5" class="displaytag" name="members" requestURI="${requestURI}" id="row">
	
	<spring:message code="actor.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="actor.surname" var="surname" />
	<display:column property="surname" title="${surname}" />
	
	<spring:message code="actor.email" var="email" />
	<display:column property="email" title="${email}" />
	
	<spring:message code="text.infoH" var="infoH" />
	<display:column title="${infoH}">
		<acme:button url="member/show.do?memberId=${row.id}" code="button.more" />
	</display:column>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
		<spring:message code="member.remove" var="removeH" />
		<display:column title="${removeH}">
			<acme:button url="member/brotherhood/remove.do?memberId=${row.id}" code="button.remove" />
		</display:column>
	</security:authorize>
		
</display:table>

<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:button url="enrolment/brotherhood/list.do" code="brotherhood.enroll" />
</security:authorize>
