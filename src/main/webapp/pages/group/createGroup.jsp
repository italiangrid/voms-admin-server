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


<div class="header1">Create a new VO group</div>

	<html:form action="/CreateGroup" method="post" onsubmit="return validateGroupForm(this)">
		<html:hidden property="method" value="createGroup"/>
		<table cellpadding="0" cellspacing="0" class="form">
			<colgroup>
				<col class="labels"/>
				<col class="fields"/>
			</colgroup>
			<tr>
				<td>
					<div class="label">
						Parent group:
					</div>	
				</td>
				<td>
					<html:select property="parentGroupName" styleClass="selectBox">
						<html:options collection="allGroups" property="name"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td>
					<div class="label">
					Name:
					</div>
				</td>
				<td>
					<html:text property="name" styleClass="inputField"/>
				</td>
			</tr>
			<tr>
				<td/>
				<td><voms:submit context="vo" permission="CONTAINER_WRITE" styleClass="submitButton" value="Create!"/></td>
			</tr>
		</table>
	</html:form>
	
<html:javascript formName="/CreateGroup"/>

