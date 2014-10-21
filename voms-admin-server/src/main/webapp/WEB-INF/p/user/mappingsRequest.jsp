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
<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp"/>

<s:set var="userId" scope="page"
	value="id" /> <voms:unsubscribedGroups var="unsubscribedGroups"
	userId="${userId}" /> <voms:unassignedRoleMap var="unassignedRoleMap"
	userId="${userId}" />


<s:set var="requestedGroupNames" value="pendingGroupMembershipRequests.{groupName}"/>
<s:set var="unrequestedGroups" value="#attr.unsubscribedGroups.{? #this.name not in #requestedGroupNames}"/>  

<s:if test="not #unrequestedGroups.empty">

    <s:if test="#request.registrationEnabled">
	<div class="subscribeGroups"><s:form
		action="request-group-membership" namespace="/user" theme="simple"
		onsubmit="ajaxSubmit(this,'req-content'); return false;">
		<s:token/>
		<s:hidden name="userId" value="%{model.id}" />
		<s:select list="#unrequestedGroups" listKey="id"
			listValue="name" name="groupId" />
		<s:submit value="%{'Request membership'}" />
	</s:form></div>
    </s:if>
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
            
            <s:set var="requestedRoleNames" value="pendingRoleMembershipRequests.{? #this.groupName == #mapping.key.name}.{roleName}"/>
            
			<td class="groupName" style="width: 45%"><s:property value="key" /></td>

			<td style="width: 30%">
              <s:iterator var="role" value="value">
				<div class="userRoleName"><s:property value="name" /></div>
			  </s:iterator>
              <!-- Requested roles -->
              
              <s:iterator var="rrName" value="#requestedRoleNames">
                <div class="requestedRoleName">
                  <s:property value="#rrName"/>
                  <span>(waiting for approval)</span>
                </div>
              </s:iterator>
      
          </td>
			
			<td class="roleAssign">
      
              
              
              <s:set var="daRoles" value="#attr.unassignedRoleMap[#mapping.key.id].{?#this.name not in #requestedRoleNames}"/>
              
              
              <s:if
				test="%{not #daRoles.empty and #request.registrationEnabled}">
				
				<s:form action="request-role-membership" namespace="/user"
					theme="simple"
					onsubmit="ajaxSubmit(this,'req-content'); return false;">
					<s:token />
					<s:hidden name="userId" value="%{model.id}" />
					<s:hidden name="groupId" value="%{#mapping.key.id}" />
					<s:select list="#daRoles"
						listKey="id" listValue="name" name="roleId" />
					<s:submit value="%{'Request role'}"
						cssClass="assignRoleButton" />
				</s:form>
			</s:if></td>
       </tr>
	</s:iterator>
    
    <s:iterator value="#requestedGroupNames" var="requestedGroup">
      <tr class="tableRow">
        <td class="requestedGroupName" colspan="3">
          ${requestedGroup}
          <span>(waiting for approval)</span>
        </td>
      </tr>  
    </s:iterator>
</table>
</div>
</div>
