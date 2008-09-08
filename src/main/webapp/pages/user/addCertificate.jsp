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
<%@ taglib uri="http://org.glite.security.voms.tags" prefix="voms"%>

<div class="header1">Add a certificate</div>

<html:form action="/UserCertificates" method="post" enctype="multipart/form-data">
	<html:hidden property="method" value="add"/>

	<table cellpadding="0" cellspacing="0" class="form">
		<colgroup>
				<col class="labels"/>
				<col class="fields"/>
		</colgroup>
		
		<tr>
			<td>
				<div class="header3">User information</div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="label">Name:</div>			
			</td>
			<td>
				${vomsUser.name} ${vomsUser.surname}		
			</td>
		</tr>
		<tr>
			<td>
				<div class="label">Email:</div>
			</td>
			
			<td>
				${vomsUser.emailAddress}
			</td>
		</tr>
		
		<tr>
			<td>
				<div class="header3">Add a certificate</div>
			</td>
		</tr>
		
		<tr>
			<td><html:radio property="certificateType" value="dnCa">Specify a DN, CA couple:</html:radio></td>
			<td>
				<table>
					<tr>
						<td>DN:</td>
						<td>
							<html:text property="dn" styleClass="inputField" size="25"/>
						</td>
					</tr>
					<tr>
						<td>CA:</td>
						<td>
							<html:select property="ca" styleClass="selectBox" style="font-size: smaller">
								<html:options collection="caList"  property="dn" labelProperty="dn"/>
							</html:select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		
		<tr>
			<td><html:radio property="certificateType" value="cert">Upload certificate from file:</html:radio></td>
			<td><html:file property="certificateFile"/></td>
		</tr>
		
		<tr>
			<td/>
			<td>
				<voms:submit context="vo" permission="CONTAINER_WRITE" styleClass="submitButton" value="Add certificate!"/>
			</td>
		</tr>
		</table>
</html:form>


