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

<s:form id="confirmUserSuspensionForm" action="suspend" namespace="/user"
  theme="bootstrap" cssClass="form-horizontal">
  
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal"
      aria-label="Close">
      <span aria-hidden="true">&times;</span>
    </button>
    <h4 class="modal-title" id="suspendUserLabel">Suspend user?</h4>
  </div>
  
  <div class="modal-body">
    <s:token/>
    <s:hidden name="userId" value="%{id}"/>
    <p>
      You are about to suspend the following user: <br/>
      <em><s:property value="shortName"/></em>
      <br/>
      Once suspended the user will not be able to obtain VOMS
      credentials for this VO.
    </p>
    <s:textfield name="suspensionReason" 
      label="Reason" size="40"
      value="%{suspensionReason}" 
      placeholder="Insert the reason why the user is being suspended here..." />
  </div>
  
  <div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
    <sj:submit cssClass="btn btn-warning" 
      formIds="confirmUserSuspensionForm" 
      value="%{'Suspend user'}" 
      validate="true" validateFunction="bootstrapValidation"/>
  </div>
</s:form>