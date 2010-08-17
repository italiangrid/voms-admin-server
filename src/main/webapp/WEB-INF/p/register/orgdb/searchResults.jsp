<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div class="reloadable">

	<tiles2:insertTemplate template="../../shared/errorsAndMessages.jsp"/>

	<s:if test="model.size > 0">
		<table>
			<thead>
				<tr>
					<th>Name</th>
					<th>Surname</th>
					<th>Date of birth <div class="dateformat">(mm/dd/yyyy)</div></th>
					<th>Institute</th>
					<th>Country</th>
					<th>Email addresses</th>
					<th/>
				</tr>
			</thead>
			<tbody>
			
				<s:iterator value="model">
				<tr>
					<td><s:property value="firstName"/></td>
					<td><s:property value="name"/></td>
					<td>
						<s:text name="format.date">
        					<s:param value="dateOfBirth"/>
      					</s:text>
					</td>
					<td><s:property value="getValidParticipationForExperiment(experimentName).institute.name" default="N/A"/></td>
					<td><s:property value="nationality1.name" default="N/A"/></td>
					<td>
						<div><s:property value="physicalEmail"/></div>
						<div><s:property value="email"/> </div>
					</td>
					<td>
						<s:form namespace="/register/orgdb" action="start">
							<s:hidden name="vomsPersonId" value="%{id}"/>
							<s:submit value="%{'It\\'s me!'}"/>
						</s:form>
					</td>
				</tr>
				</s:iterator>
			</tbody>
			
		</table>
	</s:if>
</div>