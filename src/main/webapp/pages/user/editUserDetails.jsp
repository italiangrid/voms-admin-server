<%--
 Copyright (c) Members of the EGEE Collaboration. 2006.
 See http://www.eu-egee.org/partners/ for details on the copyright
 holders.

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
     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://org.glite.security.voms.tags" prefix="voms"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>


<div class="deleteUserForm">
	<c:url
		value="/DeleteUser.do"
		var="deleteUserUrl">
	
		<c:param
			name="id"
			value="${vomsUser.id}" />
	</c:url>
	
	<c:url value="javascript:ask_confirm('Delete user: \n ${fn:escapeXml(vomsUser.escapedDn)} \n?', '${deleteUserUrl}', 'User ${fn:escapeXml(vomsUser.escapedDn)} not deleted.')"
						var="deleteUserJSUrl"/>
		
	<voms:link
		context="vo"
		permission="rw"
		href="${deleteUserJSUrl}"
		styleClass="actionLink"
		disabledStyleClass="disabledLink">
		delete this user
	</voms:link>
</div>

<div class="detailsBlock">

	<html:form action="/EditUser" method="post" onsubmit="return validateUserForm(this)">
	<table class="form">
		<colgroup>
				<col class="labels"/>
				<col class="fields"/>
		</colgroup>
		<tr>
			<td>
				<div class="label">
					DN:
				</div>
			</td>
			<td>
				<div class="userDNCA">
					<div class="userDN">${vomsUser.dn}</div>
				</div>
			</td>	
		</tr>
		
		<tr>
			<td>
				<div class="label">
					CA:
				</div>
			</td>
			<td>
				<div class="userDNCA">
					<div class="userCA">${vomsUser.ca.dn}</div>
				</div>
			</td>	
		</tr>

		<%-- 
		<tr>
			<td>
				<div class="label">
					User's common name:
				</div>
			</td>
			<td>
				<html:text property="cn" size="50" styleClass="inputField"
					value="${vomsUser.cn}"/>
			</td>
		</tr>
		--%>
		<tr>
			<td>
				<div class="label">
				Email:
				</div>
			</td>
			<td>
				<div class="userEmailAddress">
				<html:text property="emailAddress" size="50" styleClass="inputField"
					value="${vomsUser.emailAddress}"/>	
				</div>
			</td>
		</tr>
		
		<tr >
			<td/>
			<td>
				<voms:submit context="vo" permission="CONTAINER_WRITE" styleClass="submitButton" value="Save changes"/>
			</td>
		</tr>		
	</table>
	<html:hidden property="id" value="${vomsUser.id}"/>
	<html:hidden property="emailAddress" value="${vomsUser.emailAddress }"/>
	</html:form>
	<div class="separator">&nbsp;</div>
</div>

<html:javascript formName="/EditUser"/>
