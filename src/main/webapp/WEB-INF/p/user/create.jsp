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
<h1>Create a new user:</h1>

<p>All the fields below are required.</p>
<s:form validate="true">
		<s:token/>
		
		<s:textfield name="theName" label="Given name" size="40"
			value="%{name}" disabled="false" cssClass="registrationField"/>
			
		<s:textfield name="theSurname" disabled="false" label="Family name" size="40"
			value="%{surname}" cssClass="registrationField"/>
			
		<s:textfield name="theInstitution" disabled="false" label="Institution"
			size="40" value="%{institution}" cssClass="registrationField"/>
			
		<s:textarea name="theAddress" disabled="false" label="Address" rows="4"
			cols="30" value="%{address}" cssClass="registrationField"/>
			
		<s:textfield name="thePhoneNumber" disabled="false" label="Phone" size="40"
			 value="%{phoneNumber}" cssClass="registrationField"/>
			
		<s:textfield name="theEmailAddress" disabled="false" label="Email"
			size="40" value="%{emailAddress}" cssClass="registrationField"/>
		
		<h2>Certificate information</h2>
		<s:textfield name="subject" size="45" label="Subject" cssClass="registrationField"/>
  		<s:select name="caSubject" list="#request.trustedCas" listKey="subjectString" listValue="subjectString" label="CA" />
  		
		<s:submit value="%{'Create user'}" disabled="false" />
</s:form>