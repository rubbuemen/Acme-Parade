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

<display:table pagesize="5" class="displaytag" name="floats" requestURI="${requestURI}" id="row">

	<spring:message code="float.title" var="title" />
	<display:column property="title" title="${title}" />
	
	<spring:message code="float.description" var="description" />
	<display:column property="description" title="${description}" />
	
	<spring:message code="float.pictures" var="pictures" />
	<display:column title="${pictures}" >
	<jstl:forEach items="${row.pictures}" var="picture">
		<img src="<jstl:out value="${picture}"/>" width="200px" height="200px" />
	</jstl:forEach>
	</display:column>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
		<spring:message code="float.edit" var="editH" />
		<display:column title="${editH}">
			<acme:button url="float/brotherhood/edit.do?floatId=${row.id}" code="button.edit" />
		</display:column>
		
		<spring:message code="float.delete" var="deleteH" />
		<display:column title="${deleteH}">
			<acme:button url="float/brotherhood/delete.do?floatId=${row.id}" code="button.delete" />
		</display:column>
	</security:authorize>
			
</display:table>

<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:button url="float/brotherhood/create.do" code="button.create" />
</security:authorize>
