<%--
 Copyright (c) Members of the EGEE Collaboration. 2008.
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

<div id="createTagPane">
	<voms:authorized permission="ALL" context="vo">
		<html:link 
			action="/CreateTag"
			styleClass="vomsLink">Create a new tag</html:link>
	</voms:authorized>
</div><!-- createTagPane -->

<div id="manageTagPane">
<c:choose>
	<c:when test="${! empty tags}">
		<div class="header2">Tags:</div>
		
		<table class="table" cellpadding="0" cellspacing="0">
		
			<c:forEach var="tag" items="${tags}" varStatus="status">
				<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
					<td width="95%">
						<div class="tagName">
							${tag.dn}
						</div>
					</td>
					
					<td>
						<c:url value="/Tags.do" var="deleteTagURL">
							<c:param name="method" value="delete"/>
							<c:param name="id" value="${tag.id}"/>
						</c:url>
	
						<voms:link
							context="vo"
							permission="ALL"
							href="javascript:ask_confirm('Delete tag? \n${tag.dn}\n', '${deleteTagURL}', 'Tag ${tag.dn} not deleted.')"
							styleClass="actionLink"
							disabledStyleClass="disabledLink"
						>delete tag</voms:link>
						
					</td>
				</tr>
			</c:forEach>
		</table>	
	</c:when>
	<c:otherwise>
		<div class="header2">
			No tags defined in this vo.
			</div>
	</c:otherwise>
</c:choose>
</div><!-- manageTagPane -->
