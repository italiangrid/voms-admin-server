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
<div id="pi-pane">
<s:form 
  id="savePersonalInformationForm"
  action="save-personal-information"
  namespace="/user"
  theme="bootstrap"
  cssClass="form-horizontal">
  
  <s:token/>
  
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

  <sj:submit cssClass="btn btn-primary" 
      formIds="savePersonalInformationForm"
      value="%{'Save personal information'}" 
      validate="true" validateFunction="bootstrapValidation"
      targets="pi-pane"
      />
</s:form>
</div>