<%--
 Copyright (c) Members of the EGEE Collaboration. 2006.
 See http://www.eu-egee.org/partners/ for details on the copyright
 holders.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Authors:
     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://org.glite.security.voms.tags" prefix="voms"%>

<div class="header2">Certificate information</div>

<table>

<tr>
	<td colspan="2">
		<div class="header1">Subject info</div>
	</td>
</tr>	
<c:forEach var="field" items="${certificate.subjectAsList}">
	<tr>
		<td class="label">${field[0]}:</td>
		<td>${field[1]}</td>
	</tr>
</c:forEach>
<tr/>

<tr>
	<td class="label">Not after:</td>
	<td>
		<c:choose>
			<c:when test="${empty certificate.notAfter}">
				--
			</c:when>
			<c:otherwise>
				<fmt:formatDate value="${certificate.notAfter}"/>						
			</c:otherwise>
		</c:choose>
	</td>
</tr>
<tr/>


<tr>
	<td colspan="2">
		<div class="header1">Issuer info</div>
	</td>
</tr>
<c:forEach var="field" items="${certificate.issuerAsList}">
	<tr>
		<td class="label">${field[0]}:</td>
		<td>${field[1]}</td>
	</tr>
</c:forEach>
<tr>
	<td class="label">Not after:</td>
	<td>
		<fmt:formatDate value="${certificate.ca.notAfter}"/>
	</td>
</table>

