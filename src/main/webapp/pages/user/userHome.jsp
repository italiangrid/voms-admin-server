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

<div class="header1">
	Welcome to ${voName}, ${vomsUser.fullName}.
</div>

<div class="header2">
	Your VO membership information:
</div>

<div class="voMembershipInfo">	
<table class="table" cellpadding="0" cellspacing="0">		
		<tr class="tableHeaderRow">
			<td>Group name</td>
			<td>Roles</td>
		</tr>
		
		<c:forEach var="mapping" items="${vomsUser.mappingsMap}" varStatus="status">
	
			<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
				<td width="50%">
					<div class="group">
						${mapping.key}
					</div>
				</td>
				<td>
					<div class="roles">
					<c:forEach var="role" items="${mapping.value}" varStatus="status">
						<div class="roleCell" class="${ (status.index) %2 eq 0 ? 'rowEven': 'rowOdd'}">
							<div class="role">
								${role.name}
							</div>
						</div> <!--  role cell -->
					</c:forEach>
				</td>
			</tr>
		</c:forEach>		
</table><!-- Membership table -->
</div>

<div class="header2">
			Your generic attributes:
</div>

<div class="genericAttributesInfo">		
	<c:if test="${fn:length(vomsUser.attributes) == 0}">
		Currently, no generic attributes have been defined for you.
	</c:if>
	
	<c:if test="${fn:length(vomsUser.attributes) > 0}">

		
		<table class="table" cellpadding="0" cellspacing="0">
		
		<tr class="tableHeaderRow">
			<td>Attribute name</td>
			<td>Attribute value</td>
		</tr>
		
		<c:forEach var="attribute" items="${vomsUser.attributes}" varStatus="status">
			
			<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
				
				<td width="50%" class="tableCell">
					<div class="attributeName">
						${attribute.name}
					</div>
				</td>
				
				<td class="tableCell">
					<div class="attributeValue">
						${attribute.value}
					</div>
				</td>
			</tr>
		</c:forEach>
		</table>
	</c:if>
</div>