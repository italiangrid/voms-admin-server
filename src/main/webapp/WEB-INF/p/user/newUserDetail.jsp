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

<div class="usernameHeader">
  	<s:if test="name != null and surname != null">
		<s:property value="name+ ' ' +surname"/>
			<span class="institution">
    			(<s:property value="institution"/>)  
  			</span>
	</s:if>
	<s:else>	
		<s:set value="dn" var="userDN"/>
		User
		<span class="highlight">
			<voms:formatDN dn="${userDN}" fields="CN"/>
		</span>
	</s:else>
</div>

<s:if test="suspended">
<div class="userSuspensionInfo">
  This user is currently <span class="suspended">suspended</span>.
  <div>Reason: <span class="suspensionReason"> ${suspensionReason }</span></div>
</div>
</s:if>
<voms:hasPermissions var="canDelete" context="vo" permission="rw"/>
<voms:hasPermissions var="canSuspend" context="vo" permission="SUSPEND"/>


<tiles2:insertTemplate template="membershipExpiration.jsp"/>

<div class="userAdminActions">
  <s:if test="#attr.canSuspend">
    <s:url action="suspend" var="suspendUsrURL" method="input">
      <s:param name="userId" value="id"/>
    </s:url>
    <s:url action="restore" var="restoreUsrURL">
      <s:param name="userId" value="id"/>
    </s:url>
    <s:if test="suspended">
      <a href="${restoreUsrURL}" class="actionLink">Restore this user</a>
    </s:if>
    <s:else>
      <a href="${suspendUsrURL}" class="actionLink">Suspend this user</a>
    </s:else>
    
    </s:if>
    
	<s:if test="#attr.canDelete">
		<s:url action="delete" var="deleteUsrURL">
			<s:param name="userId" value="id"/>
		</s:url>
	
		<a href="${deleteUsrURL}" class="actionLink" onclick="openConfirmDialog(this, 'deleteUserDialog','${shortName}'); return false">Delete this user</a>
	
	</s:if>
	
	<s:if test="#attr.canSuspend">
		<s:url action="trigger-reacceptance" var="triggerURL">
      		<s:param name="userId" value="id"/>
    	</s:url>
    	
    	
    	<a href="${triggerURL}" class="actionLink">Trigger AUP reacceptance</a>
    	
	</s:if>
</div>
<tiles2:insertTemplate template="personalInfoPane.jsp">
  <tiles2:putAttribute name="panelName" value="Personal information"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="certificatePane.jsp">
  <tiles2:putAttribute name="panelName" value="Certificates"/>
</tiles2:insertTemplate>
  
<tiles2:insertTemplate template="mappingsPane.jsp"/>

<tiles2:insertTemplate template="attributesPane.jsp">
	<tiles2:putAttribute name="panelName" value="Generic Attributes"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="aupHistoryPane.jsp">
  <tiles2:putAttribute name="panelName" value="AUP acceptance history"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="requestHistoryPane.jsp">
	  <tiles2:putAttribute name="panelName" value="Request history"/>
</tiles2:insertTemplate>


