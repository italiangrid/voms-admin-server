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

<s:if
	test="not pendingRequests.{? #this instanceof org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest and #this.status == @org.glite.security.voms.admin.persistence.model.request.Request$STATUS@SUBMITTED }.empty">
	<h1>Unconfirmed VO membership requests:</h1>
	<table>
		<thead>
			<tr>
				<th>Requester</th>
				<th>Personal Info</th>
				<th />
			</tr>
		</thead>
		<tbody>
		<s:iterator
			value="pendingRequests.{? #this instanceof org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest and 
			#this.status == @org.glite.security.voms.admin.persistence.model.request.Request$STATUS@SUBMITTED }">
			
			<tr>
				<td><tiles2:insertTemplate template="userInfo.jsp" flush="true" />
				</td>


				<td class="personalInfo">
				<dl>
					<dt>Address:</dt>
					<dd>${requesterInfo.address}</dd>
					<dt>Phone number:</dt>
					<dd>${requesterInfo.phoneNumber}</dd>
					<dt>Email:</dt>
					<dd>${requesterInfo.emailAddress}</dd>
				</dl>
				</td>

				<td style="vertical-align: bottom; text-align: right;">
				<s:form
					action="drop-request" onsubmit="ajaxSubmit($(this),'pending-req-content'); return false;">
					<s:hidden name="requestId" value="%{id}" />
					<s:submit value="%{'Drop this request'}"/>
				</s:form>
				</td>
			</tr>
		</s:iterator>
		</tbody>
	</table>
	
</s:if>