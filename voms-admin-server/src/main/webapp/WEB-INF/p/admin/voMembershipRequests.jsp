<%--

    Copyright (c) Members of the EGEE Collaboration. 2006-2009.
    See http://www.eu-egee.org/partners/ for details on the copyright holders.

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
    	Andrea Ceccanti (INFN)

--%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<s:set
	value="pendingRequests.{? #this instanceof org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest and 
      #this.status == @org.glite.security.voms.admin.persistence.model.request.Request$STATUS@CONFIRMED }"
	var="pendingMembershipReqs" />

<s:if test="not #pendingMembershipReqs.empty">

	<h1>
		VO membership requests: <span class="badge baseline"><s:property
				value="#pendingMembershipReqs.size()" /></span>
	</h1>

	<s:form id="req_%{id}" action="membership-decision"
		onsubmit="ajaxSubmit($(this),'pending-req-content'); return false;"
		theme="simple" cssClass="decisionForm">
		
		<s:token />
		<s:hidden name="requestId" value="-1" />
		<s:hidden
              name="decision" value="reject" />
		
		<table>
			<s:iterator value="#pendingMembershipReqs">
				<tr class="req-header-row">
					<th>Requester</th>
					<th>Personal Info</th>
					<th />
				</tr>
				
				<tr id="req_info_<s:property value='id'/>">
					<td class="no-border-bottom"><tiles2:insertTemplate
							template="userInfo.jsp" flush="true" /></td>


					<td class="personalInfo no-border-bottom">
						<dl>
							<dt>Address:</dt>
							<dd>${requesterInfo.address}</dd>
							<dt>Phone number:</dt>
							<dd>${requesterInfo.phoneNumber}</dd>
							<dt>Email:</dt>
							<dd>${requesterInfo.emailAddress}</dd>
						</dl>
					</td>
					<td class="no-border-bottom" />
				</tr>
				
				<tiles2:insertTemplate template="attributeRequestManagement.jsp" />
				<tr>
					<td colspan="2" />
					<td style="vertical-align: bottom; text-align: right;">
					 
							<s:submit 
							 type="button"
							 value="approve" 
							 onclick="this.form.decision.value = this.value; this.form.requestId.value = %{id}" />

						<s:submit 
						  type="button"
						  value="reject"
							onclick="this.form.decision.value = this.value; this.form.requestId.value = %{id}" />
							
							
					</td>
				</tr>

			</s:iterator>
		</table>
	</s:form>
</s:if>