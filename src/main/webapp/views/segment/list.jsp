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

<display:table pagesize="5" class="displaytag" name="segments" requestURI="${requestURI}" id="row">

	<spring:message code="segment.origin" var="origin" />
	<display:column title="${origin}">
			<spring:message code="segment.latitude" />: ${row.origin.latitude}<br />
			<spring:message code="segment.longitude" />: ${row.origin.longitude}
	</display:column>
	
	<spring:message code="segment.destination" var="destination" />
	<display:column title="${destination}">
			<spring:message code="segment.latitude" />: ${row.destination.latitude}<br />
			<spring:message code="segment.longitude" />: ${row.destination.longitude}
	</display:column>
	
	<spring:message code="segment.timeReachOrigin" var="timeReachOrigin" />
	<display:column property="timeReachOrigin" title="${timeReachOrigin}" format="{0,date,HH:mm}" />
	
	<spring:message code="segment.timeReachDestination" var="timeReachDestination" />
	<display:column property="timeReachDestination" title="${timeReachDestination}" format="{0,date,HH:mm}" />
	
	<spring:message code="segment.editH" var="editH" />
	<display:column title="${editH}">
		<acme:button url="segment/brotherhood/edit.do?segmentId=${row.id}" code="button.edit" />
	</display:column>
	
	<spring:message code="segment.deleteH" var="deleteH" />
	<display:column title="${deleteH}">
		<acme:button url="segment/brotherhood/delete.do?segmentId=${row.id}" code="button.delete" />
	</display:column>
			
</display:table>

<acme:button url="segment/brotherhood/create.do" code="button.create" />
<acme:button url="parade/brotherhood/list.do" code="button.back" />
