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
<voms:hasPermissions var="canReadPI" context="vo" permission="PERSONAL_INFO_READ" />
<voms:hasPermissions var="canDelete" context="vo" permission="rw" />
<voms:hasPermissions var="canSuspend" context="vo" permission="SUSPEND" />
<div class="usernameHeader">
  <s:if test="#attr.canReadPI or #attr.currentAdmin.is(model)">
    <s:if test="name != null and surname != null and name.trim() != '' and surname.trim() != ''">
      <div>
        <s:property value="name+ ' ' +surname" />
        <span style="font-weight: normal; font-size: smaller">( ${id} )</span>
        <s:if test="#attr.orgdbEnabled">
          <span style="font-weight: normal; font-size: smaller">
            <tiles2:insertTemplate template="orgdbId.jsp" />
          </span>
        </s:if>
      </div>
      <div style="margin-top: .5em">
        <span class="institution">
          <s:property value="institution" />
        </span>
      </div>
    </s:if>
    <s:else>
        User <s:property value="id" />
    </s:else>
  </s:if>
  <s:else>
        User <s:property value="id" />
  </s:else>
</div>
<div class="userAdminActions">
  <s:if test="#attr.canDelete == true and #attr.readOnlyMembershipExpiration == false">
    <s:form action="change-membership-expiration" theme="simple" cssClass="middleline" cssStyle="display:inline">
      <s:token />
      <s:hidden name="userId" value="%{id}" />
      <s:submit value="%{'Change membership expiration date'}"
        disabled="%{#attr.canDelete == false or #attr.readOnlyMembershipExpiration == true}"
        tooltip="Change membership expiration date for the user" />
    </s:form>
  </s:if>
  <s:if test="#attr.canDelete and #attr.orgdbEnabled">
    <s:form action="change-orgdb-id" namespace="/user" theme="simple" cssStyle="display: inline">
      <s:hidden name="userId" value="%{id}" />
      <s:submit value="%{'Change HR id'}" />
    </s:form>
  </s:if>
  <s:if test="#attr.canSuspend">
    <s:if test="suspended">
      <s:form action="restore" theme="simple" cssStyle="display:inline">
        <s:token />
        <s:hidden name="userId" value="%{id}" />
        <s:submit value="%{'Restore this user'}" />
      </s:form>
    </s:if>
    <s:else>
      <s:form action="suspend" theme="simple" cssStyle="display:inline">
        <s:token />
        <s:hidden name="userId" value="%{id}" />
        <s:submit value="%{'Suspend this user'}"
          onclick="return openSuspendDialog(this, 'suspendUserDialog','%{shortName}');" />
      </s:form>
    </s:else>
  </s:if>
  <s:if test="#attr.canDelete">
    <s:form action="delete" theme="simple" cssStyle="display:inline">
      <s:token />
      <s:hidden name="userId" value="%{id}" />
      <s:submit value="%{'Delete this user'}"
        onclick="openConfirmDialog(this, 'deleteUserDialog', '%{shortName}'); return false" />
    </s:form>
    <s:form action="create-acceptance-record" theme="simple" cssStyle="display:inline">
      <s:token />
      <s:hidden name="userId" value="%{id}" />
      <s:submit value="%{'Sign AUP on behalf of this user'}" />
    </s:form>
  </s:if>
  <s:if
    test="#attr.canSuspend 
        and (not model.hasInvalidAUPAcceptanceRecordForAUP(#attr.defaultAUP)) 
        and (not model.hasPendingSignAUPTasks())">
    <s:form action="trigger-reacceptance" theme="simple" cssStyle="display: inline;">
      <s:token />
      <s:hidden name="userId" value="%{model.id}" />
      <s:submit value="%{'Request AUP signature'}" />
    </s:form>
  </s:if>
</div>
<tiles2:insertTemplate template="membershipStatusDetail.jsp" />
<tiles2:insertTemplate template="personalInfoPane.jsp">
  <tiles2:putAttribute name="panelName" value="Personal information" />
</tiles2:insertTemplate>
<tiles2:insertTemplate template="certificatePane.jsp">
  <tiles2:putAttribute name="panelName" value="Certificates" />
</tiles2:insertTemplate>
<tiles2:insertTemplate template="mappingsPane.jsp" />
<tiles2:insertTemplate template="attributesPane.jsp">
  <tiles2:putAttribute name="panelName" value="Generic Attributes" />
</tiles2:insertTemplate>
<tiles2:insertTemplate template="requestHistoryPane.jsp">
  <tiles2:putAttribute name="panelName" value="Request history" />
</tiles2:insertTemplate>
