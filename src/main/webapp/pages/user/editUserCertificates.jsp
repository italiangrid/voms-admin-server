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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://org.glite.security.voms.tags" prefix="voms"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>

<div id="addCertificateBox">
		<c:url value="/UserCertificates.do" var="addCertificateURL">
		<c:param name="method" value="preAdd"/>
		<c:param name="userId" value="${vomsUser.id}"/>
	</c:url>
	<voms:link
			permission="CONTAINER_READ|CONTAINER_WRITE"
			disabledStyleClass="disabledLink"
			styleClass="vomsLink"
			context="vo"
			href="${addCertificateURL}">
			Add certificate
	</voms:link>
</div>
<div id="certificateListPane">
<c:choose>
	<c:when test="${empty vomsUser.certificates}">
		No certificates imported for this user.
	</c:when>
	<c:otherwise>
	
		<table class="table" cellpadding="0" cellspacing="0">
		
			<tr class="tableHeaderRow">
				<td>Subject</td>
				<td>Issuer</td>
				<td>Expires</td>
				<td colspan="2"/>
			</tr>
			<c:forEach var="cert" items="${vomsUser.certificates}" varStatus="status">
				<tr>
					<td>
						<div>
							<voms:formatDN 
								dn="${cert.subjectString}"
								fields="CN"/>
						</div>
					</td>
					<td>
						<div class="userCA">
							<voms:formatDN 
								dn="${cert.ca.subjectString}"
								fields="CN"/>
						</div>
					</td>
					<td>
						${cert.notAfter}
					</td>
					
					<td>
						<div class="actions">
							<c:url value="/UserCertificates.do" var="manageCertURL">
								<c:param name="method" value="load"/>
								<c:param name="certificateId" value="${cert.id}"/>
							</c:url>
							<voms:link 
								href="${manageCertURL}"
								context="vo"
								permission="CONTAINER_READ|CONTAINER_WRITE"
								styleClass="actionLink"
								disabledStyleClass="disabledLink">
								more info
							</voms:link>
						</div>
					</td>
					
					<td>
						<div class="actions">
							<c:url value="/UserCertificates.do" var="deleteCertURL">
								<c:param name="method" value="delete"/>
								<c:param name="certificateId" value="${cert.id}"/>
							</c:url>
							<voms:link 
								href="${deleteCertURL}"
								context="vo"
								permission="CONTAINER_READ|CONTAINER_WRITE"
								styleClass="actionLink"
								disabledStyleClass="disabledLink">
								delete
							</voms:link>
						</div>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:otherwise>
</c:choose>
</div>
<div class="separator">&nbsp;</div>

