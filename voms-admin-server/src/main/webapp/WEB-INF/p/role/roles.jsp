<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<voms:hasPermissions var="canRead" context="vo"
	permission="CONTAINER_READ" />

<voms:hasPermissions var="canDelete" context="vo"
	permission="CONTAINER_READ|CONTAINER_WRITE" />

<h2>Roles</h2>

<s:if test="#attr.canRead">
	<div class="searchResultsPane">
		<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp" />
		<s:if
			test='(#session.searchResults.searchString eq null) and (#session.searchResults.results.size == 0)'>
No roles defined for this VO.
</s:if>
		<s:elseif test="#session.searchResults.results.size == 0">
  No roles found matching search string '<s:property
				value="#session.searchResults.searchString" />'.
</s:elseif>
		<s:else>

			<table class="table">
				<s:iterator value="#session.searchResults.results" var="role"
					status="rowStatus">
					<tr>
						<td><s:url action="edit" namespace="/role" var="editURL">
								<s:param name="roleId" value="id" />
							</s:url> <s:a href="%{editURL}">
								<s:property value="name" />
							</s:a></td>
						<td><s:if test="#attr['canDelete']">
								<s:form action="delete" namespace="/role" theme="bootstrap" cssClass="form-inline pull-right">
									<s:token />
									<s:hidden name="roleId" value="%{id}" />
									<s:submit value="%{'delete'}"
										onclick="openConfirmDialog(this, 'deleteRoleDialog','%{name}'); return false" />
								</s:form>
							</s:if></td>
					</tr>
				</s:iterator>
			</table>
		</s:else>
	</div>
</s:if>
<s:else>
  You do not have enough permissions to browse this VO roles.
</s:else>