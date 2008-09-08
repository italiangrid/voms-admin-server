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


	<c:choose>
		<c:when test="${!empty attributeDescriptions}">
			<voms:authorized permission="ATTRIBUTES_WRITE" context="${vomsGroup}">
			<div class="attributesCreationPane">
			<html:form action="/GroupAttributes" method="post" onsubmit="return validateGroupAttributesForm(this)">
				<html:hidden property="method" value="set"/>
			
				<table class="form" cellpadding="0" cellspacing="0">
					<colgroup>
						<col class="labels"/>
						<col class="fields"/>
					</colgroup>
		
					<tr>
							<td>
								<div class="label">
									Attribute:
								</div>
							</td>
							<td>
								<html:select property="attributeName" styleClass="selectBox">
									<html:options collection="attributeDescriptions" property="name" labelProperty="name"/>
								</html:select>
				
							</td>
					</tr>
					
					<tr>
						<td>
							<div class="label">
								Attribute value:
							</div>
						</td>
						<td>
							<html:textarea property="attributeValue"  rows="4" cols="50" styleClass="inputField" value=""/>
						</td>
					</tr>
					
					<tr>
						<td/>
						<td>
							<voms:submit context="${vomsGroup}" permission="ATTRIBUTES_READ|ATTRIBUTES_WRITE" styleClass="submitButton" value="Set the attribute value"/>				
						</td>
					</tr>				
				</table>
				<html:hidden property="groupId" value="${vomsGroup.id}"/>
			</html:form>
		</div> <!-- attributesCreationPane -->
		
		</voms:authorized>
		
		<voms:authorized permission="ATTRIBUTES_READ" context="${vomsGroup}">
		
			<div class="attributesListPane">
				<div class="separator">&nbsp;</div>
				
				<c:if test="${fn:length(vomsGroup.attributes) == 0}">
					No attributes defined for this group.
				</c:if>
				
				<c:if test="${fn:length(vomsGroup.attributes) > 0}">
					<div class="header2">
						Attribute list:
					</div>	
					<table class="table" cellpadding="0" cellspacing="0">
					
					<tr class="tableHeaderRow">
						<td>Attribute name</td>
						<td>Attribute value</td>
						<td>Actions</td>
					</tr>
					
					<c:forEach var="attribute" items="${vomsGroup.attributes}" varStatus="status">
						
						<tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
							
							<td width="40%" class="tableCell">
								<div class="attributeName">
									${attribute.name}
								</div>
							</td>
							
							<td width="50%" class="tableCell">
								<div class="attributeValue">
									${attribute.value}
								</div>
							</td>
							
							<td class="tableCell">
								<div class="actions">
									<c:url value="/GroupAttributes.do" var="deleteUserAttributeURL">
										<c:param name="method" value="delete"/>
										<c:param name="groupId" value="${vomsGroup.id}"/>
										<c:param name="attributeName" value="${attribute.name}"/>
									</c:url>
								
									<voms:link 
										href="${deleteUserAttributeURL}"
										context="${vomsGroup}"
										permission="ATTRIBUTES_READ|ATTRIBUTES_WRITE"
										styleClass="actionLink"
										disabledStyleClass="disabledLink">
										delete
									</voms:link>
								</div>
							</td>
						</tr>	
					</c:forEach>
					</table> <!--  table -->
				</c:if>
			</div>
			</voms:authorized>
		</c:when>
		<c:otherwise>
			No attribute classes defined for this vo.
		</c:otherwise>
	</c:choose>

<div class="separator">&nbsp;</div>



<html:javascript formName="/GroupAttributes"/>