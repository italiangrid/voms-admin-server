
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>


<div class="reloadable">
<tiles2:insertTemplate template="../shared_20/errorsAndMessages.jsp"/>

<s:set var="userId" scope="page"
	value="id" /> <voms:unsubscribedGroups var="unsubscribedGroups"
	userId="${userId}" /> <voms:unassignedRoleMap var="unassignedRoleMap"
	userId="${userId}" />


<s:if test="not #attr.unsubscribedGroups.empty">

	<div class="subscribeGroups"><s:form
		action="request-group-membership" namespace="/user" theme="simple"
		onsubmit="ajaxSubmit(this,'req-content'); return false;">
		<s:hidden name="userId" value="%{model.id}" />
		<s:select list="#attr.unsubscribedGroups" listKey="id"
			listValue="name" name="groupId" />
		<s:submit value="%{'Request membership'}" />
	</s:form></div>
</s:if>


<div class="membershipTab">
<table cellpadding="0" cellspacing="0">
	<tr>
		<th>Group name</th>
		<th>Roles</th>
		<th />
	</tr>
	<s:iterator var="mapping" value="model.mappingsMap">
		<tr class="tableRow">

			<td class="groupName"><s:property value="key" /></td>

			<td><s:iterator var="role" value="value">
				<div class="userRoleName"><s:property value="name" /></div>
			</s:iterator></td>

			<td/>
			<%--
			<td class="roleAssign"><s:if
				test="%{not #attr.unassignedRoleMap[#mapping.key.id].isEmpty}">
				
				<s:form action="request-role-membership" namespace="/user"
					theme="simple"
					onsubmit="ajaxSubmit(this,'mappings-req-content'); return false;">
					<s:token />
					<s:hidden name="userId" value="%{model.id}" />
					<s:hidden name="groupId" value="%{#mapping.key.id}" />
					<s:select list="#attr.unassignedRoleMap[#mapping.key.id]"
						listKey="id" listValue="name" name="roleId" />
					<s:submit value="%{'Request role membership'}"
						cssClass="assignRoleButton" />
				</s:form>
			</s:if></td>
			 --%>
	</s:iterator>
</table>
</div>
</div>
