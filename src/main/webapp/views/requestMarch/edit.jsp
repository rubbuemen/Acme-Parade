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

<form:form action="${actionURL}" modelAttribute="requestMarch">
<div>
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="status" value="${decision}"/>
	
	<jstl:if test="${decision eq 'APPROVED'}">
		<h3><spring:message code="requestMarch.infoNeedSelect" /></h3>
			
		<acme:textbox code="requestMarch.positionRow" path="positionRow" placeholder="1" type="number" min="1" />
		(<spring:message code="requestMarch.suggested" />: ${rowSuggested})
		<br /><br />
		
		<acme:textbox code="requestMarch.positionColumn" path="positionColumn" placeholder="1" type="number" min="1" />
		(<spring:message code="requestMarch.suggested" />: ${columnSuggested})
		<br /><br />

		
	</jstl:if>
	
	<jstl:if test="${decision eq 'REJECTED'}">
		<h3><spring:message code="requestMarch.infoNeedExplain" /></h3>
		
		<acme:textarea code="requestMarch.rejectReason" placeholder="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean at auctor massa" path="rejectReason" />
		<br />
	</jstl:if>
	
	


	<acme:submit name="save" code="button.register" />
	
	<acme:cancel url="parade/brotherhood/list.do" code="button.cancel" />
</div>
</form:form>
