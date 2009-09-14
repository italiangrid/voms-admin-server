<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<h1>Create a new user:</h1>

<p>All the fields below are required.</p>
<s:form validate="true">
		<s:token/>
		
		<s:textfield name="theName" label="Name" size="40"
			value="%{name}" disabled="false" cssClass="registrationField"/>
			
		<s:textfield name="theSurname" disabled="false" label="Surname" size="40"
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