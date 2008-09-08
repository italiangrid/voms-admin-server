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

<div id="searchMembersPane">

	<html:form action="/SearchRoleMembers" onsubmit="return validateMembershipSearchForm(this)">
		<html:hidden property="firstResults" value="0"/>
		<html:hidden property="roleId" value="${vomsRole.id}"/>
	
		Search users with role '${vomsRole.name}' in group
		<html:select property="groupId" styleClass="selectBox" value="${vomsGroup.id}">
			<html:options collection="allGroups" property="id" labelProperty="name"/>
		</html:select>
		<html:text property="text" styleClass="inputField" size="20" value="${searchResults.searchString}"/>
		<voms:submit context="vo" 
					permission="r" 
					styleClass="submitButton" 
					value="Search!"/>
	</html:form>
</div>


<c:choose>
<c:when test="${! empty searchResults.results}">


	<div class="resultsHeader">
		<voms:searchNavBar 
			context="vo" 
			permission="r" 
			disabledLinkStyleClass="disabledLink"
			id="searchResults"
			linkStyleClass="navBarLink"
			searchURL="/SearchRoleMembers.do?groupId=${vomsGroup.id}&roleId=${vomsRole.id}"
			styleClass="resultsCount"/>
	</div> <!-- resultsHeader -->
	
	<table class="table" cellpadding="0" cellspacing="0">
	
		<tr class="tableHeaderRow">
				<td>Member DN &amp; CA</td>
				<td/>
		</tr>
	
		<c:forEach var="member" items="${searchResults.results}" varStatus="status">
			<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
				<td>
					<div class="userDN">
						<div class="actions">
							<c:url value="/PreEditUser.do" var="editMemberUrl">
								<c:param name="id" value="${member.id}"/>
							</c:url>
							<voms:link 
								href="${editMemberUrl}"
								context="vo" 
								styleClass="actionLink" 
								disabledStyleClass="disabledLink" 
								permission="r">
								
								<voms:formatDN dn="${member.dn}" fields="CN"/>
					
							</voms:link>
						</div>
					</div>
					<div class="userCA">
						<voms:formatDN dn="${member.ca.dn}" fields="CN,O"/>
					</div>
				</td>
				<td>
					<div class="actions">
				
						<c:url value="/RoleMember.do" var="dismissRoleUrl">
							<c:param name="method" value="dismissRole"/>
							<c:param name="userId" value="${member.id}"/>
							<c:param name="groupId" value="${vomsGroup.id}"/>
							<c:param name="roleId" value="${vomsRole.id}"/>
						</c:url>

						<voms:link 
							href="${dismissRoleUrl}"
							context="${vomsGroup.name}" 
							styleClass="actionLink" 
							disabledStyleClass="disabledLink" 
							permission="rw">
							dismiss this role 					
						</voms:link>			
					</div>
				</td>
			</tr>
		</c:forEach>
	</table>
</c:when>

<c:otherwise>
	<c:choose>
			<c:when test="${fn:length(searchResults.searchString) > 0}">
				<div class="header2">
					No members with role '${vomsRole.name}' in group '${vomsGroup}' match "${searchResults.searchString}".
				</div>
			</c:when>
			<c:otherwise>
				<div class="header2">
					No members have role '${vomsRole.name}' in group '${vomsGroup}'.
				</div>
			</c:otherwise>
	</c:choose>
</c:otherwise>

</c:choose>

<html:javascript formName="/SearchRoleMembers"/>