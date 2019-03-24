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

<display:table pagesize="5" class="displaytag" name="chapters" requestURI="${requestURI}" id="row">

	<spring:message code="chapter.title" var="title" />
	<display:column property="title" title="${title}" />
	
	<spring:message code="actor.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="actor.surname" var="surname" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="actor.email" var="email" />
	<display:column property="email" title="${email}" />
	
	<spring:message code="text.infoH" var="infoH" />
	<display:column title="${infoH}">
		<acme:button url="chapter/show.do?chapterId=${row.id}" code="button.more" />
	</display:column>
	
	<spring:message code="text.areasH" var="areasH" />
	<display:column title="${areasH}">
		<acme:button url="area/listGeneric.do?chapterId=${row.id}" code="button.show" />
	</display:column>
	
			
</display:table>
