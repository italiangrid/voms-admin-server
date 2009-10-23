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

<div class="reloadable">

<tiles2:insertTemplate template="../shared_20/errorsAndMessages.jsp"/>

<s:set var="userId" scope="page"
	value="id" /> <voms:unsubscribedGroups var="unsubscribedGroups"
	userId="${userId}" /> <voms:unassignedRoleMap var="unassignedRoleMap"
	userId="${userId}" /> <s:if test="not #attr.unsubscribedGroups.empty">

	<div class="subscribeGroups"><s:form action="add-to-group"
		namespace="/user" theme="simple"
		onsubmit="ajaxSubmit(this,'mappings-content'); return false;">
		<s:token />
		<s:hidden name="userId" value="%{model.id}" />
		<s:select list="#attr.unsubscribedGroups" listKey="id"
			listValue="name" name="groupId" />
		<s:submit value="%{'Add to group'}" />
	</s:form></div>
</s:if>

<div class="membershipTab">
<table cellpadding="0" cellspacing="0">
	<tr>
		<th>Group name</th>
		<th>Roles</th>
	</tr>
	<s:iterator var="mapping" value="model.mappingsMap">
		<tr class="tableRow">

			<td>
			<table class="membership-management" cellpadding="0" cellspacing="0">
				<tr>
					<td class="groupName"><s:property value="key" /></td>
				</tr>
				<s:if test="not key.rootGroup">
					<tr>
						<td class="removeFromGroup"><s:form
							action="remove-from-group" namespace="/user" theme="simple"
							onsubmit="ajaxSubmit(this,'mappings-content'); return false;">
							<s:hidden name="userId" value="%{model.id}" />
							<s:hidden name="groupId" value="%{#mapping.key.id}" />
							<s:submit value="%{'Remove from this group'}" />
						</s:form></td>
					</tr>
				</s:if>
			</table>
			</td>

			<td>
			<table class="membership-management" cellpadding="0" cellspacing="0">
				<s:iterator var="role" value="value">

					<tr>
						<td class="userRoleName"><s:property value="name" /></td>
						<td class="roleDismiss"><s:form action="dismiss-role"
							namespace="/user" theme="simple"
							onsubmit="ajaxSubmit(this,'mappings-content'); return false;">
							<s:token />
							<s:hidden name="userId" value="%{model.id}" />
							<s:hidden name="groupId" value="%{#mapping.key.id}" />
							<s:hidden name="roleId" value="%{#role.id}" />
							<s:submit value="%{'Dismiss role'}" />
						</s:form></td>
					</tr>
				</s:iterator>


				<tr>
					<td />
					<td class="roleAssign"><s:if
						test="%{not #attr.unassignedRoleMap[#mapping.key.id].isEmpty}">

						<s:form action="assign-role" namespace="/user" theme="simple"
							onsubmit="ajaxSubmit(this,'mappings-content'); return false;">
							<s:token />
							<s:hidden name="userId" value="%{model.id}" />
							<s:hidden name="groupId" value="%{#mapping.key.id}" />
							<s:select list="#attr.unassignedRoleMap[#mapping.key.id]"
								listKey="id" listValue="name" name="roleId" />
							<s:submit value="%{'Assign role'}" cssClass="assignRoleButton" />
						</s:form>

					</s:if></td>
				</tr>

			</table>
			</td>
		</tr>
	</s:iterator>
</table>
</div>
</div>