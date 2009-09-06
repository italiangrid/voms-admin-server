<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="not requests.empty">

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
        <s:if test="#req instanceof org.glite.security.voms.admin.model.request.GroupMembershipRequest">
          <div class="groupName">
            ${groupName}
          </div>
        </s:if>
        <s:if test="#req instanceof org.glite.security.voms.admin.model.request.RoleMembershipRequest">
          <div class="userRoleName">
            ${roleName}
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