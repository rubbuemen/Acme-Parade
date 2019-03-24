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

<h3><spring:message code="actor.actorsToBan" /></h3>
<display:table pagesize="5" class="displaytag" name="actorsToBan" id="row">
	
	<spring:message code="actor.name" var="nameH" />
	<display:column property="name" title="${nameH}" />
	
	<spring:message code="actor.surname" var="surnameH" />
	<display:column property="surname" title="${surnameH}" />
	
	<spring:message code="actor.email" var="emailH" />
	<display:column property="email" title="${emailH}" />
	
	<spring:message code="actor.username" var="usernameH" />
	<display:column property="userAccount.username" title="${usernameH}" />
	
	<spring:message code="actor.isSpammer" var="isSpammerH" />
	<display:column title="${isSpammerH}">
		<jstl:if test="${row.isSpammer == true}">
			<spring:message code="actor.yes" var="yes"/>
			<jstl:out value="${yes}" />
		</jstl:if>
		<jstl:if test="${row.isSpammer == false}">
		<spring:message code="actor.no" var="no"/>
			<jstl:out value="${no}" />
		</jstl:if>
	</display:column>
	
	<spring:message code="actor.polarityScore" var="polarityScoreH" />
	<display:column property="polarityScore" title="${polarityScoreH}" />
	
	<spring:message code="actor.ban" var="ban" />
	<display:column title="${ban}">
			<acme:button url="systemConfiguration/administrator/ban.do?actorId=${row.id}" code="button.ban" />
	</display:column>
</display:table>

<h3><spring:message code="actor.actorsBanned" /></h3>
<display:table pagesize="5" class="displaytag" name="actorsBanned" id="row">
	
	<spring:message code="actor.name" var="nameH" />
	<display:column property="name" title="${nameH}" />
	
	<spring:message code="actor.surname" var="surnameH" />
	<display:column property="surname" title="${surnameH}" />
	
	<spring:message code="actor.email" var="emailH" />
	<display:column property="email" title="${emailH}" />
	
	<spring:message code="actor.username" var="usernameH" />
	<display:column property="userAccount.username" title="${usernameH}" />
	
	<spring:message code="actor.isSpammer" var="isSpammerH" />
	<display:column title="${isSpammerH}">
		<jstl:if test="${row.isSpammer == true}">
			<spring:message code="actor.yes" var="yes"/>
			<jstl:out value="${yes}" />
		</jstl:if>
		<jstl:if test="${row.isSpammer == false}">
		<spring:message code="actor.no" var="no"/>
			<jstl:out value="${no}" />
		</jstl:if>
	</display:column>
	
	<spring:message code="actor.polarityScore" var="polarityScoreH" />
	<display:column property="polarityScore" title="${polarityScoreH}" />
	
	<spring:message code="actor.ban" var="ban" />
	<display:column title="${ban}">
			<acme:button url="systemConfiguration/administrator/unban.do?actorId=${row.id}" code="button.unban" />
	</display:column>
</display:table>
