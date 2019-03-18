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

<jstl:choose>
<jstl:when test="${history != null}">
<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="history.inceptionRecord"/></summary>

<display:table class="displaytag" name="inceptionRecord" id="row1">
	<spring:message code="inceptionRecord.title" var="titleH" />
	<display:column property="title" title="${titleH}" />
	
	<spring:message code="inceptionRecord.description" var="descriptionH" />
	<display:column property="description" title="${descriptionH}" />
	
	<spring:message code="inceptionRecord.photos" var="photosH" />
	<display:column title="${photosH}" >
	<jstl:forEach items="${row1.photos}" var="photo">
		<img src="<jstl:out value="${photo}"/>" width="200px" height="200px" />
	</jstl:forEach>
	</display:column>
	<jstl:if test="${authority == 'BROTHERHOOD'}">
		<spring:message code="inceptionRecord.edit" var="editH" />
		<display:column title="${editH}">
			<acme:button url="inceptionRecord/brotherhood/edit.do?inceptionRecordId=${row1.id}" code="button.edit" />
		</display:column>
	</jstl:if>
</display:table>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="history.periodRecords"/></summary>

<display:table class="displaytag" name="periodRecords" id="row2">
	<spring:message code="periodRecord.title" var="titleH" />
	<display:column property="title" title="${titleH}" />
	
	<spring:message code="periodRecord.description" var="descriptionH" />
	<display:column property="description" title="${descriptionH}" />
	
	<spring:message code="periodRecord.startYear" var="startYearH" />
	<display:column property="startYear" title="${startYearH}" />
	
	<spring:message code="periodRecord.endYear" var="endYearH" />
	<display:column property="endYear" title="${endYearH}" />
	
	<spring:message code="periodRecord.photos" var="photosH" />
	<display:column title="${photosH}" >
	<jstl:forEach items="${row2.photos}" var="photo">
		<img src="<jstl:out value="${photo}"/>" width="200px" height="200px" />
	</jstl:forEach>
	</display:column>
	
	<jstl:if test="${authority == 'BROTHERHOOD'}">
		<spring:message code="periodRecord.edit" var="editH" />
		<display:column title="${editH}">
			<acme:button url="periodRecord/brotherhood/edit.do?periodRecordId=${row2.id}" code="button.edit" />
		</display:column>
		
		<spring:message code="periodRecord.delete" var="deleteH" />
		<display:column title="${deleteH}">
				<acme:button url="periodRecord/brotherhood/delete.do?periodRecordId=${row2.id}" code="button.delete" />
		</display:column>
	</jstl:if>
</display:table>

<jstl:if test="${authority == 'BROTHERHOOD'}">
	<acme:button url="periodRecord/brotherhood/create.do" code="button.create" />
</jstl:if>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="history.legalRecords"/></summary>

<display:table class="displaytag" name="legalRecords" id="row3">
	<spring:message code="legalRecord.title" var="titleH" />
	<display:column property="title" title="${titleH}" />
	
	<spring:message code="legalRecord.description" var="descriptionH" />
	<display:column property="description" title="${descriptionH}" />
	
	<spring:message code="legalRecord.legalName" var="legalNameH" />
	<display:column property="legalName" title="${legalNameH}" />
	
	<spring:message code="legalRecord.VATNumber" var="VATNumberH" />
	<display:column property="VATNumber" title="${VATNumberH}" />
	
	<spring:message code="legalRecord.applicableLaws" var="applicableLawsH" />
	<display:column title="${applicableLawsH}" >
	<ul>
	<jstl:forEach items="${row3.applicableLaws}" var="applicableLaw">
		<li><jstl:out value="${applicableLaw}"/></li>
	</jstl:forEach>
	</ul>
	</display:column>
	
	<jstl:if test="${authority == 'BROTHERHOOD'}">
		<spring:message code="legalRecord.edit" var="editH" />
		<display:column title="${editH}">
			<acme:button url="legalRecord/brotherhood/edit.do?legalRecordId=${row3.id}" code="button.edit" />
		</display:column>
		
		<spring:message code="legalRecord.delete" var="deleteH" />
		<display:column title="${deleteH}">
				<acme:button url="legalRecord/brotherhood/delete.do?legalRecordId=${row3.id}" code="button.delete" />
		</display:column>
	</jstl:if>
</display:table>

<jstl:if test="${authority == 'BROTHERHOOD'}">
	<acme:button url="legalRecord/brotherhood/create.do" code="button.create" />
</jstl:if>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="history.linkRecords"/></summary>

<display:table class="displaytag" name="linkRecords" id="row4">
	<spring:message code="linkRecord.title" var="titleH" />
	<display:column property="title" title="${titleH}" />
	
	<spring:message code="linkRecord.description" var="descriptionH" />
	<display:column property="description" title="${descriptionH}" />
	
	<spring:message code="linkRecord.brotherhood" var="brotherhoodH" />
	<jstl:choose>
	<jstl:when test="${row4.brotherhood != null}">
		<display:column title="${brotherhoodH}" >
			${row4.brotherhood.title}: <acme:button url="brotherhood/show.do?brotherhoodId=${row4.brotherhood.id}" code="button.show" />
		</display:column>
	</jstl:when>
	<jstl:otherwise>
		<display:column title="${brotherhoodH}" >
			<spring:message code="linkRecord.noOneBrotherhood" />
		</display:column>
	</jstl:otherwise>
	</jstl:choose>
	
	<jstl:if test="${authority == 'BROTHERHOOD'}">
		<spring:message code="linkRecord.edit" var="editH" />
		<display:column title="${editH}">
			<acme:button url="linkRecord/brotherhood/edit.do?linkRecordId=${row4.id}" code="button.edit" />
		</display:column>
		
		<spring:message code="linkRecord.delete" var="deleteH" />
		<display:column title="${deleteH}">
				<acme:button url="linkRecord/brotherhood/delete.do?linkRecordId=${row4.id}" code="button.delete" />
		</display:column>
	</jstl:if>
</display:table>

<jstl:if test="${authority == 'BROTHERHOOD'}">
	<acme:button url="linkRecord/brotherhood/create.do" code="button.create" />
</jstl:if>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="history.miscellaneousRecords"/></summary>

<display:table class="displaytag" name="miscellaneousRecords" id="row5">
	<spring:message code="miscellaneousRecord.title" var="titleH" />
	<display:column property="title" title="${titleH}" />
	
	<spring:message code="miscellaneousRecord.description" var="descriptionH" />
	<display:column property="description" title="${descriptionH}" />
	
	<jstl:if test="${authority == 'BROTHERHOOD'}">
		<spring:message code="miscellaneousRecord.edit" var="editH" />
		<display:column title="${editH}">
			<acme:button url="miscellaneousRecord/brotherhood/edit.do?miscellaneousRecordId=${row5.id}" code="button.edit" />
		</display:column>
		
		<spring:message code="miscellaneousRecord.delete" var="deleteH" />
		<display:column title="${deleteH}">
				<acme:button url="miscellaneousRecord/brotherhood/delete.do?miscellaneousRecordId=${row5.id}" code="button.delete" />
		</display:column>
	</jstl:if>
</display:table>

	
<jstl:if test="${authority == 'BROTHERHOOD'}">
	<acme:button url="miscellaneousRecord/brotherhood/create.do" code="button.create" />
</jstl:if>

</details><br/>
</jstl:when>

<jstl:otherwise>
	<spring:message code="history.notExist"/>
	<jstl:if test="${authority == 'BROTHERHOOD'}">
		<acme:button url="history/brotherhood/create.do" code="button.create" />
	</jstl:if>
</jstl:otherwise>
</jstl:choose>


