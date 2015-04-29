<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if
	test="requesterInfo.getMultivaluedInfo('requestedGroup').size() > 0">

	<s:set var="requestedGroups"
		value="requesterInfo.getMultivaluedInfo('requestedGroup')" />

	<s:set var="groupVisibility" value="%{'visible'}" />

	<h4>Requested groups</h4>

	<p>The applicant requested to be member of the groups listed below.
		Check the groups that you want to be assigned to the user as result of
		the approval of this membership request.</p>


	<table class="table attribute-req">
		<thead>
			<tr>
				<th><s:checkbox id="apprGroupSel_%{id}" name="notSet"
						theme="simple" /></th>
				<th>Requested group name</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="#requestedGroups" var="name">
				<tr>
					<td style="width: 1%"><s:checkbox name="approvedGroups_%{id}"
							fieldValue="%{name}" theme="simple" cssClass="groupCheck_%{id}"
							value="false" /></td>
					<td><s:property value="name" /></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>

	<script>
		$('#apprGroupSel_<s:property value="id"/>').change(function() {
			var checked = $(this).attr("checked");
			$('.groupCheck_<s:property value="id"/>').attr("checked", checked);
			$('.groupCheck_<s:property value="id"/>').change();
		});
	</script>

</s:if>