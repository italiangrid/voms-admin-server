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
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<div class="header1">
Add an acl entry to <c:if test="${acl.defaultACL}">default</c:if> ACL for context: <span class="highlight">${context}</span>.
</div>

<html:form action="/AddACLEntry" method="post" onsubmit="return validateAddACLEntryForm(this)">
	<html:hidden property="aclId" value="${acl.id}"/>
	<html:hidden property="method" value="addEntry"/>

	<table class="form" cellspacing="5">
		<tr>
			<td colspan="3">
				<div class="header3">
					Add an ACL entry for:
				</div>
			</td>
		</tr>
		<tr>
			<td><html:radio property="entryKind" value="voUser" disabled="${empty users}">The VO user:</html:radio></td>
			<td>
				<html:select property="userId" styleClass="selectBox" disabled="${empty users}" style="font-size:smaller">
					<html:options collection="users"  property="id" labelProperty="cn"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td><html:radio property="entryKind" value="nonVoUser">a non-VO user:</html:radio></td>
			<td>
				<table>
					<tr>
						<td>DN:</td>
						<td>
							<html:text property="dn" styleClass="inputField" size="25"/>
						</td>
					</tr>
					<tr>
						<td>CA:</td>
						<td>
							<html:select property="caId" styleClass="selectBox" style="font-size: smaller">
								<html:options collection="cas"  property="id" labelProperty="dn"/>
							</html:select>
						</td>
					</tr>
				</table>
			</td>		
		</tr>
		
		<tr>
			<td><html:radio property="entryKind" value="role" disabled="${empty roles}"/>Anyone with</td>
			<td>
				<html:select property="roleId" styleClass="selectBox" disabled="${empty roles}">
					<html:options collection="roles" property="id" labelProperty="name"/>
				</html:select>  role in group: 
				<html:select property="roleGroupId" styleClass="selectBox" disabled="${empty roles}">
					<html:options collection="groups" property="id" labelProperty="name"/>
				</html:select>.
			</td>
		</tr>
		
		<tr>
			<td><html:radio property="entryKind" value="group"/>Members of the</td>
			<td>
				<html:select property="groupId" styleClass="selectBox">
					<html:options collection="groups" property="id" labelProperty="name"/>
				</html:select>
				 group.
			</td>
		</tr>
		
		<tr>
			<td><html:radio property="entryKind" value="tag" disabled="${empty tags }"/>Administrator belonging to the </td>
			<td>
				<html:select property="tagId" styleClass="selectBox">
					<html:options collection="tags" property="id" labelProperty="dn"/>
				</html:select>
				 tag.
			</td>
		</tr>
		
		<tr>
			<td colspan="2"><html:radio property="entryKind" value="anyAuthenticatedUser"/>Anyone</td>
		</tr>
	</table>
		
	<table class="form" cellpadding="0" cellspacing="0">	
		<tr>
			<td colspan="4">
				<div class="header2">
					granting the following permissions:
				</div>
			</td>
		<tr>
			<td>
			<td>Read</td>
			<td>Write</td>
			<td/>
		</tr>
		<tr>
			<td>Container rights:</td>
			<td><html:multibox value="CONTAINER_READ" property="selectedPermissions"/></td>
			<td><html:multibox value="CONTAINER_WRITE" property="selectedPermissions"/></td>
			<td/>
		</tr>
		<tr>
			<td/>
			<td>Read</td>
			<td>Write</td>
			<td/>
		</tr>
		<tr>
			<td>Membership rights:</td>
			<td><html:multibox value="MEMBERSHIP_READ" property="selectedPermissions"/></td>
			<td><html:multibox value="MEMBERSHIP_WRITE" property="selectedPermissions"/></td>
			<td/>
		</tr>
		<tr>
			<td/>
			<td>List</td>
			<td>Set</td>
			<td>Default</td>
		</tr>
		<tr>
			<td>ACL management rights:</td>
			<td><html:multibox value="ACL_READ" property="selectedPermissions"/></td>
			<td><html:multibox value="ACL_WRITE" property="selectedPermissions"/></td>
			<td><html:multibox value="ACL_DEFAULT" property="selectedPermissions"/></td>
		</tr>
		<tr>
			<td/>
			<td>List</td>
			<td>Define</td>
			<td/>
		</tr>
		<tr>
			<td>Generic Attributes rights:</td>
			<td><html:multibox value="ATTRIBUTES_READ" property="selectedPermissions"/></td>
			<td><html:multibox value="ATTRIBUTES_WRITE" property="selectedPermissions"/></td>
			<td/>
		</tr>
		<tr>
			<td/>
			<td>List</td>
			<td>Manage</td>
			<td/>
		</tr>
		<tr>
			<td>Subscription management rights:</td>
			<td><html:multibox value="REQUESTS_READ" property="selectedPermissions"/></td>
			<td><html:multibox value="REQUESTS_WRITE" property="selectedPermissions"/></td>
			<td/>
		</tr>	
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		
		<c:if test="${not acl.defaultACL}">
			<tr>
				<td><div class="header">Propagate entry to children contexts</div></td>
				<td><html:checkbox property="propagated"/></td>
			</tr>
			<tr>
				<td colspan="4">&nbsp;</td>
			</tr>
		</c:if>
		
		<tr>
			<td></td>
			<td><html:submit value="Submit!" styleClass="submitButton"/></td>
		</tr>
		
	</table>
</html:form>

<html:javascript formName="/AddACLEntry"/>
