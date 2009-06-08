<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:set name="theme" value="'simple'" scope="page" />

<h3>Register!</h3> 

<s:form id="registrationForm" action="register" namespace="/register" cssClass="form">

  <ul>
    <li>
        <s:label for="request.userInfo.name" value="%{'Name'}" cssClass="desc"/>
        <s:textfield name="request.userInfo.name" size="15" required="true" cssClass=" field text"/>
    </li>
    <li>
        <s:label for="request.userInfo.surnname" value="%{'Surname'}" cssClass="desc"/>
        <s:textfield name="request.userInfo.surname" size="15" required="true" cssClass="field text"/>
    </li>
    
    <li>
      <s:label for="request.userInfo.address" value="%{'Address'}" cssClass="desc"/>
      <s:textarea name="request.userInfo.address" cols="15" rows="4" required="true" cssClass="field textarea"/>
    </li>
    
    <li>
      <s:label for="request.userInfo.phoneNumber" value="%{'Phone'}" cssClass="desc"/>
      <s:textfield name="request.userInfo.phoneNumber" size="15" required="true" cssClass="field text"/>
    </li>
    
    <li>
      <s:label for="request.userInfo.institution" value="%{'Institution'}" cssClass="desc"/>
      <s:textfield name="request.userInfo.institution" size="15" required="true" cssClass="field text"/>
    </li>
    
    <li>
      <s:label for="request.userInfo.emailAddress" value="%{'Email'}" cssClass="desc"/>
      <s:textfield name="request.userInfo.emailAddress" size="15" required="true" cssClass="field text"/>
    </li>
   </ul>
 
   <s:submit/>
  
</s:form>
