<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div class="reloadable"><s:if
  test="pendingRequests.empty">
  No pending requests found.
</s:if> <s:else>
<tiles2:insertTemplate
  template="../shared_20/errorsAndMessages.jsp" /> 
  <s:if
    test="not pendingRequests.{? #this instanceof org.glite.security.voms.admin.model.request.NewVOMembershipRequest}.empty">
    <h1>VO membership requests:</h1>
    <table>
      <tr>
        <th>Requester</th>
        <th>Personal Info</th>
        <th/>
      </tr>

      <s:iterator
        value="pendingRequests.{? #this instanceof org.glite.security.voms.admin.model.request.NewVOMembershipRequest}">
        <tr>
          <td>
            <tiles2:insertTemplate
            template="userInfo.jsp"
            flush="true" />
          </td>
            
            
          <td class="personalInfo">
          <dl>
            <dt>Address:</dt>
            <dd>${requesterInfo.address}</dd>
            <dt>Phone number:</dt>
            <dd>${requesterInfo.phoneNumber}</dd>
            <dt>Email:</dt>
            <dd>${requesterInfo.emailAddress}</dd>
          </dl>
          </td>
          
          <td style="vertical-align: bottom; text-align: right;"><s:form
            action="decision">
            <s:hidden
              name="requestId"
              value="%{id}" />
            <s:radio
              list="{'approve','reject'}"
              name="decision"
              onchange="ajaxSubmit($(this).closest('form'),'pending-req-content'); return false;" />
          </s:form></td>
        </tr>
      </s:iterator>
    </table>
  </s:if>

  <s:if
    test="not pendingRequests.{? #this.typeName == 'Group membership request'}.empty">
    <h1>Group membership requests:</h1>


    <table>
      <tr>
        <th>Requester</th>
        <th>Requested group</th>
        <th />
      </tr>
      <s:iterator
        value="pendingRequests.{? #this.typeName == 'Group membership request'}"
        var="groupRequest">
        <tr class="tableRow">
          <td style="width: 40%"><tiles2:insertTemplate
            template="userInfo.jsp"
            flush="true" /></td>
          <td>
          <div class="groupName"><s:property value="groupName" /></div>

          </td>

          <td style="vertical-align: bottom; text-align: right"><s:form
            action="decision">
            <s:hidden
              name="requestId"
              value="%{id}" />
            <s:radio
              list="{'approve','reject'}"
              name="decision"
              onchange="ajaxSubmit($(this).closest('form'),'pending-req-content'); return false;" />
          </s:form></td>
        </tr>
      </s:iterator>
    </table>
  </s:if>

  <s:if
    test="not pendingRequests.{? #this.typeName == 'Role membership request'}.empty">
    <h3>Role membership requests:</h3>
    <table>
      <tr>
        <th>Requester</th>
        <th>Requested role</th>
        <th />
      </tr>
      <s:iterator
        value="pendingRequests.{? #this.typeName == 'Role membership request'}"
        var="roleRequest">
        <tr class="tableRow">
          <td style="width: 40%"><tiles2:insertTemplate
            template="userInfo.jsp"
            flush="true" /></td>
          <td>
          <div class="userRoleName"><s:property value="groupName" />/<s:property
            value="roleName" /></div>

          </td>

          <td style="vertical-align: bottom; text-align: right"><s:form
            action="decision">
            <s:hidden
              name="requestId"
              value="%{id}" />
            <s:radio
              list="{'approve','reject'}"
              name="decision"
              onchange="ajaxSubmit($(this).closest('form'),'pending-req-content'); return false;" />
          </s:form></td>
        </tr>
      </s:iterator>
    </table>
  </s:if>
  <s:if
    test="not pendingRequests.{? #this instanceof org.glite.security.voms.admin.model.request.MembershipRemovalRequest }.empty">
    <h3>Membership removal requests:</h3>
    <table>
      <tr>
        <th>Requester</th>
        <th>Reason</th>
        <th/>
      </tr>
      <s:iterator
        value="pendingRequests.{? #this instanceof org.glite.security.voms.admin.model.request.MembershipRemovalRequest}"
        var="membershipRemovalReq">
        <tr class="tableRow">
          <td style="width: 40%"><tiles2:insertTemplate
            template="userInfo.jsp"
            flush="true" /></td>
          <td>
            <s:property value="reason"/>
          </td>
          <td style="vertical-align: bottom; text-align: right"><s:form
            action="decision">
            <s:hidden
              name="requestId"
              value="%{id}" />
            <s:radio
              list="{'approve','reject'}"
              name="decision"
              onchange="ajaxSubmit($(this).closest('form'),'pending-req-content'); return false;" />
          </s:form></td>
        </tr>
      </s:iterator>
    </table>
 </s:if>
  
</s:else></div>