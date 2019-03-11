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

<security:authorize access="hasRole('MEMBER')">
	<h3><spring:message code="brotherhood.brotherhoodsBelongs" /></h3>
</security:authorize>
<display:table pagesize="5" class="displaytag" name="brotherhoods" requestURI="${requestURI}" id="row">
	
	<spring:message code="brotherhood.titleH" var="titleH" />
	<display:column property="title" title="${titleH}" />
	
	<spring:message code="brotherhood.establishmentDateH" var="establishmentDateH" />
	<display:column title="${establishmentDateH}">
			<fmt:formatDate var="format" value="${row.establishmentDate}" pattern="dd/MM/YYYY" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="brotherhood.commentsH" var="commentsH" />
	<display:column property="comments" title="${commentsH}" />
	
	<spring:message code="brotherhood.paradesH" var="paradesH" />
	<display:column title="${paradesH}">
			<acme:button url="parade/member/list.do?brotherhoodId=${row.id}" code="button.show" />
	</display:column>
	
	<spring:message code="member.remove" var="removeH" />
	<display:column title="${removeH}">
		<acme:button url="brotherhood/member/remove.do?brotherhoodId=${row.id}" code="button.remove" />
	</display:column>
</display:table>

<security:authorize access="hasRole('MEMBER')">
<h3><spring:message code="brotherhood.brotherhoodsNotBelongs" /></h3>
<display:table pagesize="5" class="displaytag" name="brotherhoodsNotBelongs" requestURI="${requestURI}" id="row">
	
	<spring:message code="brotherhood.titleH" var="titleH" />
	<display:column property="title" title="${titleH}" />
	
	<spring:message code="brotherhood.establishmentDateH" var="establishmentDateH" />
	<display:column title="${establishmentDateH}">
			<fmt:formatDate var="format" value="${row.establishmentDate}" pattern="dd/MM/YYYY" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="brotherhood.commentsH" var="commentsH" />
	<display:column property="comments" title="${commentsH}" />
	
	<spring:message code="text.enrolmentsH" var="enrolmentsH" />
		<display:column title="${enrolmentsH}">
			<acme:button url="enrolment/member/list.do?brotherhoodId=${row.id}" code="button.show" />
		</display:column>
	
</display:table>
</security:authorize>
