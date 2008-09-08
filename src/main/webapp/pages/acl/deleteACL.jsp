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
Delete <c:if test="${acl.defaultACL}">default</c:if> ACL entry: 
</div>

<html:form action="/ACL" method="post">
	<html:hidden property="method" value="delete"/>
	
	<html:hidden property="aclId" value="${acl.id}"/>
	<html:hidden property="adminId" value="${admin.id}"/>
	
	<table class="table" cellpadding="0" cellspacing="0">		
		<tr>
			<td class="header">Admin:</td>
			<td class="admin">
				<div class="userDN">
					<voms:formatDN dn="${admin.dn}" fields="CN"/>
				</div>
				<div class="userCA">
					<voms:formatDN dn="${admin.ca.dn}" fields="CN"/>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td class="header">Context:</td>
			<td class="highlight">${acl.context}</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td class="header" >Permission:</td>
			<td style="font-weight:bold;">${permission.compactRepresentation}</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><div class="header">Remove also from children contexts?</div></td>
			<td><html:checkbox property="propagated"/></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td/>
			<td><html:submit value="Delete ACL entry!" styleClass="submitButton"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
	</table>
</html:form>