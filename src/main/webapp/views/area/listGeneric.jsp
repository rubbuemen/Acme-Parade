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


<jstl:choose>
<jstl:when test="${area == null}">
		<spring:message code="area.noArea" />
</jstl:when>

<jstl:otherwise>
<display:table pagesize="5" class="displaytag" name="area" requestURI="${requestURI}" id="row">

	<spring:message code="area.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="area.pictures" var="pictures" />
	<display:column title="${pictures}" >
	<jstl:forEach items="${row.pictures}" var="picture">
		<img src="<jstl:out value="${picture}"/>" width="200px" height="200px" />
	</jstl:forEach>
	</display:column>
	
	<spring:message code="area.brotherhoods" var="brotherhoods" />
	<display:column title="${brotherhoods}">
		<acme:button url="brotherhood/listGeneric.do?areaId=${row.id}" code="button.show" />
	</display:column>
	
</display:table>
</jstl:otherwise>
</jstl:choose>

<acme:button url="chapter/list.do" code="button.back" />

