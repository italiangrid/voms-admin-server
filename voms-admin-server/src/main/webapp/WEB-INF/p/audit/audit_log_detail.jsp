<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015

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