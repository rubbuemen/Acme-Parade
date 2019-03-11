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

<form:form action="${actionURL}" modelAttribute="finder">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="finder.keyWord" path="keyWord" placeholder="Lorem Ipsum" />
	<br />

	<acme:textbox code="finder.minDate" path="minDate" placeholder="dd/MM/yyyy" type="date" />
	<br />
	
	<acme:textbox code="finder.maxDate" path="maxDate" placeholder="dd/MM/yyyy" type="date" />
	<br />
	
	<acme:select items="${areas}" itemLabel="name" code="finder.area" path="area"/>
	<br />
	
	<acme:submit name="save" code="button.search" />
	<acme:button url="finder/member/clear.do" code="button.clear" />

</form:form>


<display:table pagesize="5" class="displaytag" name="parades" requestURI="${requestURI}" id="row">

	<spring:message code="parade.ticker" var="ticker" />
	<display:column property="ticker" title="${ticker}" />
	
	<spring:message code="parade.title" var="title" />
	<display:column property="title" title="${title}" />
	
	<spring:message code="parade.description" var="description" />
	<display:column property="description" title="${description}" />
	
	<spring:message code="parade.momentOrganise" var="momentOrganise" />
	<display:column title="${momentOrganise}">
			<fmt:formatDate var="format" value="${row.momentOrganise}" pattern="dd/MM/YYYY HH:mm" />
			<jstl:out value="${format}" />
	</display:column>
			
</display:table>