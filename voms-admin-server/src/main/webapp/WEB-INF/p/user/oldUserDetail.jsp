<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<voms:hasPermissions var="canDelete" context="vo" permission="rw"/>
<voms:hasPermissions var="canSuspend" context="vo" permission="SUSPEND"/>

<h2>
  <tiles2:insertTemplate template="userName.jsp"/>
</h2>

<div class="usernameHeader">
    
<div class="userAdminActions">
  <s:if test="#attr.canSuspend">
    
    <s:if test="suspended">
      <s:form action="restore" theme="simple" cssStyle="display:inline">
        <s:token/>
        <s:hidden name="userId" value="%{id}"/>
        <s:submit value="%{'Restore this user'}"/>
      </s:form>
    </s:if>
    <s:else>
      <s:url 
        action="confirm-user-suspension" 
        var="confirmSuspensionAction" 
        namespace="/user">
      <s:param name="userId" value="%{id}"/>    
    </s:url>
      
      <a href="${confirmSuspensionAction}" 
        data-toggle="modal" 
        data-target="#ConfirmUserSuspensionDialog"
        class="btn btn-warning">
        Suspend
      </a>
    </s:else>
    
    </s:if>
    
  <s:if test="#attr.canDelete">
    <s:url 
        action="confirm-user-delete" 
        var="confirmDeleteAction" 
        namespace="/user">
      <s:param name="userId" value="%{id}"/>
    </s:url>
    <a href="${confirmDeleteAction}" 
        data-toggle="modal" 
        data-target="#ConfirmUserDeleteDialog"
        class="btn btn-danger">
        Delete
    </a>
  </s:if>
</div>
</div>

<div class="badge-container">
  <tiles2:insertTemplate template="suspensionDetail.jsp"/>
</div>

<div class="badge-container">
  <tiles2:insertTemplate template="aupStatusDetail.jsp"/>
</div>

<s:if test="not #attr.disableMembershipEndTime">
  <tiles2:insertTemplate template="membershipExpiration2.jsp"/>
</s:if>

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

<tiles2:insertTemplate template="aupStatusPane.jsp">
  <tiles2:putAttribute name="panelName" value="AUP acceptance status"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="requestHistoryPane.jsp">
    <tiles2:putAttribute name="panelName" value="Request history"/>
</tiles2:insertTemplate>