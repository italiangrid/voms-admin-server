<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015

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

<div id="orgdb-results">

	<s:form action="query-database" validate="true"
		namespace="/register/orgdb"
		onsubmit="ajaxSubmit(this,'orgdb-results'); return false;">
		<ul class="form">
			<li><s:textfield name="emailAddress"
					value="%{requester.emailAddress}" size="60"
					label="%{'Your CERN HR DB email address'}"
					cssClass="registrationField" /></li>

			<s:submit />
		</ul>
	</s:form>

	<div class="reloadable">
		<tiles2:insertTemplate template="../../shared/errorsAndMessages.jsp" />
		<s:if test="model.size > 0">
			<table>
				<thead>
					<tr>
						<th>Name</th>
						<th>Surname</th>
						<th>Institute</th>
						<th>Country</th>
						<th>Email addresses</th>
						<th />
					</tr>
				</thead>
				<tbody>

					<s:iterator value="model">
						<tr>
							<td><s:property value="firstName" /></td>
							<td><s:property value="name" /></td>
							<td><s:property
									value="getValidParticipationForExperiment(experimentName).institute.name"
									default="N/A" /></td>
							<td><s:property value="nationality1.name" default="N/A" /></td>
							<td>
								<div>
									<s:property value="physicalEmail" />
								</div>
								<div>
									<s:property value="email" />
								</div>
							</td>
							<td><s:form namespace="/register/orgdb" action="start">
									<s:hidden name="vomsPersonId" value="%{id}" />
									<s:submit value="%{'It\\'s me!'}" />
								</s:form></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</s:if>
	</div>
</div>