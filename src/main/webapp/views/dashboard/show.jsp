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


<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC1"/></summary>

<spring:message var="avg" code="dashboard.avg"/>
<spring:message var="min" code="dashboard.min"/>
<spring:message var="max" code="dashboard.max"/>
<spring:message var="stddev" code="dashboard.stddev"/>

<jstl:set var="avgQueryC1" value="${avgQueryC1 == \"null\" ? 0 : avgQueryC1}" />
<jstl:set var="minQueryC1" value="${minQueryC1 == \"null\" ? 0 : minQueryC1}" />
<jstl:set var="maxQueryC1" value="${maxQueryC1 == \"null\" ? 0 : maxQueryC1}" />
<jstl:set var="stddevQueryC1" value="${stddevQueryC1 == \"null\" ? 0 : stddevQueryC1}" />

<spring:message code="dashboard.stadistics" var="stadistics" />

<div class="chartQueryC1" style="width:50%;">
	<canvas id="chartQueryC1"></canvas>
</div>
<script>
var chartQueryC1Id = document.getElementById('chartQueryC1');
var stadistics = "${stadistics}";

var labels = ['${avg}', '${min}', '${max}', '${stddev}'];
var data = ['${avgQueryB1}','${minQueryB1}', '${maxQueryB1}', '${stddevQueryB1}'];

var chartQueryC1 = new Chart(chartQueryC1Id, {
    type: 'bar',
    data: {
        labels: labels,
        datasets: [{
            label: stadistics,
            backgroundColor: 'rgb(221,160,221)',
            borderColor: 'rgb(25, 52, 25)',
            borderWidth: 1,
            data: data,
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }
});
</script>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC2"/></summary>

<display:table class="displaytag" name="queryC2" id="row">
	<spring:message code="brotherhood.titleH" var="titleH" />
	<display:column property="title" title="${titleH}" />
	
	<spring:message code="brotherhood.establishmentDateH" var="establishmentDateH" />
	<display:column title="${establishmentDateH}">
			<fmt:formatDate var="format" value="${row.establishmentDate}" pattern="dd/MM/YYYY" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="brotherhood.commentsH" var="commentsH" />
	<display:column property="comments" title="${commentsH}" />
</display:table>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC3"/></summary>

<display:table class="displaytag" name="queryC3" id="row">
	<spring:message code="brotherhood.titleH" var="titleH" />
	<display:column property="title" title="${titleH}" />
	
	<spring:message code="brotherhood.establishmentDateH" var="establishmentDateH" />
	<display:column title="${establishmentDateH}">
			<fmt:formatDate var="format" value="${row.establishmentDate}" pattern="dd/MM/YYYY" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="brotherhood.commentsH" var="commentsH" />
	<display:column property="comments" title="${commentsH}" />
</display:table>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC4"/></summary>

<display:table class="displaytag" name="queryC4" id="row">
	<spring:message code="brotherhood.titleH" var="titleH" />
	<display:column title="${titleH}">
		<jstl:out value="${row[0]}" />
	</display:column>
	<spring:message code="requestMarch.status" var="statusH" />
	<display:column title="${statusH}">
		<jstl:out value="${row[1]}" />
	</display:column>
	<spring:message code="dashboard.ratio" var="ratio" />
	<display:column title="${ratio}">
		<jstl:out value="${row[2]}" />
	</display:column>
</display:table>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC5"/></summary>

<display:table class="displaytag" name="queryC5" id="row">
	<spring:message code="parade.ticker" var="ticker" />
	<display:column property="ticker" title="${ticker}" />
	
	<spring:message code="parade.title" var="title" />
	<display:column property="title" title="${title}" />
	
	<spring:message code="parade.description" var="description" />
	<display:column property="description" title="${description}" />
	
	<spring:message code="parade.momentOrganise" var="momentOrganise" />
	<display:column title="${momentOrganise}">
			<fmt:formatDate var="format" value="${row.momentOrganise}" pattern="dd/MM/YYYY HH:mm" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="parade.maxRows" var="maxRows" />
	<display:column property="maxRows" title="${maxRows}" />
	
	<spring:message code="parade.maxColumns" var="maxColumns" />
	<display:column property="maxColumns" title="${maxColumns}" />
	
</display:table>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC6"/></summary>

<display:table class="displaytag" name="queryC6" id="row">
	<spring:message code="requestMarch.status" var="statusH" />
	<display:column title="${statusH}">
		<jstl:out value="${row[0]}" />
	</display:column>
	<spring:message code="dashboard.ratio" var="ratio" />
	<display:column title="${ratio}">
		<jstl:out value="${row[1]}" />
	</display:column>
</display:table>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC7"/></summary>

<display:table class="displaytag" name="queryC7" id="row">
	<<spring:message code="actor.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="actor.surname" var="surname" />
	<display:column property="surname" title="${surname}" />
	
	<spring:message code="actor.email" var="email" />
	<display:column property="email" title="${email}" />
	
	<spring:message code="text.infoH" var="infoH" />
	<display:column title="${infoH}">
		<acme:button url="member/show.do?memberId=${row.id}" code="button.more" />
	</display:column>
	
</display:table>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC8"/></summary>

<spring:message code="dashboard.count" var="count" />
<div class="chartQueryC8" style="width:50%;">
	<canvas id="chartQueryC8"></canvas>
</div>
<script>
var chartQueryC8Id = document.getElementById('chartQueryC8');
var count = "${count}";

var labels = [];
var data = [];
		
<jstl:forEach items="${queryC8}" var="row">
	<jstl:if test="${language eq 'en'}">
		labels.push("${row[0]}");
	</jstl:if>
	<jstl:if test="${language eq 'es'}">
		labels.push("${row[1]}");
	</jstl:if>
	
	var number = parseInt("${row[2]}");
	data.push(number);
</jstl:forEach>

var chartQueryC8 = new Chart(chartQueryC8Id, {
    type: 'bar',
    data: {
        labels: labels,
        datasets: [{
            label: count,
            backgroundColor: 'rgb(221,160,221)',
            borderColor: 'rgb(25, 52, 25)',
            borderWidth: 1,
            data: data,
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }
});
</script>

</details><br/>


<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryB1"/></summary>

<spring:message var="ratio" code="dashboard.ratio"/>
<spring:message var="count" code="dashboard.count"/>
<spring:message var="avg" code="dashboard.avg"/>
<spring:message var="min" code="dashboard.min"/>
<spring:message var="max" code="dashboard.max"/>
<spring:message var="stddev" code="dashboard.stddev"/>

<jstl:set var="ratioQueryB1" value="${ratioQueryB1 == \"null\" ? 0 : ratioQueryB1}" />
<jstl:set var="countQueryB1" value="${countQueryB1 == \"null\" ? 0 : countQueryB1}" />
<jstl:set var="avgQueryB1" value="${avgQueryB1 == \"null\" ? 0 : avgQueryB1}" />
<jstl:set var="minQueryB1" value="${minQueryB1 == \"null\" ? 0 : minQueryB1}" />
<jstl:set var="maxQueryB1" value="${maxQueryB1 == \"null\" ? 0 : maxQueryB1}" />
<jstl:set var="stddevQueryB1" value="${stddevQueryB1 == \"null\" ? 0 : stddevQueryB1}" />

<spring:message code="dashboard.stadistics" var="stadistics" />

<div class="chartQueryB1" style="width:50%;">
	<canvas id="chartQueryB1"></canvas>
</div>
<script>
var chartQueryB1Id = document.getElementById('chartQueryB1');
var stadistics = "${stadistics}";

var labels = ['${ratio}', '${count}', '${min}', '${max}', '${avg}', '${stddev}'];
var data = ['${ratioQueryB1}', '${countQueryB1}', '${minQueryB1}', '${maxQueryB1}','${avgQueryB1}', '${stddevQueryB1}'];

var chartQueryB1 = new Chart(chartQueryB1Id, {
    type: 'bar',
    data: {
        labels: labels,
        datasets: [{
            label: stadistics,
            backgroundColor: 'rgb(221,160,221)',
            borderColor: 'rgb(25, 52, 25)',
            borderWidth: 1,
            data: data,
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }
});
</script>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryB2"/></summary>

<ul>
<li><b><spring:message code="dashboard.min"/>:</b> <jstl:out value="${minQueryB2 == \"null\" ? 0 : minQueryB2}"></jstl:out></li>
<li><b><spring:message code="dashboard.max"/>:</b> <jstl:out value="${maxQueryB2 == \"null\" ? 0 : maxQueryB2}"></jstl:out></li>
<li><b><spring:message code="dashboard.avg"/>:</b> <jstl:out value="${avgQueryB2 == \"null\" ? 0 : avgQueryB2}"></jstl:out></li>
<li><b><spring:message code="dashboard.stddev"/>:</b> <jstl:out value="${stddevQueryB2 == \"null\" ? 0 : stddevQueryB2}"></jstl:out></li>
</ul>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryB3"/></summary>

<ul>
<li><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioQueryB3 == \"null\" ? 0 : ratioQueryB3}"></jstl:out></li>
</ul>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryAPlus1"/></summary>

<spring:message code="dashboard.notSpammer" var="notSpammer" />
<div class="chartQueryAPlus1" style="width:20%;">
	<canvas id="chartQueryAPlus1"></canvas>
</div>

<jstl:set var="spammers" value="${percentageSpammersQueryAplus1}" />
<jstl:set var="notSpammers" value="${percentageNotSpammersQueryAplus1}" />
<script>
var chartQueryAPlus1Id = document.getElementById('chartQueryAPlus1');

var labels = ['Spammers', '${notSpammer}'];
var data = ['${spammers}', '${notSpammers}'];

var chartQueryAPlus1 = new Chart(chartQueryAPlus1Id, {
    type: 'pie',
    data: {
        labels: labels,
        datasets: [{
            backgroundColor: ['rgb(114,20,34)', 'rgb(48,132,70)'],
            borderColor: 'rgb(25, 52, 25)',
            borderWidth: 1,
            data: data,
        }]
    },
});
</script>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryAPlus2"/></summary>

<spring:message code="dashboard.brotherhood" var="brotherhood" />
<spring:message code="dashboard.member" var="member" />
<spring:message code="dashboard.administrator" var="administrator" />
<div class="chartQueryAPlus2" style="width:20%;">
	<canvas id="chartQueryAPlus2"></canvas>
</div>

<jstl:set var="brotherhoods" value="${avgPolarityBrotherhoodsQueryAplus2}" />
<jstl:set var="members" value="${avgPolarityMembersQueryAplus2}" />
<jstl:set var="administrators" value="${avgPolarityAdministratorsQueryAplus2}" />
<script>
var chartQueryAPlus2Id = document.getElementById('chartQueryAPlus2');

var labels = ['${brotherhood}', '${member}', '${administrator}'];
var data = ['${brotherhoods}', '${members}', '${administrators}'];

var chartQueryAPlus2 = new Chart(chartQueryAPlus2Id, {
    type: 'pie',
    data: {
        labels: labels,
        datasets: [{
            backgroundColor: ['rgb(59,131,189)', 'rgb(237,255,33)', 'rgb(195,195,195)'],
            borderColor: 'rgb(25, 52, 25)',
            borderWidth: 1,
            data: data,
        }]
    },
});
</script>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryAcmeParadeC1"/></summary>

<ul>
<li><b><spring:message code="dashboard.avg"/>:</b> <jstl:out value="${avgQueryAcmeParadeC1 == \"null\" ? 0 : avgQueryAcmeParadeC1}"></jstl:out></li>
<li><b><spring:message code="dashboard.min"/>:</b> <jstl:out value="${minQueryAcmeParadeC1 == \"null\" ? 0 : minQueryAcmeParadeC1}"></jstl:out></li>
<li><b><spring:message code="dashboard.max"/>:</b> <jstl:out value="${maxQueryAcmeParadeC1 == \"null\" ? 0 : maxQueryAcmeParadeC1}"></jstl:out></li>
<li><b><spring:message code="dashboard.stddev"/>:</b> <jstl:out value="${stddevQueryAcmeParadeC1 == \"null\" ? 0 : stddevQueryAcmeParadeC1}"></jstl:out></li>
</ul>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryAcmeParadeC2"/></summary>

<display:table class="displaytag" name="queryAcmeParadeC2" id="row">
	<spring:message code="brotherhood.titleH" var="titleH" />
	<display:column property="title" title="${titleH}" />
	
	<spring:message code="brotherhood.establishmentDateH" var="establishmentDateH" />
	<display:column title="${establishmentDateH}">
			<fmt:formatDate var="format" value="${row.establishmentDate}" pattern="dd/MM/YYYY" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="brotherhood.commentsH" var="commentsH" />
	<display:column property="comments" title="${commentsH}" />
</display:table>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryAcmeParadeC3"/></summary>

<display:table class="displaytag" name="queryAcmeParadeC3" id="row">
	<spring:message code="brotherhood.titleH" var="titleH" />
	<display:column property="title" title="${titleH}" />
	
	<spring:message code="brotherhood.establishmentDateH" var="establishmentDateH" />
	<display:column title="${establishmentDateH}">
			<fmt:formatDate var="format" value="${row.establishmentDate}" pattern="dd/MM/YYYY" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="brotherhood.commentsH" var="commentsH" />
	<display:column property="comments" title="${commentsH}" />
</display:table>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryAcmeParadeB1"/></summary>

<ul>
<li><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioQueryAcmeParadeB1 == \"null\" ? 0 : ratioQueryAcmeParadeB1}"></jstl:out></li>
</ul>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryAcmeParadeB2"/></summary>

<ul>
<li><b><spring:message code="dashboard.avg"/>:</b> <jstl:out value="${avgQueryAcmeParadeB2 == \"null\" ? 0 : avgQueryAcmeParadeB2}"></jstl:out></li>
<li><b><spring:message code="dashboard.min"/>:</b> <jstl:out value="${minQueryAcmeParadeB2 == \"null\" ? 0 : minQueryAcmeParadeB2}"></jstl:out></li>
<li><b><spring:message code="dashboard.max"/>:</b> <jstl:out value="${maxQueryAcmeParadeB2 == \"null\" ? 0 : maxQueryAcmeParadeB2}"></jstl:out></li>
<li><b><spring:message code="dashboard.stddev"/>:</b> <jstl:out value="${stddevQueryAcmeParadeB2 == \"null\" ? 0 : stddevQueryAcmeParadeB2}"></jstl:out></li>
</ul>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryAcmeParadeB3"/></summary>

<display:table class="displaytag" name="queryAcmeParadeB3" id="row">
	<spring:message code="chapter.title" var="titleH" />
	<display:column property="title" title="${titleH}" />
	
	<spring:message code="actor.name" var="nameH" />
	<display:column property="name" title="${nameH}" />
	
	<spring:message code="actor.surname" var="surnameH" />
	<display:column property="surname" title="${surnameH}" />
	
	<spring:message code="actor.email" var="emailH" />
	<display:column property="email" title="${emailH}" />
	
	<spring:message code="actor.username" var="usernameH" />
	<display:column property="userAccount.username" title="${usernameH}" />
</display:table>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryAcmeParadeB4"/></summary>

<ul>
<li><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioQueryAcmeParadeB4 == \"null\" ? 0 : ratioQueryAcmeParadeB4}"></jstl:out></li>
</ul>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryAcmeParadeB5"/></summary>

<display:table class="displaytag" name="queryAcmeParadeB5" id="row">
	<spring:message code="parade.status" var="statusH" />
	<display:column title="${statusH}">
		<jstl:out value="${row[0]}" />
	</display:column>
	<spring:message code="dashboard.ratio" var="ratio" />
	<display:column title="${ratio}">
		<jstl:out value="${row[1]}" />
	</display:column>
</display:table>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryAcmeParadeA1"/></summary>

<ul>
<li><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioQueryAcmeParadeA1 == \"null\" ? 0 : ratioQueryAcmeParadeA1}"></jstl:out></li>
</ul>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryAcmeParadeA2"/></summary>

<ul>
<li><b><spring:message code="dashboard.avg"/>:</b> <jstl:out value="${avgQueryAcmeParadeA2 == \"null\" ? 0 : avgQueryAcmeParadeA2}"></jstl:out></li>
<li><b><spring:message code="dashboard.min"/>:</b> <jstl:out value="${minQueryAcmeParadeA2 == \"null\" ? 0 : minQueryAcmeParadeA2}"></jstl:out></li>
<li><b><spring:message code="dashboard.max"/>:</b> <jstl:out value="${maxQueryAcmeParadeA2 == \"null\" ? 0 : maxQueryAcmeParadeA2}"></jstl:out></li>
<li><b><spring:message code="dashboard.stddev"/>:</b> <jstl:out value="${stddevQueryAcmeParadeA2 == \"null\" ? 0 : stddevQueryAcmeParadeA2}"></jstl:out></li>
</ul>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryAcmeParadeA3"/></summary>

<display:table class="displaytag" name="queryAcmeParadeA3" id="row">
	<spring:message code="actor.name" var="nameH" />
	<display:column property="name" title="${nameH}" />
	
	<spring:message code="actor.surname" var="surnameH" />
	<display:column property="surname" title="${surnameH}" />
	
	<spring:message code="actor.email" var="emailH" />
	<display:column property="email" title="${emailH}" />
	
	<spring:message code="actor.username" var="usernameH" />
	<display:column property="userAccount.username" title="${usernameH}" />
</display:table>

</details><br/>