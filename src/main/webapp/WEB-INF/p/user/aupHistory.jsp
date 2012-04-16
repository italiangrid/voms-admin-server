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
  <tiles2:insertTemplate template="../shared/errorsAndMessages.jsp" />
  <s:if test="aupAcceptanceRecords.empty">
No AUP acceptance records found.
	<s:if test="#request.registrationEnabled">
      <voms:hasPermissions
        var="canSuspend"
        context="vo"
        permission="SUSPEND" />

      <s:if test="#attr.canSuspend">
        <div style="text-align: right;">
          <s:form
            action="create-acceptance-record"
            onsubmit="ajaxSubmit(this,'aup-history-content'); return false;"
            theme="simple"
            cssStyle="display: inline">

            <s:token />

            <s:hidden
              name="userId"
              value="%{model.id}" />

            <s:submit value="%{'Sign AUP on behalf of user'}" />
          </s:form>
        </div>
      </s:if>
    </s:if>
  </s:if>
  <s:else>

    <s:iterator value="aupAcceptanceRecords">
      <s:url
        action="load"
        namespace="/aup"
        var="saURL" />

      <s:if test="not valid">
        <s:if test="hasPendingSignAUPTasks()">
          <div>
            <tiles2:insertTemplate template="aupStatusDetail.jsp" />
            <div style="display: inline; vertical-align: middle">
              for AUP version <a href="${saURL}"><s:property
                  value="aupVersion.version" /></a>.
                  <s:if test="getPendingSignAUPTask(#attr.defaultAUP).daysBeforeExpiration > 0">
                    Task expires in 
                    <s:property value="getPendingSignAUPTask(#attr.defaultAUP).daysBeforeExpiration"/> days.
                  </s:if>
            </div>
          </div>
          </s:if>
          <s:else>
            AUP <a href="${saURL}"><s:property value="aupVersion.version" /></a> signature has been invalidated.
            A request to sign the AUP is being sent to the user. 
          </s:else>
      </s:if>
      <s:else>
        <div>
          AUP version <a href="${saURL}"><s:property
              value="aupVersion.version" /></a> was last signed
          <s:property value="daysSinceLastAcceptance" />
          days ago. This signature will expire in
          <s:property value="daysBeforeExpiration" />
          days.
        </div>
      </s:else>
    </s:iterator>

    <s:if test="#request.registrationEnabled">
      <voms:hasPermissions
        var="canSuspend"
        context="vo"
        permission="SUSPEND" />
      <div style="text-align: right;">

        <s:if test="#attr.canSuspend">
          <s:if
            test="(not model.hasInvalidAUPAcceptanceRecord()) and (not model.hasPendingSignAUPTasks())">

            <s:form
              action="trigger-reacceptance"
              onsubmit="ajaxSubmit(this,'aup-history-content'); return false;"
              theme="simple"
              cssStyle="display: inline;">
              <s:token />
              <s:hidden
                name="userId"
                value="%{model.id}" />
              <s:submit value="%{'Request AUP reacceptance'}" />
            </s:form>

          </s:if>
        </s:if>

      </div>
    </s:if>
  </s:else>
</div>

