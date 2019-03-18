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

<form:form action="${actionURL}" modelAttribute="history">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<fieldset>
	    <legend><spring:message code="history.inceptionRecord"/></legend>
	    <acme:textbox code="inceptionRecord.title" path="inceptionRecord.title" placeholder="Lorem Ipsum"/>
		<br />
	
		<acme:textbox code="inceptionRecord.description" path="inceptionRecord.description" placeholder="Lorem Ipsum"/>
		<br />
		
		<acme:textarea code="inceptionRecord.photos" path="inceptionRecord.photos" placeholder="http://Loremipsum.com, http://Loremipsum.com, ..." />
		<br />
	</fieldset>
	
	<br /><br />
	<spring:message code="history.createMoreRecords"/>.
	<br /><br />
	
	<acme:submit name="save" code="button.register" />
	<acme:cancel url="history/brotherhood/list.do" code="button.cancel" />

</form:form>
