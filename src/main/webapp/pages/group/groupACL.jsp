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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>

<div class="header2">
	Access control list:
</div>
<div id="addACLEntryBox">

	<c:url value="/PreAddACLEntry.do" var="addACLEntryURL">
		<c:param name="method" value="preAddEntry"/>
		<c:param name="aclId" value="${vomsGroup.ACL.id}"/>
	</c:url>
	<voms:link
			permission="ACL_READ|ACL_WRITE"
			disabledStyleClass="disabledLink"
			styleClass="vomsLink"
			context="${vomsGroup}"
			href="${addACLEntryURL}">
			Add entry
	</voms:link>
</div>

<table class="table" cellpadding="0" cellspacing="0">
	<tr class="tableHeaderRow">
		<td>Admin DN &amp; CA</td>
		<td>Container</td>
		<td>Membership</td>
		<td>ACL</td>
		<td>Attributes</td>
		<td>Requests</td>
		<td colspan="2"/>
	</tr>
	<c:forEach var="permission" items="${vomsGroup.ACL.externalPermissions}" varStatus="status">
		
		<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
			<td width="40%" class="admin">
				<div class="userDN">
					<voms:formatDN dn="${permission.key.dn}" fields="CN"/>
				</div>
				<div class="userCA">
					<voms:formatDN dn="${permission.key.ca.dn}" fields="CN"/>
				</div>
			</td>
			<voms:printPermission/>
			<td class="actions">
				<c:url value="/ACL.do" var="editACLUrl">
					<c:param name="method" value="load"/>
					<c:param name="aclId" value="${vomsGroup.ACL.id}"/>
					<c:param name="adminId" value="${permission.key.id}"/>
				</c:url>
				<voms:link
					permission="ACL_READ|ACL_WRITE"
					disabledStyleClass="disabledLink"
					styleClass="vomsLink"
					context="${vomsGroup}"
					href="${editACLUrl}">
					edit
				</voms:link>
			</td>
			<td class="actions">
				<c:url value="/ACL.do" var="deleteACLUrl">
					<c:param name="method" value="preDelete"/>
					<c:param name="aclId" value="${vomsGroup.ACL.id}"/>
					<c:param name="adminId" value="${permission.key.id}"/>
				</c:url>
				<voms:link
					permission="ACL_READ|ACL_WRITE"
					disabledStyleClass="disabledLink"
					styleClass="vomsLink"
					context="${vomsGroup}"
					href="${deleteACLUrl}">
					delete
				</voms:link>				
			</td>
		</tr>
	</c:forEach>
</table>

<div class="separator">
&nbsp;
</div>

<div class="header2">
	Default Access control list:
</div>

<div id="addDefaultACLEntryBox">

	<c:url value="/PreAddACLEntry.do" var="addACLEntryURL">
		<c:param name="method" value="preAddEntry"/>
		<c:choose>
			<c:when test="${empty vomsGroup.defaultACL}">
				<c:param name="groupId" value="${vomsGroup.id}"/>				
			</c:when>
			<c:otherwise>
				<c:param name="aclId" value="${vomsGroup.defaultACL.id}"/>	
			</c:otherwise>
		</c:choose>		
	</c:url>
	<voms:link
			permission="ACL_READ|ACL_WRITE|ACL_DEFAULT"
			disabledStyleClass="disabledLink"
			styleClass="vomsLink"
			context="${vomsGroup}"
			href="${addACLEntryURL}">
			Add entry
	</voms:link>
</div>

<c:choose>
	<c:when test="${!empty vomsGroup.defaultACL}">
		<table class="table" cellpadding="0" cellspacing="0">
			<tr class="tableHeaderRow">
				<td>Admin DN &amp; CA</td>
				<td>Container</td>
				<td>Membership</td>
				<td>ACL</td>
				<td>Attributes</td>
				<td>Requests</td>
				<td colspan="2"/>
			</tr>
			<c:forEach var="permission" items="${vomsGroup.defaultACL.externalPermissions}" varStatus="status">
		
				<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
					<td width="40%" class="admin">
						<div class="userDN">
							<voms:formatDN dn="${permission.key.dn}" fields="CN"/>
						</div>
						<div class="userCA">
							<voms:formatDN dn="${permission.key.ca.dn}" fields="CN"/>
						</div>
					</td>
					<voms:printPermission/>
					<td class="actions">
						<c:url value="/ACL.do" var="editACLUrl">
							<c:param name="method" value="load"/>
							<c:param name="aclId" value="${vomsGroup.defaultACL.id}"/>
							<c:param name="adminId" value="${permission.key.id}"/>
						</c:url>
						<voms:link
							permission="ACL_READ|ACL_WRITE|ACL_DEFAULT"
							disabledStyleClass="disabledLink"
							styleClass="vomsLink"
							context="${vomsGroup}"
							href="${editACLUrl}">
							edit
						</voms:link>
					</td>
					<td class="actions">
						<c:url value="/ACL.do" var="editACLUrl">
							<c:param name="method" value="delete"/>
							<c:param name="groupId" value="${vomsGroup.id}"/>
							<c:param name="aclId" value="${vomsGroup.defaultACL.id}"/>
							<c:param name="adminId" value="${permission.key.id}"/>
						</c:url>
						<voms:link
							permission="ACL_READ|ACL_WRITE|ACL_DEFAULT"
							disabledStyleClass="disabledLink"
							styleClass="vomsLink"
							context="${vomsGroup}"
							href="${editACLUrl}">
							delete
						</voms:link>				
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:when>
	<c:otherwise>
		<div style="clear:both;">
			Default acl not defined for this group.
		</div>
	</c:otherwise>
</c:choose>