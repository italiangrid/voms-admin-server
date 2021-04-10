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
Please fill out all the fields in the form below and click on the submit
button at the bottom of the page.
</p>
<div>
  <h2>IMPORTANT:</h2>
  <div style="margin-top: 1em; line-height: 1.5em;">
  By submitting this information you agree that it may be distributed to and stored by 
  VO and site administrators.<br/>
  You also agree that action may be taken to confirm the information you provide 
  is correct, that it may be used for the purpose of controlling access to VO resources and that it may be 
  used to contact you in relation to this activity.
  </div>
  <div class="alert alert-error" style="margin-top: 1em; line-height: 1.5em">
  After you submit this request, you will receive an email with instructions on how to proceed.<br/>
  Your request will not be forwarded to the VO managers until you confirm that you have a valid email 
  address by following those instructions.
  </div>
</div>


<s:form action="submit-request" validate="true">
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
      <s:textfield name="name" label="%{'Given name'}" size="40" cssClass="registrationField"/>
    </li>
    <li>
      <s:textfield name="surname" label="%{'Family name'}" size="40" cssClass="registrationField"/>
    </li>
    <s:if test="requiredFields.contains('institution')">
      <li>
        <s:textfield name="institution" label="%{'Institution'}" size="40" cssClass="registrationField"/>
      </li>
    </s:if>
    <s:if test="requiredFields.contains('phoneNumber')">
      <li>
        <s:textfield name="phoneNumber" label="%{'Phone number'}" size="40" cssClass="registrationField"/>
      </li>
    </s:if>
    
    <s:if test="requiredFields.contains('address')">
      <li>
        <s:textarea name="address" label="%{'Address'}" rows="5" cols="40" cssClass="registrationField"/>
      </li>
    </s:if>
    
    <li>
      <s:textfield name="emailAddress" value="%{requester.emailAddress}" size="60" label="%{'Email address'}" cssClass="registrationField"/>
    </li>
    <li>
     <h2 style="color: black">The VO AUP:</h2>
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
     <s:submit align="left"/>
    </li>
  </ul>     
</s:form>