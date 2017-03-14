<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<p>
<s:form 
  id="savePersonalInformationForm"
  action="save-personal-information"
  namespace="/user"
  theme="bootstrap"
  cssClass="form-horizontal">
  
  <s:token/>
  <s:hidden name="userId" value="%{id}" />
  <s:textfield name="theName" label="Given name" size="40"
      value="%{name}" placeholder="Insert the user name..." />
      
    <s:textfield name="theSurname" label="Family name" size="40"
      value="%{surname}" placeholder="Insert the user family name..." />
      
    <s:textfield name="theInstitution" label="Institution"
      size="40" value="%{institution}" />
      
    <s:textarea name="theAddress" label="Address" rows="4"
      cols="30" value="%{address}" />
      
    <s:textfield name="thePhoneNumber" disabled="false" label="Phone" size="40"
       value="%{phoneNumber}" />
      
    <s:textfield name="theEmailAddress" disabled="false" label="Email"
      size="40" value="%{emailAddress}" />
    
    <div class="form-group">
      <div class="col-sm-offset-3 col-sm-9">
	    <sj:submit cssClass="btn btn-primary" 
	      value="%{'Save personal information'}" 
	      validate="true" validateFunction="bootstrapValidation"
	      />
	      </div>
    </div>
</s:form>
</p>

<div class="reloadable">
	<voms:hasPermissions var="canReadPI" context="vo"
		permission="PERSONAL_INFO_READ" />

	<s:if test="#attr.canReadPI or (#attr.currentAdmin.is(model))">
		<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp" />

		<s:form action="save-personal-information"
			onsubmit="ajaxSubmit(this,'pers-info-content'); return false;">

			<s:token />

			<s:hidden name="userId" value="%{id}" />

			<s:textfield name="theName" label="Given name" size="80"
				cssClass="registrationField" value="%{name}" disabled="false"
				readonly="%{#attr.readOnlyPI}" />

			<s:textfield name="theSurname" disabled="false" label="Family name"
				size="80" cssClass="registrationField" value="%{surname}"
				readonly="%{#attr.readOnlyPI}" />

			<s:textfield name="theInstitution" disabled="false"
				label="Institution" size="80" cssClass="registrationField"
				value="%{institution}" readonly="%{#attr.readOnlyPI}" />

			<s:textarea name="theAddress" disabled="false" label="Address"
				rows="4" cols="80" cssClass="registrationField" value="%{address}" />

			<s:textfield name="thePhoneNumber" disabled="false" label="Phone"
				size="80" cssClass="registrationField" value="%{phoneNumber}"
        />

			<s:textfield name="theEmailAddress" disabled="false" label="Email"
				size="80" cssClass="registrationField" value="%{emailAddress}" 
				readonly="%{#attr.readOnlyPI}" />

			<s:submit value="%{'Change personal information'}" />
			
		</s:form>
	</s:if>
	<s:else>
		You do not have the rights to see the personal information for this user.
	</s:else>
</div>
