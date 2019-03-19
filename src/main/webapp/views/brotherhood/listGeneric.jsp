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
			<acme:button url="parade/listGeneric.do?brotherhoodId=${row.id}&areaId=${areaId}" code="button.show" />
	</display:column>
	
	<jstl:if test="${chapter == null}">
		<spring:message code="brotherhood.membersH" var="membersH" />
		<display:column title="${membersH}">
			<acme:button url="member/listGeneric.do?brotherhoodId=${row.id}" code="button.show" />
		</display:column>
				
		<spring:message code="brotherhood.floatsH" var="floatsH" />
		<display:column title="${floatsH}">
			<acme:button url="float/listGeneric.do?brotherhoodId=${row.id}" code="button.show" />
		</display:column>
		
		<spring:message code="brotherhood.historyH" var="historyH" />
		<display:column title="${historyH}">
			<acme:button url="history/list.do?brotherhoodId=${row.id}" code="button.show" />
		</display:column>
		
		<spring:message code="text.infoH" var="infoH" />
		<display:column title="${infoH}">
			<acme:button url="brotherhood/show.do?brotherhoodId=${row.id}" code="button.more" />
		</display:column>
		
		<security:authorize access="hasRole('MEMBER')">
			<spring:message code="text.enrollH" var="enrollH" />
			<jstl:set var = "brothers" value = "${brotherhoodsMemberLogged}"/>
			<jstl:set var = "brother" value = "${row}"/>
			<display:column title="${enrollH}">
				<jstl:if test="${!fn:contains(brotherhoodsMemberLogged, brother)}">
					<acme:button url="enrolment/member/enroll.do?brotherhoodId=${row.id}" code="button.enroll" />
				</jstl:if>
			</display:column>
		</security:authorize>
	</jstl:if>

</display:table>

<jstl:if test="${chapter != null}">
	<acme:button url="area/listGeneric.do?chapterId=${chapter.id}" code="button.back" />
</jstl:if>
