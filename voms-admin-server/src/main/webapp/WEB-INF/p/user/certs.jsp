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

<voms:hasPermissions var="canDelete" context="vo"
  permission="CONTAINER_READ|CONTAINER_WRITE|MEMBERSHIP_READ|MEMBERSHIP_WRITE" />

<voms:hasPermissions var="canSuspend" context="vo"
    permission="CONTAINER_READ|MEMBERSHIP_READ|SUSPEND" />


<s:url action="confirm-suspend-certificate" 
  namespace="/user" 
  var="suspendCertificateAction"/>
  
<s:url action="restore-certificate" 
  namespace="/user" 
  var="restoreCertificateAction"/> 
    
<s:url action="confirm-delete-certificate" 
  namespace="/user" 
  var="deleteCertificateAction"/> 

<p>
<s:if test="certificates.empty">
  No certificates defined for this user.
</s:if>
<s:else>
  <div class="container-fluid">
      <s:iterator var="cert" value="certificates">
        <s:set value="subjectString"
            var="thisCertDN" />
         <div class="col-xs-8">
           <span class="certificate-subject">
            ${cert.subjectString}
           </span>
           <br/>
           <span class="certificate-issuer">
              ${cert.ca.subjectString}
           </span>
         </div>
         <div>
            <s:if test="suspended">
              <span class="label label-danger">suspended</span>
            </s:if>
         </div>
         <div class="pull-right">
             <s:form id="certsOperationsForm" 
              namespace="/user" cssClass="form-horizontal">
                <s:token/>
                <s:hidden name="userId" value="%{model.id}" />
                <s:hidden name="certificateId" value="%{#cert.id}" />
                
                <div class="btn-group btn-group-sm" role="group">
                  <s:if test="#attr.canSuspend and not suspended">
                    <button type="submit" 
                      class="btn btn-warning"
                      formaction="${certificateSuspendAction}"
                      formmethod="post">Suspend</button>
                  </s:if>
                  <s:if test="#attr.canSuspend and suspended and not user.suspended">
                    <button type="submit" 
                      class="btn"
                      formaction="${certificateRestoreAction}"
                      formmethod="post">Restore</button>
                  </s:if>
                  <s:if test="model.certificates.size > 1 and #attr.canDelete">
                      <button type="submit" 
                        class="btn btn-danger"
                        formaction="${certificateDeleteAction}"
                        formmethod="post">Delete</button>
                  </s:if>
                </div>
             </s:form>
        </div>
      </s:iterator>
  </div>
</s:else>
</p>