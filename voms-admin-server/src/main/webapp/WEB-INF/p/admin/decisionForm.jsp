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

<s:form
  action="decision"
  onsubmit="ajaxSubmit($(this),'pending-req-content'); return false;"
  cssClass="decisionForm"
  theme="simple">
  <s:token />

  <s:hidden
    name="requestId"
    value="%{id}" />

  <s:hidden
    name="decision"
    value="reject" />

  <s:submit
    name="submit"
    value="approve"
    onclick="this.form.decision.value = this.value" />

  <s:submit
    name="submit"
    value="reject"
    onclick="this.form.decision.value = this.value" />
</s:form>