<%--

    Copyright (c) Members of the EGEE Collaboration. 2006-2009.
    See http://www.eu-egee.org/partners/ for details on the copyright holders.

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
    	Andrea Ceccanti (INFN)

--%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<voms:hasPermissions var="canRead" context="vo"
	permission="CONTAINER_READ" />

<h2>Groups</h2>

<s:if test="#attr.canRead">

	<div class="searchResultsPane">
		<s:if
			test='(#session.searchResults.searchString eq null) and (#session.searchResults.results.size == 0)'>
No groups found in this VO.
</s:if>
		<s:elseif test="#session.searchResults.results.size == 0">
  No groups found matching search string '<s:property
				value="#session.searchResults.searchString" />'.
</s:elseif>
		<s:else>

			<table class="table">
				<s:iterator value="#session.searchResults.results" var="group"
					status="rowStatus">
					<tr>

						<td>
							<div class="groupName">
								<s:url action="edit" namespace="/group" var="editURL">
									<s:param name="groupId" value="id" />
								</s:url>
								<s:a href="%{editURL}">
									<s:property value="name" />
								</s:a>
							</div> <s:if test="description != null">
								<div class="groupDescription">
									<s:property value="description" />
								</div>
							</s:if>
						</td>
						<td><voms:hasPermissions var="canDelete" context="/${voName}"
								permission="CONTAINER_READ|CONTAINER_WRITE" /> <s:if
								test="(not rootGroup) and #attr['canDelete']">
								<div style="float: right">

									<s:form action="delete" namespace="/group" theme="simple"
										cssClass="inline">
										<s:token />
										<s:hidden name="groupId" value="%{id}" />

										<s:submit value="%{'delete'}"
											onclick="openConfirmDialog(this, 'deleteGroupDialog','%{name}'); return false" />
									</s:form>
								</div>
							</s:if></td>
					</tr>
				</s:iterator>
			</table>
		</s:else>
	</div>
</s:if>
<s:else>
  You do not have enough permissions to browse this VO groups.
</s:else>