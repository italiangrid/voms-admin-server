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

<div class="reloadable">
	<voms:hasPermissions var="canReadPI" context="vo"
		permission="PERSONAL_INFO_READ" />

	<s:if test="#attr.canReadPI or (#attr.currentAdmin.is(model))">
		<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp" />

		<s:form action="save-personal-information"
			onsubmit="ajaxSubmit(this,'pers-info-content'); return false;">

			<s:token />

			<s:hidden name="userId" value="%{id}" />

			<s:textfield name="theName" label="Given name" size="40"
				cssClass="registrationField" value="%{name}" disabled="false"
				readonly="%{#attr.readOnlyPI}" />

			<s:textfield name="theSurname" disabled="false" label="Family name"
				size="40" cssClass="registrationField" value="%{surname}"
				readonly="%{#attr.readOnlyPI}" />

			<s:textfield name="theInstitution" disabled="false"
				label="Institution" size="40" cssClass="registrationField"
				value="%{institution}" readonly="%{#attr.readOnlyPI}" />

			<s:textarea name="theAddress" disabled="false" label="Address"
				rows="4" cols="30" cssClass="registrationField" value="%{address}" />

			<s:textfield name="thePhoneNumber" disabled="false" label="Phone"
				size="40" cssClass="registrationField" value="%{phoneNumber}"
        />

			<s:textfield name="theEmailAddress" disabled="false" label="Email"
				size="40" cssClass="registrationField" value="%{emailAddress}" 
				readonly="%{#attr.readOnlyPI}" />

			<s:submit value="%{'Change personal information'}" />
			
		</s:form>
	</s:if>
	<s:else>
		You do not have the rights to see the personal information for this user.
	</s:else>
</div>