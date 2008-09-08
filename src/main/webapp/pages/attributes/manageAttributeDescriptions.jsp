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

<div class="header1">Create a new attribute description</div>

<div id="createAttributeDescriptionPane">
<voms:authorized 
	permission="CONTAINER_READ|CONTAINER_WRITE|ATTRIBUTES_READ|ATTRIBUTES_WRITE" 
	context="vo">
	
	<html:form action="/CreateAttributeDescription" method="post" onsubmit="return validateAttributeDescriptionForm(this)">
	
	<table cellpadding="0" cellspacing="0" class="form">
		<colgroup>
				<col class="labels"/>
				<col class="fields"/>
		</colgroup>
		<tr>
			<td>
				<div class="label">Attribute name:</div>			
			</td>
			<td>
				<html:text property="attributeName" size="25" styleClass="inputField"/>		
			</td>
		</tr>
		
		<tr>
			<td>
				<div class="label">Attribute description:</div>			
			</td>
			<td>
				<html:textarea property="attributeDescription"  rows="4" cols="50" styleClass="inputField" value=""/>
			</td>
		</tr>
		
		<tr>
			<td>
				<div class="label">Unique contraint:</div>			
			</td>
			<td>
				<html:checkbox property="attributeUnique" styleClass="inputField"/>
			</td>
		</tr>
		
		<tr>
			<td/>
			<td>
				<html:submit value="Create!" styleClass="submitButton"/>
			</td>
		</tr>
		<html:hidden property="method" value="create"/>
		</table>
	</html:form>
</voms:authorized>
</div>

<div id="listAttributeDescriptionsPane">
	<c:choose>
		<c:when test="${! empty attributeDescriptions}">
					
			<table class="table" cellpadding="0" cellspacing="0">
				<tr class="tableHeaderRow">
					<td>Attribute name</td>
					<td>Attribute description</td>
					<td>Unique check</td>
					<td/>
				</tr>
				<c:forEach var="desc" items="${attributeDescriptions}" varStatus="status">
					<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
						<td>
							${desc.name}
						</td>
						<td>
							${desc.description}
						</td>
						<td>
							${desc.unique}
						</td>
						
						<td>
							<div class="actions">
								<c:url value="/AttributeDescription.do" var="deleteAttributeDescriptionURL">
									<c:param name="method" value="delete"/>
									<c:param name="attributeName" value="${desc.name}"/>
								</c:url>
							
								<voms:link 
									href="${deleteAttributeDescriptionURL}"
									context="vo"
									permission="ATTRIBUTES_READ|ATTRIBUTES_WRITE"
									styleClass="actionLink"
									disabledStyleClass="disabledLink">
									delete
								</voms:link>
							</div>
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:when>
		<c:otherwise>
			<div class="header2">
					No attribute descriptions defined for this vo.
			</div>
		</c:otherwise>
	</c:choose>
	
</div>
<html:javascript formName="/CreateAttributeDescription"/>
