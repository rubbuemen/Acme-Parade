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

<display:table pagesize="5" class="displaytag" name="sponsorships" requestURI="${requestURI}" id="row">

	<spring:message code="sponsorship.banner" var="banner" />
	<display:column title="${banner}" >
		<img src="<jstl:out value="${row.banner}"/>" width="300px" height="100px" />
	</display:column>
	
	<spring:message code="sponsorship.targetURL" var="targetURL" />
	<display:column title="${targetURL}" >
		<a href="<jstl:out value="${row.targetURL}"/>">${row.targetURL}</a>
	</display:column>
	
	<spring:message code="sponsorship.isActivated" var="isActivated" />
	<display:column title="${isActivated}">
		<jstl:if test="${row.isActivated == true}">
			<spring:message code="actor.yes" var="yes"/>
			<jstl:out value="${yes}" />
		</jstl:if>
		<jstl:if test="${row.isActivated == false}">
		<spring:message code="actor.no" var="no"/>
			<jstl:out value="${no}" />
		</jstl:if>
	</display:column>
	
	<spring:message code="sponsorship.creditCard" var="creditCard" />
	<display:column title="${creditCard}">
			<spring:message code="creditCard.holder" />: ${row.creditCard.holder}<br />
			<spring:message code="creditCard.make" />: ${row.creditCard.make}<br />
			<spring:message code="creditCard.number" />: ${row.creditCard.number}<br />
			<spring:message code="creditCard.expirationMonth" />: ${row.creditCard.expirationMonth}<br />
			<spring:message code="creditCard.expirationYear" />: ${row.creditCard.expirationYear}<br />
			<spring:message code="creditCard.cvv" />: ${row.creditCard.cvv}
	</display:column>
	
	<spring:message code="sponsorship.parade" var="parade" />
	<display:column property="parade.title" title="${parade}" />
	
	<spring:message code="sponsorship.edit" var="editH" />
	<display:column title="${editH}" >
		<acme:button url="sponsorship/sponsor/edit.do?sponsorshipId=${row.id}" code="button.edit" />
	</display:column>
	
	<spring:message code="sponsorship.deactivate" var="deactivateH" />
	<display:column title="${deactivateH}" >
		<jstl:if test="${row.isActivated}">
			<acme:button url="sponsorship/sponsor/deactivate.do?sponsorshipId=${row.id}" code="button.deactivate" />
		</jstl:if>	
	</display:column>
	
	<spring:message code="sponsorship.activate" var="activateH" />
	<display:column title="${activateH}" >
		<jstl:if test="${!row.isActivated}">
			<acme:button url="sponsorship/sponsor/activate.do?sponsorshipId=${row.id}" code="button.activate" />
		</jstl:if>	
	</display:column>
		
</display:table>

<acme:button url="sponsorship/sponsor/create.do" code="button.create" />
