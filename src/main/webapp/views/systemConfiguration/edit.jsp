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

<form:form id="form" action="${actionURL}" modelAttribute="systemConfiguration">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="systemConfiguration.nameSystem" path="nameSystem" placeholder="Lorem Ipsum" />
	<br />
	
	<acme:textbox code="systemConfiguration.bannerUrl" path="bannerUrl" placeholder="http://LoremIpsum.com" type="url" />
	<br />
	<jstl:if test="${not empty bannerUrl}">
		<img src="<jstl:out value='${bannerUrl}' />" />
		<br /><br />
	</jstl:if>
	
	<acme:textarea code="systemConfiguration.welcomeMessageEnglish" path="welcomeMessageEnglish" placeholder="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean at auctor massa" />
	<br />
	
	<acme:textarea code="systemConfiguration.welcomeMessageSpanish" path="welcomeMessageSpanish" placeholder="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean at auctor massa" />
	<br />
	
	<acme:textbox code="systemConfiguration.phoneCountryCode" path="phoneCountryCode" placeholder="+ddd" />
	<br />
	
	<acme:textbox code="systemConfiguration.periodFinder" path="periodFinder" placeholder="1" type="number" min="1" max="24" />
	<br />
	
	<acme:textbox code="systemConfiguration.maxResultsFinder" path="maxResultsFinder" placeholder="1" type="number" min="1" max="100" />
	<br />
	
	<acme:textarea code="systemConfiguration.prioritiesMessagesList" path="prioritiesMessagesList" placeholder="Lorem ipsum, Lorem ipsum, Lorem ipsum, Lorem ipsum" />
	<br />
	
	<acme:textarea code="systemConfiguration.positiveWords" path="positiveWords" placeholder="Lorem ipsum, Lorem ipsum, Lorem ipsum, Lorem ipsum" />
	<br />
	
	<acme:textarea code="systemConfiguration.negativeWords" path="negativeWords" placeholder="Lorem ipsum, Lorem ipsum, Lorem ipsum, Lorem ipsum" />
	<br />
	
	<acme:textarea code="systemConfiguration.spamWords" path="spamWords" placeholder="Lorem ipsum, Lorem ipsum, Lorem ipsum, Lorem ipsum" />
	<br />

	<acme:submit name="save" code="button.save" />
	<acme:cancel url="systemConfiguration/administrator/show.do" code="button.cancel" />

</form:form>
