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

<form:form action="${actionURL}" modelAttribute="box">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<input type="hidden" name="boxParentOld" value="${boxParentOld}" />

	<acme:textbox code="box.name" path="name" placeholder="Lorem Ipsum"/>
	<br />
	
	<acme:select items="${boxes}" itemLabel="name" code="box.parentBox" path="parentBox"/>
	<br />

	<jstl:choose>
		<jstl:when test="${box.id == 0}">
			<acme:submit name="save" code="button.create" />
		</jstl:when>
		<jstl:otherwise>
			<acme:submit name="save" code="button.save" />
		</jstl:otherwise>
	</jstl:choose>
	<acme:cancel url="box/list.do" code="button.cancel" />

</form:form>
 
