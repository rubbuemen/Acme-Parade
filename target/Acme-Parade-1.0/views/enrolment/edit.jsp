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

<form:form action="${actionURL}" modelAttribute="enrolment">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<jstl:if test="${language eq 'en'}">
		<jstl:set var="name" value="nameEnglish" />
	</jstl:if>
	<jstl:if test="${language eq 'es'}">
		<jstl:set var="name" value="nameSpanish" />
	</jstl:if>

	<acme:select items="${positionsBrotherhood}" itemLabel="${name}" code="enrolment.positionBrotherhood" path="positionBrotherhood"/>
	<br />


	<acme:submit name="save" code="button.register" />
	
	<acme:cancel url="enrolment/brotherhood/list.do" code="button.cancel" />

</form:form>