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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<c:choose>
	<c:when test="${empty vomsGroup}">
		No group found in page context!
	</c:when>
	<c:otherwise>
				
		<div id="groupEditPane">
		

				
			<voms:panel id="groupACL" 
				title="ACL management for group ${vomsGroup}"
				panelClass="panel"
				headerClass="panelHeader"
				buttonClass="panelButton"
				titleClass="panelTitle"
				contentClass="panelContent">
				<voms:authorized permission="ACL_READ" context="${vomsGroup}" message="You don't have the necessary rights to list ACLs defined for group ${vomsGroup}.">
					<tiles:insert attribute="groupACL"/>
				</voms:authorized>
			</voms:panel>
				
			
			
			
				
			<voms:panel id="groupMembership" 
					title="Membership details for group ${vomsGroup}"
					panelClass="panel"
					headerClass="panelHeader"
					buttonClass="panelButton"
					titleClass="panelTitle"
					contentClass="panelContent">
					<voms:authorized permission="MEMBERSHIP_READ" context="${vomsGroup}" message="You don't have the necessary rights to list ${vomsGroup} members.">
						<tiles:insert attribute="groupMembership"/>
					</voms:authorized>
			</voms:panel>
				

			
		
			<voms:panel id="groupAttributes" 
				title="Generic attributes management for group ${vomsGroup}"
				panelClass="panel"
				headerClass="panelHeader"
				buttonClass="panelButton"
				titleClass="panelTitle"
				contentClass="panelContent">
					<voms:authorized permission="ATTRIBUTES_READ" context="${vomsGroup}" message="You don't have the necessary rights to list attributes defined for group ${vomsGroup}.">
						<tiles:insert attribute="groupAttributes"/>		
					</voms:authorized>
				</voms:panel>
				
		</div>
		
	</c:otherwise>	
</c:choose>
	
	