<%--
 * index.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${actionURL}" modelAttribute="sponsorship">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="sponsorship.banner" path="banner" placeholder="http://LoremIpsum.com" type="url" />
	<jstl:if test="${not empty sponsorship.banner}">
		<br />
		<img src="<jstl:out value='${sponsorship.banner}' />" />
		<br />
	</jstl:if>
	<br />
	
	<acme:textbox code="sponsorship.targetURL" path="targetURL" placeholder="http://LoremIpsum.com" type="url" />
	<br />
	
	<acme:select items="${paradesAccepted}" itemLabel="title" code="sponsorship.parade" path="parade"/>
	<br />
		
	<fieldset>
		<legend>
			<spring:message code="sponsorship.creditCard" />
		</legend>
		<acme:textbox code="creditCard.holder" path="creditCard.holder"	placeholder="Lorem Ipsum" />
		<br />

		<acme:selectString items="${creditCardMakes}" itemLabel="creditCard.make" code="creditCard.make" path="creditCard.make"/>
		<br />
		
		<acme:textbox code="creditCard.number" path="creditCard.number"	placeholder="NNNNNNNNNNNNNNNN" />
		<br />
		
		<acme:textbox code="creditCard.expirationMonth" path="creditCard.expirationMonth" placeholder="MM" type="number" min="1" max="12" />
		<br />
		
		<acme:textbox code="creditCard.expirationYear" path="creditCard.expirationYear" placeholder="YY" type="number" min="0" max="99" />
		<br />
		
		<acme:textbox code="creditCard.cvv" path="creditCard.cvv" placeholder="NNN" type="number" min="100" max="999" />
		<br />
	</fieldset>

	<jstl:choose>
		<jstl:when test="${sponsorship.id == 0}">
			<acme:submit name="save" code="button.register" />
		</jstl:when>
		<jstl:otherwise>
			<acme:submit name="save" code="button.save" />
		</jstl:otherwise>
	</jstl:choose>
	<acme:cancel url="sponsorship/sponsor/list.do" code="button.cancel" />

</form:form>
