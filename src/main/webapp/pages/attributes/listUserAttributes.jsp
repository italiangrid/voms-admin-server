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


<div id="listUserAttributesPane">

	<table class="table" cellpadding="0" cellspacing="0">
		<tr class="tableHeaderRow">
			<td>Attribute name</td>
			<td>User DN &amp; CA</td>
			<td>Attribute value</td>
		</tr>
		<c:forEach var="userAttribute" items="${userAttributes}" varStatus="status">
			
			<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
			
				<td>
					<div class="attributeName">
						${userAttribute[0]}
					</div>
				</td>
				
				<td>
					<div class="userDN">
						<voms:formatDN dn="${userAttribute[1].dn}" fields="CN"/>
					</div>
					<div class="userCA">
						<voms:formatDN dn="${userAttribute[1].ca.dn}" fields="CN,O"/>
					<div>
				</td>
				
				<td>
					<div class="attributeValue">
						${userAttribute[2]}
					</div>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>