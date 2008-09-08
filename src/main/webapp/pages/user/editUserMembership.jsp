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

<div class="membershipBlock">
	
	<c:if test="${fn:length(unsubscribedGroups) > 0}">
		<div class="subscribeGroups">
			<html:form action="/Member" method="post">
				<html:hidden property="method" value="addToGroup"/>
				<html:hidden property="userId" value="${vomsUser.id}"/>
				<html:select property="groupId" styleClass="selectBox">
					<html:options collection="unsubscribedGroups" property="id" labelProperty="name"/>
				</html:select>
				<voms:submit context="vo" permission="CONTAINER_WRITE" styleClass="submitButton" value="Add to group"/>
			</html:form>
		</div>
	</c:if>
	
	<table class="table" cellpadding="0" cellspacing="0">
		
		<tr class="tableHeaderRow">
			<td>Group name</td>
			<td>Roles</td>
			<td/>
		</tr>
		
		<c:forEach var="mapping" items="${vomsUser.mappingsMap}" varStatus="status">
	
			<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
				<td width="40%">
					<div class="group">
						${mapping.key}
					</div>
				</td>
				<td width="50%">
					<div class="roles">
					<c:forEach var="role" items="${mapping.value}" varStatus="status">
						<div class="roleCell" class="${ (status.index) %2 eq 0 ? 'rowEven': 'rowOdd'}">
							<div class="role">
								${role.name}
							</div>
							<div class="action">
							
								<c:url value="/Member.do" var="dismissRoleURL">
									<c:param name="method" value="dismissRole"/>
									<c:param name="userId" value="${vomsUser.id }"/>
									<c:param name="groupId" value="${mapping.key.id}"/>
									<c:param name="roleId" value="${role.id}"/>
								</c:url>
									
								<voms:link 
									href="${dismissRoleURL}"
									context="${mapping.key.name}" 
									styleClass="actionLink" 
									disabledStyleClass="disabledLink" 
									permission="CONTAINER_READ|MEMBERSHIP_READ|MEMBERSHIP_WRITE">
									dismiss role
								</voms:link>
								
							</div> <!--  action -->
						</div> <!--  role cell -->
					</c:forEach>
				
					<voms:unassignedRoles id="unassignedRoles" groupId="${mapping.key.id}" var="unRoles"/>
					<c:if test="${fn:length(unRoles) > 0}">				
						<div class="roleAssign">
							<html:form action="/Member" method="post">
								
								<html:hidden property="method" value="assignRole"/>
								<html:hidden property="userId" value="${vomsUser.id}"/>
								<html:hidden property="groupId" value="${mapping.key.id}"/>									
								
								<html:select property="roleId" styleClass="selectBox">
									<html:options collection="unRoles" property="id" labelProperty="name"/>
								</html:select>
								<voms:submit context="${mapping.key.name}" permission="rw" styleClass="submitButton" value="Assign role"/>
							</html:form>
						</div>
					</c:if>
					</div>
				</td>
				<td width="10%">
					<c:if test="${!mapping.key.rootGroup}">
						<c:url value="/Member.do" var="removeGroupURL">
							<c:param name="method" value="removeFromGroup"/>
							<c:param name="groupId" value="${mapping.key.id}"/>
							<c:param name="userId" value="${vomsUser.id }"/>
						</c:url>
						<voms:link 
							href="${removeGroupURL}"
							context="${mapping.key.name}" 
							styleClass="actionLink" 
							disabledStyleClass="disabledLink" 
							permission="CONTAINER_READ|MEMBERSHIP_READ|MEMBERSHIP_WRITE">
							remove					
						</voms:link>
					</c:if>
				</td>
			</tr>
		</c:forEach>		
	</table><!-- Membership table -->
	<div class="separator">&nbsp;</div>
</div>
