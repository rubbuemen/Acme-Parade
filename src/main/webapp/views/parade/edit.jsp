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
	<security:authorize access="hasRole('CHAPTER')">
		<form:hidden path="status" value="${decision}"/>
	</security:authorize>

	<security:authorize access="hasRole('BROTHERHOOD')">
		<acme:textbox code="parade.title" path="title" placeholder="Lorem Ipsum"/>
		<br />
	
		<acme:textbox code="parade.description" path="description" placeholder="Lorem Ipsum"/>
		<br />
		
		<acme:textbox code="parade.momentOrganise" path="momentOrganise" placeholder="dd/MM/yyyy"  />
		<br />
		
		<acme:textbox code="parade.maxRows" path="maxRows" placeholder="1" type="number" min="1" />
		<br />
		
		<acme:textbox code="parade.maxColumns" path="maxColumns" placeholder="1" type="number"  min="1" />
		<br />
		
		<acme:select items="${floats}" itemLabel="title" code="parade.floats" path="floats" multiple="true" />
		<br />
	</security:authorize>
	<security:authorize access="hasRole('CHAPTER')">
		<jstl:if test="${decision eq 'REJECTED'}">
			<h3><spring:message code="parade.infoNeedExplain" /></h3>
			<acme:textarea code="parade.rejectReason" placeholder="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean at auctor massa" path="rejectReason" />
			<br />
		</jstl:if>
	</security:authorize>

	<jstl:choose>
		<jstl:when test="${parade.id == 0}">
			<acme:submit name="save" code="button.register" />
		</jstl:when>
		<jstl:otherwise>
			<acme:submit name="save" code="button.save" />
		</jstl:otherwise>
	</jstl:choose>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
		<acme:cancel url="parade/brotherhood/list.do" code="button.cancel" />
	</security:authorize>
	<security:authorize access="hasRole('CHAPTER')">
		<acme:cancel url="parade/chapter/list.do?brotherhoodId=${brotherhood.id}" code="button.cancel" />
	</security:authorize>

</form:form>