<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<tiles2:insertTemplate template="../shared_20/formattedDNControls.jsp" />

<voms:authorized permission="CONTAINER_READ|CONTAINER_WRITE"
	context="vo">
	<div id="add-certificate-link"><s:url action="add-certificate"
		namespace="/user" var="addCertificateURL">
		<s:param name="userId" value="id" />
	</s:url> <s:a href="%{#addCertificateURL}" cssClass="actionLink  ">Add a new certificate</s:a>
	</div>
</voms:authorized>


<s:if test="certificates.empty">
        No certificates defined for this user.
      </s:if>
<s:else>
	<voms:hasPermissions var="canSuspend" context="/${voName}"
		permission="CONTAINER_READ|MEMBERSHIP_READ|SUSPEND" />

	<table cellpadding="0" cellspacing="0">

		<s:iterator var="cert" value="certificates">
			<tr class="tableRow">
				<td>
				<div class="userDN"><s:set value="subjectString"
					var="thisCertDN" /> <voms:formatDN dn="${thisCertDN}" fields="CN" />
				</div>

				<div class="userCA"><s:set value="ca.subjectString"
					var="thisCertCA" /> <voms:formatDN dn="${thisCertCA}" fields="CN" />
				</div>

				<div class="cert-date-info">Added on: <span><s:property
					value="creationTime" /></span></div>

				<div class="cert-status-info"><s:if test="suspended">
					<span> Suspended: </span>

					<span class="suspensionReason"> <s:property
						value="suspensionReason" /> </span>
				</s:if></div>

				<div class="cert-operations"><s:if
					test="#attr.canSuspend and not suspended">

					<s:form action="suspend-certificate" namespace="/user"
						theme="simple" cssClass="cert-operation-forms">
						<s:token />
						<s:hidden name="userId" value="%{model.id}" />
						<s:hidden name="certificateId" value="%{#cert.id}" />
						<s:submit value="%{'Suspend'}" />
					</s:form>
				</s:if> <s:if test="#attr.canSuspend and suspended">

					<s:if test="not user.suspended">
						<s:form action="restore-certificate" namespace="/user"
							theme="simple" cssClass="cert-operation-forms">
							<s:token />
							<s:hidden name="userId" value="%{model.id}" />
							<s:hidden name="certificateId" value="%{#cert.id}" />
							<s:submit value="%{'Restore'}" />
						</s:form>
					</s:if>

				</s:if> <s:if test="model.certificates.size > 1">
					<s:form action="delete-certificate" namespace="/user"
						theme="simple" cssClass="cert-operation-forms">
						<s:token />
						<s:hidden name="userId" value="%{model.id}" />
						<s:hidden name="certificateId" value="%{#cert.id}" />
						<s:submit value="%{'Delete'}" />
					</s:form>
				</s:if></div>

				</td>
			</tr>
		</s:iterator>
	</table>
</s:else>
