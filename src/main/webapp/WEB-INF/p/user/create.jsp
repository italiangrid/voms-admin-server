<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<h1>Create a new user:</h1>

<s:form validate="true">
		<s:token/>
		
		<s:textfield name="theName" label="Name" size="40"
			cssClass="text" value="%{name}" disabled="false"/>
			
		<s:textfield name="theSurname" disabled="false" label="Surname" size="40"
			cssClass="text" value="%{surname}"/>
			
		<s:textfield name="theInstitution" disabled="false" label="Institution"
			size="40" cssClass="text" value="%{institution}"/>
			
		<s:textarea name="theAddress" disabled="false" label="Address" rows="4"
			cols="30" cssClass="text" value="%{address}"/>
			
		<s:textfield name="thePhoneNumber" disabled="false" label="Phone" size="40"
			cssClass="text" value="%{phoneNumber}"/>
			
		<s:textfield name="theEmailAddress" disabled="false" label="Email"
			size="40" cssClass="text" value="%{emailAddress}"/>
		
		<h2>Certificate information</h2>
		<s:textfield name="subject" size="45" label="Subject"/>
  		<s:select name="caSubject" list="#request.trustedCas" listKey="subjectString" listValue="subjectString" label="CA"/>
  		
		<s:submit value="%{'Create user'}" disabled="false" />
</s:form>