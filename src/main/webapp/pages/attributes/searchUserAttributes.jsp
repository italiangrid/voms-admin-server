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
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@ page contentType="text/html; charset=UTF-8" %>


<div id="searchUserAttributesPane">
	<html:form action="/SearchUserAttributes" method="post">
		<html:hidden property="firstResults" value="0" />
		<html:text property="text" styleClass="searchField" size="20" value="${searchResults.searchString }"/>	
		<voms:submit context="vo" permission="ATTRIBUTES_READ" styleClass="submitButton" value="Search user attributes"/>
	</html:form>
</div> <!--  searchUserAttributesPane -->


<div id="manageAttributeClassesPane">
	<voms:link 
		action="/LoadAttributeDescriptions"
		context="vo"
		permission="ATTRIBUTES_READ|ATTRIBUTES_WRITE" 
		disabledStyleClass="disabledLink"
		styleClass="vomsLink">
		Manage attribute classes
	</voms:link>
</div>

<div id="searchResultsPane">
	<c:choose>
		<c:when test="${! empty searchResults.results}">
					
			<table class="table" cellpadding="0" cellspacing="0">
				<tr class="tableHeaderRow">
					<td>Attribute name</td>
					<td>Attribute value</td>
					<td>User</td>
				</tr>
				<c:forEach var="userAttribute" items="${searchResults.results}" varStatus="status">
					<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
						<td>
							<div class="attributeName">
								${userAttribute[0]}
							</div>
						</td>
						
						<td>
							<div class="attributeValue">
								${userAttribute[2]}
							</div>
						</td>
						
						<td>
							<div class="userDN">
							
								<c:url value="/PreEditUser.do" var="editUserUrl">
									<c:param name="id" value="${userAttribute[1].id}"/>
								</c:url>
								<voms:link
									context="vo"
									permission="r"
									href="${editUserUrl}"
									styleClass="actionLink"
									disabledStyleClass="disabledLink"
								>
									${userAttribute[1].fullName}
								</voms:link>
							</div>
						</td>
					</tr>
				</c:forEach>
			</table>
			
			<div class="resultsFooter">
				<voms:searchNavBar context="vo" 
					permission="ATTRIBUTES_READ" 
					disabledLinkStyleClass="disabledLink"
					id="searchResults"
					linkStyleClass="navBarLink"
					searchURL="/SearchUserAttributes.do"
					styleClass="resultsCount"
				/>	
			</div><!-- resultsFooter -->
			
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${empty searchResults.searchString}">
					<div class="header2">
					No users attributes defined in this vo.
					</div>
				</c:when>
				<c:otherwise>
					<div class="header2">
					No user attributes found matching "${ searchResults.searchString }".		
					</div>
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
</div> <!-- searchResultsPane -->