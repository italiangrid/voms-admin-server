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

<div id="searchGroupsPane">
	<html:form action="/SearchGroups" method="post" onsubmit="return validateSearchForm(this)">
		<html:text property="text" styleClass="inputField" size="20" value="${searchResults.searchString }"/>
		<html:hidden property="firstResults" value="0"/>
		<voms:submit context="vo" permission="CONTAINER_READ" styleClass="submitButton" value="Search groups"/>
	</html:form>
</div>

<div id="createGroupPane">
	<voms:authorized permission="CONTAINER_READ|CONTAINER_WRITE" context="vo">
		<html:link action="/PreCreateGroup" styleClass="vomsLink">Create a new group</html:link>	
	</voms:authorized>
</div>

<c:if test="${hasPerms}">
	<tiles:insert page="/pages/group/createGroup.jsp" flush="true"/>
</c:if>

<c:choose>

	<c:when test="${! empty searchResults.results}">

		<div id="searchGroupsResultsPane">
			<div class="header2">
				Groups:
			</div>
			
			<table class="table" cellpadding="0" cellspacing="0">
				
				<c:forEach var="vomsGroup" items="${searchResults.results}" varStatus="status">
				<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
					<td class="tableCell" width="90%">
					<c:url value="/LoadGroup.do" var="editGroupUrl">
							<c:param name="id" value="${vomsGroup.id}"/>
						</c:url>
						<voms:link
							context="vo"
							permission="r"
							href="${editGroupUrl}"
							styleClass="actionLink"
							disabledStyleClass="disabledLink"
						>${vomsGroup.name }</voms:link>
					</td>
					<td class="tableCell">
						<div class="actions">
						<c:if test="${! vomsGroup.rootGroup}"> 
							<c:url value="/Group.do" var="deleteGroupUrl">
								<c:param name="method" value="deleteGroup"/>
								<c:param name="id" value="${vomsGroup.id}"/>
							</c:url>
							<voms:link
								context="vo"
								permission="rw"
								href="javascript:ask_confirm('Delete group ${vomsGroup.name}?','${deleteGroupUrl}','Group ${vomsGroup.name } not deleted.')"
								styleClass="actionLink"
								disabledStyleClass="disabledLink"
							>delete</voms:link>
						</c:if>
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
					searchURL="/SearchGroups.do"
					styleClass="resultsCount"
				/>	
			</div> <!-- resultsFooter -->	
		</div> <!--  searchGroupResultsPane -->
		
	</c:when>
		
	<c:otherwise>
		<div class="header2">
			No groups found matching "${searchResults.searchString}".
		</div>
	</c:otherwise>
	
</c:choose>
<html:javascript formName="/SearchGroups"/>
