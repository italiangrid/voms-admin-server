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
<div id="hr-results">
  <s:form action="resolve-hr-id" namespace="/register/hr" onsubmit="ajaxSubmit(this,'hr-results'); return false;">
    <ul class="form">
      <li>
        <s:textfield name="emailAddress" value="%{requester.emailAddress}" size="60"
          label="%{'Your CERN HR DB email address'}" cssClass="registrationField" />
      </li>
      <s:submit />
    </ul>
  </s:form>
  <div class="reloadable">
    <tiles2:insertTemplate template="../../shared/errorsAndMessages.jsp" />
    <s:if test="model.size > 0">
      <table>
        <thead>
          <tr>
            <th>CERN Person ID</th>
            <th>Name</th>
            <th>Surname</th>
            <th>Institute</th>
            <th>Email addresses</th>
            <th />
          </tr>
        </thead>
        <tbody>
          <s:iterator value="model">
            <tr>
              <td>
                <s:property value="id" />
              </td>
              <td>
                <s:property value="firstName" />
              </td>
              <td>
                <s:property value="name" />
              </td>
              <td>
                <s:property value="findValidParticipationForExperiment(now,experimentName).get().institute.name" default="N/A" />
              </td>
              <td>
                <div>
                  <s:property value="physicalEmail" />
                </div>
                <div>
                  <s:property value="email" />
                </div>
              </td>
              <td>
                <s:form namespace="/register/hr" action="start">
                  <s:submit value="%{'It\\'s me!'}" />
                </s:form>
              </td>
            </tr>
          </s:iterator>
        </tbody>
      </table>
    </s:if>
  </div>
</div>