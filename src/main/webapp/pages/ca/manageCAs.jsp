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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://org.glite.security.voms.tags" prefix="voms"%>


<jsp:useBean id="now" class="java.util.Date"/>

<div id="searchCAsPane">
		<html:form action="/SearchCAs" method="post">
			<html:hidden property="firstResults" value="0" />
			<html:text property="text" styleClass="searchField" size="20" value="${searchResults.searchString }"/>	
			<voms:submit context="vo" permission="CONTAINER_READ" styleClass="submitButton" value="Search CAs"/>
		</html:form>
</div> <!-- searchCAsPane -->

<div id="searchResultsPane"> 
	<c:choose>
		<c:when test="${empty searchResults.results}">
			
			<c:if test="${empty searchResults.searchString}">
				No Certificate Authorities found in database!
			</c:if>
			
			<c:if test="${!empty searchResults.searchString}">
				No Certificate Authorities found that match with '${searchResults.searchString}'.
			</c:if>
		</c:when>
		<c:otherwise>
			<table class="table" cellpadding="0" cellspacing="0">
				<tr class="tableHeaderRow">
					<td>CA subject</td>
					<td style="text-align: center">Expires on</td>
				</tr>
				<c:forEach var="ca" items="${searchResults.results}" varStatus="status">
					<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
						<td>
							<div class="caCN">
								<voms:formatDN dn="${ca.subjectString}" fields="CN"/><br/>
							</div>
							<div class="userCA">${ca.subjectString}</div>
						</td>

						<c:choose>
							<c:when test="${now gt ca.notAfter}">
								<td class="expiredCA" style="text-align: center;">
									Expired!<br/>
									<div style="font-size: smaller; color: black; font-weight: normal">
										<fmt:formatDate value="${ca.notAfter}"/>
									</div>
								</td>
							</c:when>
							<c:otherwise>
								<td style="text-align: center;">
									<fmt:formatDate value="${ca.notAfter}"/>
								</td>						
							</c:otherwise>
						</c:choose>

					</tr>					
				</c:forEach>
			</table>	
			<div class="resultsFooter">
				<voms:searchNavBar 
					context="vo" 
					permission="CONTAINER_READ" 
					disabledLinkStyleClass="disabledLink"
					id="searchResults"
					linkStyleClass="navBarLink"
					searchURL="/SearchCAs.do"
					styleClass="resultsCount"
				/>	
		</div><!-- resultsFooter -->				
		</c:otherwise>
	</c:choose>
</div> <!-- searchResultsPane -->