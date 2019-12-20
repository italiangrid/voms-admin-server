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

<div class="reloadable">

<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp"/>

<voms:authorized permission="CONTAINER_READ|CONTAINER_WRITE"
	context="vo">
	<div id="add-certificate-link"><s:url action="add-certificate"
		namespace="/user" var="addCertificateURL">
		<s:param name="userId" value="id" />
	</s:url> <s:a href="%{#addCertificateURL}" cssClass="actionLink  ">Add a new certificate</s:a>
	</div>
</voms:authorized>


<s:if test="certificates.empty">
        No certificates defined for this user.
      </s:if>
<s:else>
	<voms:hasPermissions var="canSuspend" context="/${voName}"
		permission="CONTAINER_READ|MEMBERSHIP_READ|SUSPEND" />

	<table>
        <tr>
          <th colspan="2">Certificate</th>
        </tr>
		<s:iterator var="cert" value="certificates">
			<tr class="tableRow">
				<td>
				<div class="userDN"><s:set value="subjectString"
					var="thisCertDN" />${thisCertDN}
				</div>

				<div class="userCA"><s:set value="ca.subjectString"
					var="thisCertCA" />${thisCertCA}
				</div>

                <s:if test="#attr.canReadPI">
  				<div class="cert-date-info">Added on: <span>
                    <s:text name="format.datetime">
                      <s:param
                        value="creationTime" />
                    </s:text></span></div>
                </s:if>
				<div class="badge-container">
                  <tiles2:insertTemplate template="suspensionDetail.jsp"/>
                </div>

				<div class="cert-operations">
				
				<voms:hasPermissions var="canDelete" context="/${voName}"
						permission="CONTAINER_READ|CONTAINER_WRITE|MEMBERSHIP_READ|MEMBERSHIP_WRITE" />
				
                <s:if
					test="#attr.canSuspend and not suspended">

					<s:form action="suspend-certificate" namespace="/user"
						cssClass="cert-operation-forms" onsubmit="ajaxSubmit(this,'cert-info-content');" method="post">
						<s:token />
						<s:hidden name="userId" value="%{model.id}" />
						<s:hidden name="certificateId" value="%{#cert.id}" />
						<s:submit value="%{'Suspend'}" onclick=" return openSuspendDialog(this, 'suspendCertificateDialog','%{#cert.subjectString}');" />
					</s:form>
				</s:if> <s:if test="#attr.canSuspend and suspended">

					<s:if test="not user.suspended">
						<s:form action="restore-certificate" namespace="/user"
							cssClass="cert-operation-forms" onsubmit="ajaxSubmit(this,'cert-info-content'); return false;">
							<s:token />
							<s:hidden name="userId" value="%{model.id}" />
							<s:hidden name="certificateId" value="%{#cert.id}" />
							<s:submit value="%{'Restore'}" />
						</s:form>
					</s:if>

				</s:if> 
				
				<s:if test="model.certificates.size > 1">
				
				
					<s:if test="#attr.canDelete">
						<s:form action="delete-certificate" namespace="/user"
							theme="simple" cssClass="cert-operation-forms">
							<s:token />
							<s:hidden name="userId" value="%{model.id}" />
							<s:hidden name="certificateId" value="%{#cert.id}" />
							<s:submit value="%{'Delete'}" onclick="confirmRemoveCertificateDialog(this, '%{#cert.subjectString}', '%{#cert.ca.subjectString}', '%{model.shortName}'); return false;"/>
						</s:form>
					</s:if>
				</s:if>
				
				</div>

				</td>
			</tr>
		</s:iterator>
	</table>
</s:else>

</div>