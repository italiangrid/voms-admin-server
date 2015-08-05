<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp" />
<s:if test="pendingRequests.empty">
  No pending requests.
</s:if>
<s:else>
  <s:set
    value="pendingRequests.size()"
    var="pendingRequestSize" />
  <s:form
    id="requestsHandlingForm"
    action="bulk-decision"
    namespace="/admin">
    <s:token />
    <table class="table" style="clear: both">
      <thead>
        <tr class="req-toolbar">
          <th><input
            type="checkbox"
            id="req-selector"></th>
          <th>Pending requests <span class="badge"><s:property
                value="#pendingRequestSize" /></span></th>
          <th>
            <div class="req-toolbar-btns req-toolbar-btns-hidden">
              <s:submit
                name="decision"
                value="approve"
                cssClass="btn-bulk-req"
                theme="simple" />
              <s:submit
                name="decision"
                value="reject"
                cssClass="btn-bulk-req"
                theme="simple"
                action="bulk-reject"
                onclick="rejectRequestDialog(this,'confirmRejectedRequestDialog'); return false;" />
            </div>
          </th>
        </tr>
      </thead>
      <tbody>
        <s:iterator
          value="pendingRequests"
          var="request">
          <tr class="req-row req-row-noselect">
            <td class="req-selector-col"><s:checkbox
                name="requestIds"
                fieldValue="%{id}"
                cssClass="req-checkbox"
                theme="simple"
                value="%{'false'}" /></td>
            <td colspan="2">
              <div class="req-content">
                <div class="req-status">
                  <tiles2:insertTemplate template="requestIcon.jsp" />
                </div>
                <tiles2:insertTemplate template="requestor.jsp" />
                <tiles2:insertTemplate template="requestInfo.jsp" />
                <a
                  href="#"
                  class="req-detail-trigger">more info</a>
              </div>

              <div class="req-controls">
                <s:url
                  action="bulk-decision"
                  var="reqApproveURL">
                  <s:param
                    name="requestIds"
                    value="%{#request.id}" />
                  <s:param
                    name="decision"
                    value="%{'approve'}" />
                </s:url>
                <s:url
                  action="bulk-reject"
                  var="reqRejectURL">
                  <s:param
                    name="requestIds"
                    value="%{#request.id}" />
                  <s:param
                    name="decision"
                    value="%{'reject'}" />
                </s:url>
                <div class="req-btns">
                  <button
                    type="submit"
                    formaction="${reqApproveURL}"
                    class="btn-req">approve</button>
                  <button
                    id="cancel_req_${request.id}"
                    type="submit"
                    class="hidden"
                    formaction="${reqRejectURL}"></button>
                  <button
                    type="submit"
                    formaction="${reqRejectURL}"
                    class="btn-req"
                    onclick="rejectSingleRequestDialog('#cancel_req_${request.id}','confirmRejectedRequestDialog'); return false;">
                    reject</button>
                </div>
              </div>
              <div class="req-detail req-detail-hidden">
                <tiles2:insertTemplate template="requestDetail.jsp" />
              </div>
            </td>
          </tr>
        </s:iterator>
      </tbody>
    </table>
  </s:form>
</s:else>