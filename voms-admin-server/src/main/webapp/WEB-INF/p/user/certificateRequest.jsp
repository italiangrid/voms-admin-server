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
	
	<table>
        <tr>
          <th colspan="2">Certificate</th>
        </tr>
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
                
                <div class="badge-container">
                  <tiles2:insertTemplate template="suspensionDetail.jsp"/>
                </div>
				
				<div class="cert-operations">
				
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
			<s:token/>
			<s:hidden name="userId" value="%{model.id}" />
			<s:submit value="%{'Add an additional certificate'}" method="input" align="right"/>
		</s:form>
	
	</s:if>
</s:else>

</div>
