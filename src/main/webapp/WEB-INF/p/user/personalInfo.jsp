<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div class="reloadable">
	<voms:hasPermissions var="canReadPI" context="vo" permission="PERSONAL_INFO_READ"/>
	 
	
	<s:if test="#attr.canReadPI or (#attr.currentAdmin.is(model))">
	 <tiles2:insertTemplate template="../shared_20/errorsAndMessages.jsp"/>
	<s:form action="save-personal-information"
		onsubmit="ajaxSubmit(this,'pers-info-content'); return false;">
		<s:token/>
		<s:hidden name="userId" value="%{id}"/>
		<s:textfield name="theName" label="Name" size="40"
			cssClass="registrationField" value="%{name}" disabled="false"/>
		<s:textfield name="theSurname" disabled="false" label="Surname" size="40"
			cssClass="registrationField" value="%{surname}"/>
		<s:textfield name="theInstitution" disabled="false" label="Institution"
			size="40" cssClass="registrationField" value="%{institution}"/>
		<s:textarea name="theAddress" disabled="false" label="Address" rows="4"
			cols="30" cssClass="registrationField" value="%{address}"/>
		<s:textfield name="thePhoneNumber" disabled="false" label="Phone" size="40"
			cssClass="registrationField" value="%{phoneNumber}"/>
		<s:textfield name="theEmailAddress" disabled="false" label="Email"
			size="40" cssClass="registrationField" value="%{emailAddress}"/>
		<s:submit value="%{'Change personal information'}" disabled="false" />
	</s:form>
	</s:if>
	<s:else>
		You do not have the rights to see the personal information for this user.
	</s:else>
</div>