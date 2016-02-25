<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015

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

<voms:hasPermissions
  var="canDelete"
  context="vo"
  permission="rw" />
<voms:hasPermissions
  var="canSuspend"
  context="vo"
  permission="SUSPEND" />

<div class="usernameHeader">

  <s:if test="name != null and surname != null">
    <span style="vertical-align: middle"> <s:property
        value="name+ ' ' +surname" />
    </span>
    <span class="institution"> (<s:property value="institution" />)
    </span>
  </s:if>
  <s:else>
    <s:set
      value="dn"
      var="userDN" />
		User
		<span class="highlight"> <voms:formatDN
        dn="${userDN}"
        fields="CN" />
    </span>
  </s:else>

  <div class="userAdminActions">
    <s:if test="#attr.canSuspend">

      <s:if test="suspended">
        <s:form
          action="restore"
          theme="simple"
          cssStyle="display:inline">
          <s:token />
          <s:hidden
            name="userId"
            value="%{id}" />
          <s:submit value="%{'Restore this user'}" />
        </s:form>
      </s:if>
      <s:else>
        <s:form
          action="suspend"
          theme="simple"
          cssStyle="display:inline">
          <s:token />
          <s:hidden
            name="userId"
            value="%{id}" />
          <s:submit
            value="%{'Suspend this user'}"
            onclick="return openSuspendDialog(this, 'suspendUserDialog','%{shortName}');" />
        </s:form>
      </s:else>

    </s:if>

    <s:if test="#attr.canDelete">
      <s:form
        action="delete"
        theme="simple"
        cssStyle="display:inline">
        <s:token />
        <s:hidden
          name="userId"
          value="%{id}" />
        <s:submit
          value="%{'Delete this user'}"
          onclick="openConfirmDialog(this, 'deleteUserDialog', '%{shortName}'); return false" />
      </s:form>

      <s:form
        action="create-acceptance-record"
        theme="simple"
        cssStyle="display:inline">
        <s:token />
        <s:hidden
          name="userId"
          value="%{id}" />
        <s:submit value="%{'Sign AUP on behalf of this user'}" />
      </s:form>
    </s:if>
    <s:if
      test="#attr.canSuspend 
          and (not model.hasInvalidAUPAcceptanceRecordForAUP(#attr.defaultAUP)) 
          and (not model.hasPendingSignAUPTasks())">

      <s:form
        action="trigger-reacceptance"
        theme="simple"
        cssStyle="display: inline;">
        <s:token />
        <s:hidden
          name="userId"
          value="%{model.id}" />
        <s:submit value="%{'Request AUP signature'}" />
      </s:form>
    </s:if>
  </div>
</div>

<div class="badge-container">
  <tiles2:insertTemplate template="suspensionDetail.jsp" />
</div>

<div class="badge-container">
  <tiles2:insertTemplate template="aupStatusDetail.jsp" />
</div>

<s:if test="not hasPendingSignAUPTasks()">
  <s:iterator
    value="aupAcceptanceRecords.{? #this.aupVersion == #attr.defaultAUP.activeVersion}">
    <s:property value="daysBeforeExpiration" /> days to AUP signature expiration
   </s:iterator>
</s:if>

<s:if test="#attr.orgdbEnabled">
  <tiles2:insertTemplate template="orgdbId.jsp" />
</s:if>

<s:if test="not #attr.disableMembershipEndTime">
  <tiles2:insertTemplate template="membershipExpiration2.jsp" />
</s:if>

<tiles2:insertTemplate template="personalInfoPane.jsp">
  <tiles2:putAttribute
    name="panelName"
    value="Personal information" />
</tiles2:insertTemplate>

<tiles2:insertTemplate template="certificatePane.jsp">
  <tiles2:putAttribute
    name="panelName"
    value="Certificates" />
</tiles2:insertTemplate>

<tiles2:insertTemplate template="mappingsPane.jsp" />

<tiles2:insertTemplate template="attributesPane.jsp">
  <tiles2:putAttribute
    name="panelName"
    value="Generic Attributes" />
</tiles2:insertTemplate>

<tiles2:insertTemplate template="requestHistoryPane.jsp">
  <tiles2:putAttribute
    name="panelName"
    value="Request history" />
</tiles2:insertTemplate>


