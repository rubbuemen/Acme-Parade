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

<display:table pagesize="5" class="displaytag" name="processions" requestURI="${requestURI}" id="row">

	<spring:message code="procession.ticker" var="ticker" />
	<display:column property="ticker" title="${ticker}" />
	
	<spring:message code="procession.title" var="title" />
	<display:column property="title" title="${title}" />
	
	<spring:message code="procession.description" var="description" />
	<display:column property="description" title="${description}" />
	
	<spring:message code="procession.momentOrganise" var="momentOrganise" />
	<display:column title="${momentOrganise}">
			<fmt:formatDate var="format" value="${row.momentOrganise}" pattern="dd/MM/YYYY HH:mm" />
			<jstl:out value="${format}" />
	</display:column>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
		<spring:message code="procession.maxRows" var="maxRows" />
		<display:column property="maxRows" title="${maxRows}" />
		
		<spring:message code="procession.maxColumns" var="maxColumns" />
		<display:column property="maxColumns" title="${maxColumns}" />
	
		<spring:message code="procession.edit" var="editH" />
		<display:column title="${editH}">
			<jstl:if test="${!row.isFinalMode}">
				<acme:button url="procession/brotherhood/edit.do?processionId=${row.id}" code="button.edit" />
			</jstl:if>	
		</display:column>
		
		<spring:message code="procession.delete" var="deleteH" />
		<display:column title="${deleteH}">
			<jstl:if test="${!row.isFinalMode}">
				<acme:button url="procession/brotherhood/delete.do?processionId=${row.id}" code="button.delete" />
			</jstl:if>	
		</display:column>
		
		<spring:message code="procession.changeFinalMode" var="changeFinalModeH" />
		<display:column title="${changeFinalModeH}" >
			<jstl:if test="${!row.isFinalMode}">
				<acme:button url="procession/brotherhood/change.do?processionId=${row.id}" code="button.change" />
			</jstl:if>
		</display:column>
		
		<spring:message code="procession.requestsMarch" var="requestsMarchH" />
		<display:column title="${requestsMarchH}">
			<jstl:if test="${row.isFinalMode}">
				<acme:button url="requestMarch/brotherhood/list.do?processionId=${row.id}" code="button.show" />
			</jstl:if>	
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('MEMBER')">
		<spring:message code="procession.requestsMarch" var="requestsMarchH" />
		<display:column title="${requestsMarchH}">
				<acme:button url="requestMarch/member/list.do?processionId=${row.id}" code="button.show" />
		</display:column>
	
	</security:authorize>
			
</display:table>

<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:button url="procession/brotherhood/create.do" code="button.create" />
</security:authorize>

<security:authorize access="hasRole('MEMBER')">
	<acme:button url="brotherhood/member/list.do" code="button.back" />
</security:authorize>