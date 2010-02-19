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

<div class="reloadable">
<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp"/>

<s:if test="certificates.empty">
	No certificates defined for this user.
</s:if>

<s:else>
	
	<table cellpadding="0" cellspacing="0">
		<s:iterator var="cert" value="certificates">
			<tr class="tableRow">
				<td>
				<div class="userDN"><s:set value="subjectString"
					var="thisCertDN" /> <voms:formatDN dn="${thisCertDN}" fields="CN" />
				</div>

				<div class="userCA"><s:set value="ca.subjectString"
					var="thisCertCA" /> <voms:formatDN dn="${thisCertCA}" fields="CN" />
				</div>

				<div class="cert-date-info">Added on: <span>
                  <s:text name="format.datetime">
                    <s:param
                      value="creationTime" />
                  </s:text></span></div>

				<div class="cert-status-info"><s:if test="suspended">
					<span> Suspended: </span>

					<span class="suspensionReason"> <s:property
						value="suspensionReason" /> </span>
				</s:if></div>

				<div class="cert-operations"><s:if
					test="#attr.canSuspend and not suspended">

					<s:form action="suspend-certificate" namespace="/user"
						theme="simple" cssClass="cert-operation-forms" method="input">
						<s:token />
						<s:hidden name="userId" value="%{model.id}" />
						<s:hidden name="certificateId" value="%{#cert.id}" />
						<s:submit value="%{'Suspend'}" />
					</s:form>
				</s:if> <s:if test="#attr.canSuspend and suspended">

					<s:if test="not user.suspended">
						<s:form action="restore-certificate" namespace="/user"
							theme="simple" cssClass="cert-operation-forms">
							<s:token />
							<s:hidden name="userId" value="%{model.id}" />
							<s:hidden name="certificateId" value="%{#cert.id}" />
							<s:submit value="%{'Restore'}" />
						</s:form>
					</s:if>

				</s:if> 
				
				<s:if test="model.certificates.size > 1">
				
					<voms:hasPermissions var="canDelete" context="/${voName}"
						permission="CONTAINER_READ|CONTAINER_WRITE|MEMBERSHIP_READ|MEMBERSHIP_WRITE" />
					<s:if test="#attr.canDelete">
						<s:form action="delete-certificate" namespace="/user"
							theme="simple" cssClass="cert-operation-forms">
							<s:token />
							<s:hidden name="userId" value="%{model.id}" />
							<s:hidden name="certificateId" value="%{#cert.id}" />
							<s:submit value="%{'Delete'}" />
						</s:form>
					</s:if>
				</s:if>
				
				</div>

				</td>
			</tr>
		</s:iterator>
		
		
		
		<s:iterator var="req" value="pendingCertificateRequests">
			<tr class="tableRow">
				<td>
				<div class="waitingForApproval" style="float: right">(Waiting for approval)</div>
				<div class="requestedDN"><voms:formatDN dn="${req.certificateSubject}" fields="CN" />
				</div>

				<div class="requestedCA"><voms:formatDN dn="${req.certificateIssuer}" fields="CN" />
					
				</div>

				<div class="cert-date-info">Requested on: <span>
                  <s:text name="format.datetime">
                    <s:param
                      value="creationDate" />
                  </s:text></span>
                 </div>
                 </td>
             </tr>
		</s:iterator>
		
	</table>
	<s:if test="#request.registrationEnabled">
	
		<s:form action="request-certificate" namespace="/user">
			<s:hidden name="userId" value="%{model.id}" />
			<s:submit value="%{'Request new certificate'}" method="input" align="right"/>
		</s:form>
	
	</s:if>
</s:else>

</div>
