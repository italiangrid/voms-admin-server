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
  
  <voms:hasPermissions
          var="canSuspend"
          context="vo"
          permission="SUSPEND" />
  
  <s:if test="aupAcceptanceRecords.empty">
    
    No acceptance records found for the currently active AUP version. 
  </s:if>
  <s:else>
    
    <s:set var="currentAccRec" value="aupAcceptanceRecords.{? #this.aupVersion == #attr.defaultAUP.activeVersion}"/>
    <s:if test="not #currentAccRec.empty">
      <s:iterator value="aupAcceptanceRecords.{? #this.aupVersion == #attr.defaultAUP.activeVersion}">
        <s:url
          action="load"
          namespace="/aup"
          var="saURL" />
                
        <s:if test="hasPendingSignAUPTasks()">
          <div style="margin-bottom: 1em" >
            <tiles2:insertTemplate template="aupStatusDetail.jsp" />
          </div>
          <div class="alert alert-warning">
            <strong>WARNING:</strong>
            If the user fails to sign the AUP in the requested time he/she will be suspended.
            Note that the user can restore his/her membership at <strong>any time</strong>, even after being
            suspended, by just signing the AUP following the instructions sent in the Sign AUP email 
            notification.
          </div>
        </s:if>
        <s:else>
          <s:if test="not valid">
              <div class="alert alert-info">
                <strong><a href="${saURL}">AUP</a> signature has been invalidated.</strong>
                A request to sign the AUP will be sent to the user as soon as the membership check task
                runs again.
              </div> 
          </s:if>
          <s:else>
            <div>
            The <a href="${saURL}">current AUP</a> was last signed
            <s:property value="daysSinceLastAcceptance" />
            days ago. This signature will expire in
            <s:property value="daysBeforeExpiration" />
            days.
          </div>
          </s:else>
        </s:else>
      
      </s:iterator>
      <s:if test="#request.registrationEnabled">
        
        <div style="text-align: right;">
        
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

        <s:if test="#attr.canSuspend 
          and (not model.hasInvalidAUPAcceptanceRecordForAUP(#attr.defaultAUP)) 
          and (not model.hasPendingSignAUPTasks())">
          
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
        
      </div>
    </s:if>
   </s:if>
   <s:else>
    No acceptance records found for the active AUP.
   </s:else>
  </s:else>
</div>

