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

<form:form action="${actionURL}" modelAttribute="parade">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="parade.title" path="title" placeholder="Lorem Ipsum"/>
	<br />

	<acme:textbox code="parade.description" path="description" placeholder="Lorem Ipsum"/>
	<br />
	
	<acme:textbox code="parade.momentOrganise" path="momentOrganise" placeholder="dd/MM/yyyy HH:mm" type="datetime"  />
	<br />
	
	<acme:textbox code="parade.maxRows" path="maxRows" placeholder="1" type="number" min="1" />
	<br />
	
	<acme:textbox code="parade.maxColumns" path="maxColumns" placeholder="1" type="number"  min="1" />
	<br />
	
	<acme:select items="${floats}" itemLabel="title" code="parade.floats" path="floats" multiple="true" />
	<br />

	<jstl:choose>
		<jstl:when test="${parade.id == 0}">
			<acme:submit name="save" code="button.register" />
		</jstl:when>
		<jstl:otherwise>
			<acme:submit name="save" code="button.save" />
		</jstl:otherwise>
	</jstl:choose>
	<acme:cancel url="parade/brotherhood/list.do" code="button.cancel" />

</form:form>