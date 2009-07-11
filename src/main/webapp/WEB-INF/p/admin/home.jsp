<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>Pending requests</h1>




<s:if test="pendingRequests.empty">
  No pending request found.
</s:if>
<s:else>
  <table class="table">
  
    <s:iterator value="pendingRequests" var="request">
    <tr>
      <td>
        <s:property value="typeName"/>
      </td>
      <td>
        <div class="userDN">
          <s:property value="requesterInfo.certificateSubject"/>
        </div>
        <div class="userCA">
          <s:property value='requesterInfo.certificateIssuer'/>
        </div>
      </td>
      <td>
        <div>
          <s:url action="decision" var="requestDetailURL" method="input" namespace="/request">
            <s:param name="requestId" value="id"/>
          </s:url> 
          <s:a href="%{requestDetailURL}">
            request details
          </s:a>
        </div>
      </td>
      <tr>
    </s:iterator>
  </table>
</s:else>