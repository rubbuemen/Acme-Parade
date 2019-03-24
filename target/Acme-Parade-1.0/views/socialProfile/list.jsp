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

<display:table pagesize="5" class="displaytag" name="socialProfiles" requestURI="${requestURI}" id="row">

	<spring:message code="socialProfile.nick" var="nick" />
	<display:column property="nick" title="${nick}" />
	
	<spring:message code="socialProfile.socialNetworkName" var="socialNetworkName" />
	<display:column property="socialNetworkName" title="${socialNetworkName}" />
	
	<spring:message code="socialProfile.profileLink" var="profileLink" />
	<display:column title="${profileLink}" >
		<a href="<jstl:out value="${row.profileLink}"/>">${row.profileLink}</a>
	</display:column>
	
	<spring:message code="socialProfile.edit" var="editH" />
	<display:column title="${editH}">
		<acme:button url="socialProfile/edit.do?socialProfileId=${row.id}" code="button.edit" />
	</display:column>
		
	<spring:message code="socialProfile.delete" var="deleteH" />
	<display:column title="${deleteH}">
		<acme:button url="socialProfile/delete.do?socialProfileId=${row.id}" code="button.delete" />
	</display:column>
			
</display:table>

<acme:button url="socialProfile/create.do" code="button.create" />
