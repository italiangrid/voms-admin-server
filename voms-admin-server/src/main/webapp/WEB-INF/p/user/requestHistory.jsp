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

<s:if test="not requests.empty and #attr.canReadPI">

  <table>
    <tr>
      <th>Request type</th>
      <th>Info</th>
      <th>Status</th>
    </tr>
    
    <s:iterator value="requests" var="req">
      <tr class="tableRow">
      <td style="width: 45%">
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

</s:if>
<s:else>
	No requests found.
</s:else>