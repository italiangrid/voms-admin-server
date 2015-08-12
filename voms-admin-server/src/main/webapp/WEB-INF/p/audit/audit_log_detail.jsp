<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h4>Audit log entry</h4>

<dl class="al-dtl-list">
  <dt>Event type</dt>
  <dd>${event.type}</dd>
  <dt>ID</dt>
  <dd>${event.id}</dd>
  <dt>Principal</dt>
  <dd>${event.principal}</dd>
  <dt>Timestamp</dt>
  <dd>${request.timestamp}</dd>
</dl>

<h4>Audit event data</h4>

<dl class="al-dtl-list">
<s:iterator value="#event.sortedData">
  <dt><s:property value="name"/></dt>
  <dd>
    <s:property value="value"/>
  </dd>
</s:iterator>
</dl>