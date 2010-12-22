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
	test="not pendingRequests.{? #this instanceof org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest }.empty">
	<h1 style="margin-top: 2em">Membership removal requests:</h1>
	<table>
		<tr>
			<th>Requester</th>
			<th>Reason</th>
			<th />
		</tr>
		<s:iterator
			value="pendingRequests.{? #this instanceof org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest}"
			var="membershipRemovalReq">
			<tr class="tableRow">
				<td style="width: 40%"><tiles2:insertTemplate
					template="userInfo.jsp" flush="true" /></td>
				<td><s:property value="reason" /></td>
				<td style="vertical-align: bottom; text-align: right"><s:form
					action="decision">
					<s:token/>
					<s:hidden name="requestId" value="%{id}" />
					<s:radio list="{'approve','reject'}" name="decision"
						onchange="ajaxSubmit($(this).closest('form'),'pending-req-content'); return false;" />
				</s:form></td>
			</tr>
		</s:iterator>
	</table>
</s:if>