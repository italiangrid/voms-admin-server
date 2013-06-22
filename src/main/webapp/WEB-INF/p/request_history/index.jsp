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
<h1>Request log</h1>

<voms:hasPermissions 
    var="isVOAdmin"
    context="vo"
    permission="ALL"/>

<s:if test="not #attr.isVOAdmin">
  You don't have sufficient privileges to access this information.  
</s:if>
<s:else>  
  <s:if test="closedRequests.size() == 0">
    No requests found.
  </s:if>
  <s:else>
  <table class="table">
    <tr>
      <th>Request type</th>
      <th>Info</th>
      <th>Approver</th>
      <th>Status</th>
    </tr>
    <s:iterator value="closedRequests" var="req">
      <tr>
        <td>
        
        <div class="requestType">
          <s:property value="typeName"/>
          <span class="reqId">(${id})</span>
        </div>
          <dl class="requestDateInfo">
            <dt>Created on:</dt>
            <dd>${creationDate}</dd>
            <s:if test="status.toString() == 'EXPIRED'">
              <dt>Expired on:</dt>
              <dd>${expirationDate}</dd>
            </s:if>
            <s:if test="completionDate != null">
              <dt>Completed on:</dt>
              <dd>${completionDate}</dd>
            </s:if>
              <dt>Requester:</dt>
              <dd>${requesterInfo.name} ${requesterInfo.surname}</dd>
          </dl>
        </td>
        <td>
          <s:if test="#req instanceof org.glite.security.voms.admin.persistence.model.request.GroupMembershipRequest">
            <div class="groupName">
              ${groupName}
            </div>
          </s:if>
          <s:if test="#req instanceof org.glite.security.voms.admin.persistence.model.request.RoleMembershipRequest">
            <div class="userRoleName">
              ${groupName}/Role=${roleName}
            </div>
          </s:if>
          
          <s:if test="#req instanceof org.glite.security.voms.admin.persistence.model.request.CertificateRequest">
            <div class="userDN">
              <voms:formatDN dn="${certificateSubject}" fields="CN"/>
              
            </div>
            <div class="userCA">
              <voms:formatDN dn="${certificateIssuer}" fields="CN"/>
            </div>
          </s:if>
        </td>
        
        <td>
          <div class="userDN">
              <voms:formatDN dn="${approverDN}" fields="CN"/>
              
            </div>
            <div class="userCA">
              <voms:formatDN dn="${approverCA}" fields="CN"/>
            </div>
          
        </td>
        <td>
          <s:set var="reqStatusClass" value="status.toString().toLowerCase()+'Req'"/>
          <div class="<s:property value='#reqStatusClass'/>">
            <s:property value="status"/>
          </div>  
        </td>
      </tr>
    </s:iterator>
  </table>
  </s:else>
</s:else>