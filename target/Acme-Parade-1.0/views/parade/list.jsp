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

<display:table pagesize="5" class="displaytag" name="parades" requestURI="${requestURI}" id="row">

	<security:authorize access="!hasRole('MEMBER')">
		<jstl:choose>
		  <jstl:when test="${row.status eq 'SUBMITTED'}">
		  	<jstl:set var="color" value="grey"/>
		  </jstl:when>
		  <jstl:when test="${row.status eq 'ACCEPTED'}">
		    <jstl:set var="color" value="green"/>
		  </jstl:when>
		  <jstl:when test="${row.status eq 'REJECTED'}">
		    <jstl:set var="color" value="red"/>
		  </jstl:when>
		  <jstl:otherwise>
		    <jstl:set var="color" value="inherit"/>
		  </jstl:otherwise>
		</jstl:choose>
	</security:authorize>

	<spring:message code="parade.ticker" var="ticker" />
	<display:column property="ticker" title="${ticker}" style="background-color: ${color};" />
	
	<spring:message code="parade.title" var="title" />
	<display:column property="title" title="${title}" style="background-color: ${color};"  />
	
	<spring:message code="parade.description" var="description" />
	<display:column property="description" title="${description}" style="background-color: ${color};" />
	
	<spring:message code="parade.momentOrganise" var="momentOrganise" />
	<display:column title="${momentOrganise}" style="background-color: ${color};" >
			<fmt:formatDate var="format" value="${row.momentOrganise}" pattern="dd/MM/YYYY" />
			<jstl:out value="${format}" />
	</display:column>
	
	<security:authorize access="hasAnyRole('BROTHERHOOD', 'CHAPTER')">
		<spring:message code="parade.status" var="status" />
		<display:column property="status" title="${status}" style="background-color: ${color};" />
		
		<spring:message code="parade.rejectReason" var="rejectReason" />
		<display:column property="rejectReason" title="${rejectReason}" style="background-color: ${color};" />
	</security:authorize>
	
	<security:authorize access="hasRole('CHAPTER')">
		<spring:message code="parade.decideParade" var="decideParade" />
		<display:column title="${decideParade}" style="background-color: ${color};" >
			<jstl:if test="${row.status eq 'SUBMITTED'}">
				<acme:button url="parade/chapter/edit.do?paradeId=${row.id}&decision=ACCEPTED" code="button.accept" />
				<acme:button url="parade/chapter/edit.do?paradeId=${row.id}&decision=REJECTED" code="button.reject" />
			</jstl:if>	
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
		<spring:message code="parade.maxRows" var="maxRows" />
		<display:column property="maxRows" title="${maxRows}" style="background-color: ${color};" />
		
		<spring:message code="parade.maxColumns" var="maxColumns" />
		<display:column property="maxColumns" title="${maxColumns}" style="background-color: ${color};" />
	
		<spring:message code="parade.edit" var="editH" />
		<display:column title="${editH}" style="background-color: ${color};" >
			<jstl:if test="${!row.isFinalMode}">
				<acme:button url="parade/brotherhood/edit.do?paradeId=${row.id}" code="button.edit" />
			</jstl:if>	
		</display:column>
		
		<spring:message code="parade.delete" var="deleteH" />
		<display:column title="${deleteH}" style="background-color: ${color};" >
			<jstl:if test="${!row.isFinalMode}">
				<acme:button url="parade/brotherhood/delete.do?paradeId=${row.id}" code="button.delete" />
			</jstl:if>	
		</display:column>
		
		<spring:message code="parade.changeFinalMode" var="changeFinalModeH" />
		<display:column title="${changeFinalModeH}" style="background-color: ${color};" >
			<jstl:if test="${!row.isFinalMode}">
				<acme:button url="parade/brotherhood/change.do?paradeId=${row.id}" code="button.change" />
			</jstl:if>
		</display:column>
		
		<spring:message code="parade.copy" var="copyH" />
		<display:column title="${copyH}" style="background-color: ${color};" >
			<acme:button url="parade/brotherhood/copy.do?paradeId=${row.id}" code="button.copy" />
		</display:column>
		
		<spring:message code="parade.requestsMarch" var="requestsMarchH" />
		<display:column title="${requestsMarchH}" style="background-color: ${color};" >
			<jstl:if test="${row.status eq 'ACCEPTED'}">
				<acme:button url="requestMarch/brotherhood/list.do?paradeId=${row.id}" code="button.show" />
			</jstl:if>	
		</display:column>
		
		<spring:message code="parade.segments" var="segmentsH" />
		<display:column title="${segmentsH}" style="background-color: ${color};" >
			<acme:button url="segment/brotherhood/list.do?paradeId=${row.id}" code="button.show" />
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('MEMBER')">
		<spring:message code="parade.requestsMarch" var="requestsMarchH" />
		<display:column title="${requestsMarchH}" style="background-color: ${color};" >
				<acme:button url="requestMarch/member/list.do?paradeId=${row.id}" code="button.show" />
		</display:column>
	</security:authorize>
	
	<spring:message code="parade.sponsorship" var="sponsorship" />
	<display:column title="${sponsorship}" style="background-color: ${color};" >
		<jstl:if test="${randomSponsorship.containsKey(row)}">
			<jstl:set var="banner" value="${randomSponsorship.get(row).banner}"/>
			<img src="<jstl:out value='${banner}'/>" width="200px" height="100px" />
		</jstl:if>
	</display:column>
			
</display:table>

<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:button url="parade/brotherhood/create.do" code="button.create" />
</security:authorize>

<security:authorize access="hasRole('MEMBER')">
	<acme:button url="brotherhood/member/list.do" code="button.back" />
</security:authorize>

<security:authorize access="hasRole('CHAPTER')">
	<acme:button url="brotherhood/chapter/list.do" code="button.back" />
</security:authorize>