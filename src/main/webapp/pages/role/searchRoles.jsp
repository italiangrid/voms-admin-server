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

<div id="searchRolesPane">
	<html:form action="/SearchRoles" method="post" onsubmit="return validateSearchForm(this)">
		<html:text property="text" styleClass="inputField" size="20" value="${searchResults.searchString}"/>
		<html:hidden property="firstResults" value="0"/>
		<voms:submit context="vo" permission="CONTAINER_READ" styleClass="submitButton" value="Search roles"/>
	</html:form>
</div>

<voms:authorized permission="CONTAINER_WRITE" context="vo">
	<div id="createRolePane">
		<html:link styleClass="vomsLink" action="/PreCreateRole">Create a new role</html:link>
	</div>
</voms:authorized>

<div id="searchRolesResultsPane">
<c:choose>
	<c:when test="${! empty searchResults.results}">

			<div class="header2">
			Roles:
			</div>
					
			<table class="table" cellpadding="0" cellspacing="0">
		
				<c:forEach var="vomsRole" items="${searchResults.results}" varStatus="status">
				<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
					<td class="tableCell" width="90%">
						<c:url value="/LoadRole.do" var="editRoleUrl">
							<c:param name="id" value="${vomsRole.id}"/>
						</c:url>

						<voms:link
							context="vo"
							permission="r"
							href="${editRoleUrl}"
							styleClass="actionLink"
							disabledStyleClass="disabledLink"
						>
							${vomsRole.name }
						</voms:link	>
					</td>
					<td class="tableCell" width="5%">
						<div class="actions">
							<c:url value="/Role.do" var="deleteRoleUrl">
								<c:param name="method" value="deleteRole"/>
								<c:param name="id" value="${vomsRole.id}"/>
							</c:url>
							<voms:link
								context="vo"
								permission="rw"
								href="javascript:ask_confirm('Delete role ${vomsRole.name}?','${deleteRoleUrl}','Role ${vomsRole.name } not deleted.')"
								styleClass="actionLink"
								disabledStyleClass="disabledLink"
							>
							delete
							</voms:link>
						</div>
					</td>
				</tr>
				</c:forEach>
			</table>
			<div class="resultsFooter">
				<voms:searchNavBar 
					context="vo" 
					permission="r" 
					disabledLinkStyleClass="disabledLink"
					id="searchResults"
					linkStyleClass="navBarLink"
					searchURL="/SearchRoles.do"
					styleClass="resultsCount"
				/>	
			</div> <!-- resultsFooter -->	
	</c:when>
		
	<c:otherwise>
		<c:choose>
				<c:when test="${fn:length(searchResults.searchString) > 0}">
					<div class="header2">
						No roles found matching "${searchResults.searchString}".
					</div>
				</c:when>
				<c:otherwise>
					<div class="header2">
						No roles defined in this vo.
					</div>
				</c:otherwise>
			</c:choose>
	</c:otherwise>
</c:choose>
</div> <!--  searchRoleResultsPane -->
<html:javascript formName="/SearchRoles"/>