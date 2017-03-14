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

<h1>
Welcome to the registration page for the <span class="voName">${voName}</span> VO.
</h1>

<p>
To access the VO resources, you must agree to the VO's Acceptable Usage Policy (AUP) rules.
<br/>
Please fill out all fields in the form below and click on the submit
button at the bottom of the page.
</p>
<p>
After you submit this request, you will receive an email with instructions on how to proceed. 
<br/>
<span style="font-weight: bold">
Your request will not be forwarded to the VO managers until you confirm that you have a valid email 
address by following those instructions.</span>
</p>

<p><span style="font-weight: bold">IMPORTANT</span>:</p>
<p style="margin-bottom: 2em">
By submitting this information you agree that it may be distributed to and stored by 
VO and site administrators. You also agree that action may be taken to confirm the information you provide 
is correct, that it may be used for the purpose of controlling access to VO resources and that it may be 
used to contact you in relation to this activity.
</p>

<s:form action="submit-request" validate="true" namespace="/register/orgdb">
	<s:token/>
  
  <h2 style="color: black">Your certificate subject (DN):</h2>
  <div class="highlight" style="font-size: 14px">
      <s:property value="requester.certificateSubject"/>
  </div>
  <h2 style="color: black">The CA that issued your certificate:</h2>
  <div class="regDN" style="font-size: 14px">
      <s:property value="requester.certificateIssuer"/>
  </div>
  <ul class="form">
    <li>
      <s:textfield name="name" label="%{'Your name'}" size="40" cssClass="registrationField" readonly="true" value="%{requester.name}"/>
    </li>
    <li>
      <s:textfield name="surname" label="%{'Your surname'}" size="40" cssClass="registrationField" readonly="true" value="%{requester.surname}"/>
    </li>
    <li>
      <s:textfield name="institution" label="%{'Your institution'}" size="40" cssClass="registrationField" readonly="true" value="%{requester.institution}"/>
    </li>
    <li>
      <s:textfield name="phoneNumber" label="%{'Your phone number'}" size="40" cssClass="registrationField" value="%{requester.phoneNumber}" />
    </li>
    <li>
      <s:textarea name="address" label="%{'Your address'}" rows="5" cols="40" cssClass="registrationField" value="%{requester.address}" />
    </li>
    <li>
      <s:textfield name="emailAddress" value="%{requester.emailAddress}" size="60" label="%{'Your email address'}" cssClass="registrationField" readonly="true"/>
    </li>
    <li>
     <h2 style="color: black">The VO Acceptable Usage Policy (AUP):</h2>
      <s:textarea rows="20" cols="80" value="%{currentAUPVersion.URLContent}" readonly="true"/>
    </li>
    <li class="aupAcceptance">
      <s:checkbox 
        name="aupAccepted"
        label="I confirm I have read and agree with the terms expressed in the VO Acceptable Usage Policy
      document displayed above."
        labelposition="right" 
        
      />
    </li>
    <li>
      <s:textarea name="userMessage" rows="3" cols="80" cssClass="registrationField" label="%{'Message for VO administrator'}"/>
    <li>
    <li>
     <s:submit align="left"/>
    </li>
  </ul>     
</s:form>