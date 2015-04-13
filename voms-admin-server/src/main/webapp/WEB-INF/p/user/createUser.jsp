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

<s:form id="createUserForm" action="create-user" namespace="/user"
  theme="bootstrap" cssClass="form-horizontal">
  
  <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel">Create a new VO user</h4>
	</div>
  
  <div class="modal-body">
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
		
		<hr/>
		
		<h4>Certificate information</h4>

		<s:textfield name="subject" size="45" label="Subject" />
    
    <s:select name="caSubject" 
      list="#request.trustedCas" 
      listKey="subjectString" 
      listValue="subjectString" 
      label="CA" />
  </div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		<sj:submit cssClass="btn btn-primary" 
		  formIds="createUserForm" 
		  value="%{'Create user'}" 
		  validate="true" validateFunction="bootstrapValidation"/>
	</div>
</s:form>