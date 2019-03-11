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
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<display:table pagesize="5" class="displaytag" name="areas" requestURI="${requestURI}" id="row">

	<spring:message code="area.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="area.pictures" var="pictures" />
	<display:column title="${pictures}" >
	<jstl:forEach items="${row.pictures}" var="picture">
		<img src="<jstl:out value="${picture}"/>" width="200px" height="200px" />
	</jstl:forEach>
	</display:column>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
		<spring:message code="area.select" var="selectH" />
		<display:column title="${selectH}">
			<acme:button url="area/brotherhood/edit.do?areaId=${row.id}" code="button.select" />
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="area.edit" var="editH" />
		<display:column title="${editH}">
			<acme:button url="area/administrator/edit.do?areaId=${row.id}" code="button.edit" />
		</display:column>
		
		<spring:message code="area.delete" var="deleteH" />
		<jstl:set var = "areasUsed" value = "${areasUsed}"/>
		<jstl:set var = "area" value = "${row}"/>
		<display:column title="${deleteH}">
			<jstl:if test="${!fn:contains(areasUsed, area)}">
				<acme:button url="area/administrator/delete.do?areaId=${row.id}" code="button.delete" />
			</jstl:if>
	</display:column>
	</security:authorize>
			
</display:table>

<security:authorize access="hasRole('ADMIN')">
	<acme:button url="area/administrator/create.do" code="button.create" />
</security:authorize>
