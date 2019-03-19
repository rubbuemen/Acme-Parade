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

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${actionURL}" modelAttribute="segment">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<input type="hidden" name="paradeId" value="${parade.id}">
	
	<jstl:if test="${not empty segments}">
		<jstl:set var="read" value="true"/>
	</jstl:if>
		<fieldset>
		    <legend><spring:message code="segment.origin"/></legend>
		    <acme:textbox code="segment.origin.latitude" path="origin.latitude" placeholder="(-)NN.NNNNNNNNNNNNNNN" value="${originLatitude}" readonly="${read}"/>
			<br />
			
			<acme:textbox code="segment.origin.longitude" path="origin.longitude" placeholder="(-)NN.NNNNNNNNNNNNNNN" value="${originLongitude}" readonly="${read}" />
			<br />
			
			<acme:textbox code="segment.timeReachOrigin" path="timeReachOrigin" placeholder="HH:mm" type="time" value="${timeReachOrigin}" readonly="${read}" />
			<br />
		</fieldset>
	
	<fieldset>
	    <legend><spring:message code="segment.destination"/></legend>
	    <acme:textbox code="segment.destination.latitude" path="destination.latitude" placeholder="(-)NN.NNNNNNNNNNNNNNN"/>
		<br />
		
		<acme:textbox code="segment.destination.longitude" path="destination.longitude" placeholder="(-)NN.NNNNNNNNNNNNNNN"/>
		<br />
		
		<acme:textbox code="segment.timeReachDestination" path="timeReachDestination" placeholder="HH:mm" type="time"  />
		<br />
	</fieldset>	

	<jstl:choose>
		<jstl:when test="${segment.id == 0}">
			<acme:submit name="save" code="button.register" />
		</jstl:when>
		<jstl:otherwise>
			<acme:submit name="save" code="button.save" />
		</jstl:otherwise>
	</jstl:choose>
	<acme:cancel url="segment/brotherhood/list.do?paradeId=${parade.id}" code="button.cancel" />

</form:form>
