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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<ul style="list-style-type: disc">
	<spring:message code="message.priority" var="priority" />
	<li><b>${priority}:</b> <jstl:out value="${messageEntity.priority}" /></li>
	
	<jstl:if test="${not empty messageEntity.tags}">
		<spring:message code="message.tags" var="tags" />
		<li><b>${tags}:</b> <jstl:out value="${messageEntity.tags}" /></li>
	</jstl:if>
	
	<spring:message code="message.sender" var="sender" />
	<li><b>${sender}:</b> <jstl:out value="${messageEntity.sender.userAccount.username}" /></li>
	
	<spring:message code="message.recipients" var="recipients" />
	
	<li><b>${recipients}:</b>
	<ul>
	<jstl:forEach items="${messageEntity.recipients}" var = "recipient" >
		<li><jstl:out value="${recipient.userAccount.username}" /></li>
    </jstl:forEach>
	</ul>
	
	<spring:message code="message.subject" var="subject" />
	<li><b>${subject}:</b> <jstl:out value="${messageEntity.subject}" /></li>
	
	<spring:message code="message.body" var="body" />
	<li><b>${body}:</b> <jstl:out value="${messageEntity.body}" /></li>
</ul>

<acme:cancel url="message/list.do?boxId=${box.id}" code="button.cancel" />
<acme:cancel url="message/delete.do?boxId=${box.id}&messageId=${messageEntity.id}" code="button.delete" />