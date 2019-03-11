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

<display:table pagesize="5" class="displaytag" name="boxes" requestURI="${requestURI}" id="row">
	<spring:message code="box.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="box.showMessages" var="showMessages" />
	<display:column title="${showMessages}">
	<jstl:if test="${not empty row.messages}">
		<acme:button url="message/list.do?boxId=${row.id}" code="button.show" />
	</jstl:if>
	</display:column>
	
	<spring:message code="box.showBoxes" var="showBoxes" />
	<display:column title="${showBoxes}">
	<jstl:if test="${not empty row.childsBox}">
		<acme:button url="box/list.do?parentBoxId=${row.id}" code="button.show" />
	</jstl:if>	
	</display:column>
	
	<spring:message code="box.edit" var="editH" />
	<display:column title="${editH}">
		<jstl:if test="${!row.isSystemBox}">
			<acme:button url="box/edit.do?boxId=${row.id}" code="button.edit" />
		</jstl:if>
	</display:column>
	
	<spring:message code="box.delete" var="deleteH" />
	<display:column title="${deleteH}">
		<jstl:if test="${!row.isSystemBox}">
			<acme:button url="box/delete.do?boxId=${row.id}" code="button.delete" />
		</jstl:if>
	</display:column>
</display:table>

<acme:button url="box/create.do" code="button.createBox" />
<acme:button url="message/create.do" code="button.sendMessage" />
<security:authorize access="hasRole('ADMIN')">
	<acme:button url="message/create.do?broadcast=true" code="button.sendBroadcast" />
</security:authorize>
