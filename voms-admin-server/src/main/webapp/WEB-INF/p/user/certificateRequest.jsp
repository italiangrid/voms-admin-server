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

<div class="reloadable">
  <tiles2:insertTemplate template="../shared/errorsAndMessages.jsp" />

  <s:if test="certificates.empty">
	No certificates defined for this user.
</s:if>

  <s:else>

    <table>
      <tr>
        <th colspan="2">Certificate</th>
      </tr>

      <s:iterator
        var="cert"
        value="certificates">
        <tr class="tableRow">
          <td>
            <div class="userDN">${subjectString}</div>

            <div class="userCA">${ca.subjectString}</div>

            <div class="cert-date-info">
              Added on: <span> <s:text name="format.datetime">
                  <s:param value="creationTime" />
                </s:text></span>
            </div> <tiles2:insertTemplate template="suspensionDetail.jsp" />


            <div class="cert-operations">
              <s:if test="not currentAdminIsCertificate(#cert)">
                <s:form action="remove-own-certificate">
                  <%-- <s:token /> --%>
                  <s:hidden
                    name="certificateId"
                    value="%{#cert.id}" />
                  <s:submit
                    value="%{'Remove this certificate'}"
                    align="right"
                    onclick="confirmRemoveOwnCertificateDialog(this, '%{#cert.subjectString}', '%{#cert.ca.subjectString}'); return false" />
                </s:form>
              </s:if>
            </div>
          </td>
        </tr>
      </s:iterator>

      <s:iterator
        var="req"
        value="pendingCertificateRequests">
        <tr class="tableRow">
          <td>
            <div
              class="waitingForApproval"
              style="float: right">(Waiting for approval)</div>
            <div class="requestedDN">${req.certificateSubject}</div>

            <div class="requestedCA">${req.certificateIssuer}</div>

            <div class="cert-date-info">
              Requested on: <span> <s:text name="format.datetime">
                  <s:param value="creationDate" />
                </s:text></span>
            </div>
          </td>
        </tr>
      </s:iterator>

    </table>
    <s:if test="#request.registrationEnabled">

      <s:form
        action="request-certificate"
        namespace="/user">
        <s:token />
        <s:hidden
          name="userId"
          value="%{model.id}" />
        <s:submit
          value="%{'Add an additional certificate'}"
          method="input"
          align="right" />
      </s:form>

    </s:if>
  </s:else>

</div>
