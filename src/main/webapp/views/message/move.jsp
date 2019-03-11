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

<display:table pagesize="5" class="displaytag" name="boxes" id="row" requestURI="message/move.do">
	<spring:message code="box.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="message.move" var="move" />
	<display:column title="${move}">
		<acme:button url="message/saveMove.do?boxId=${row.id}&messageId=${messageEntity.id}" code="button.move" />
	</display:column>
	
</display:table>

<acme:cancel url="box/list.do" code="button.cancel" />