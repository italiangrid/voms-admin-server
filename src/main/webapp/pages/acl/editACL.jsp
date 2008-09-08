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
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<div class="header1">
Edit <c:if test="${acl.defaultACL}">default</c:if> ACL entry:
</div>

<html:form action="/ACL" method="post">
	<html:hidden property="method" value="set"/>
	
	<html:hidden property="aclId" value="${acl.id}"/>
	<html:hidden property="adminId" value="${admin.id}"/>
	
	<table class="form" cellpadding="0" cellspacing="0">
		<COLGROUP>
			<COL width="60%"/>
		</COLGROUP>
		<tr>
			<td class="header">Admin:</td>
			<td class="admin" colspan = "2">
				<div class="userDN">
					<voms:formatDN dn="${admin.dn}" fields="CN"/>
				</div>
				<div class="userCA">
					<voms:formatDN dn="${admin.ca.dn}" fields="CN"/>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td class="header">Context:</td>
			<td class="highlight" colspan = "2">${acl.context}</td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td>
			<td>Read</td>
			<td>Write</td>
			<td/>
		</tr>
		<tr>
			<td class="header">Container rights:</td>
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
			<td class="header">Membership rights:</td>
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
			<td class="header">ACL management rights:</td>
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
			<td class="header">Generic Attributes rights:</td>
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
			<td class="header">Subscription management rights:</td>
			<td><html:multibox value="REQUESTS_READ" property="selectedPermissions"/></td>
			<td><html:multibox value="REQUESTS_WRITE" property="selectedPermissions"/></td>
			<td/>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr>
			<td><div class="header">Propagate to children contexts?</div></td>
			<td><html:checkbox property="propagated"/></td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr>
			<td></td>
			<td> <html:submit value="submit!" styleClass="submitButton"/>
			</td>
		</tr>

	</table>
</html:form>

