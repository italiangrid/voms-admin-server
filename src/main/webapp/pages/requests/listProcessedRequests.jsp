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
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>


<div class="header1">
	Processed VO Membership requests
</div>

<c:choose>
	<c:when test="${!empty requests}">


		<table class="table" cellpadding="0" cellspacing="0">
		
			<c:forEach var="pRequest" items="${requests}" varStatus="status">
				<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">			
					<td>
						<div class="userDN">
							<c:url value="/LoadVOMembershipRequest.do" var="loadReqURL">
								<c:param name="requestId" value="${pRequest.id}"/>
							</c:url>
							<voms:link
								context="vo"
								permission="REQUESTS_READ"
								href="${loadReqURL}"
								styleClass="actionLink"
								disabledStyleClass="disabledLink">
								<voms:formatDN dn="${pRequest.dn}" fields="CN"/>						
							</voms:link>			
						</div>
						<div class="userCA">
							<voms:formatDN dn="${pRequest.ca}" fields="CN,O"/>
						</div>
					</td>
					<td>
						<div class="requestStatus">
							${ pRequest.status eq 2 ? 'approved' : 'rejected'}
						</div>
					</td>
				</tr>		
			</c:forEach>
		
		</table>
	</c:when>
		<c:otherwise>
		<p>
			No processed requests found.
		</p>
	</c:otherwise>
</c:choose>