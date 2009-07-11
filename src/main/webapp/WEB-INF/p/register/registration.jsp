<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>
Welcome to voms-admin registration for the <span class="voName"> ${voName}</span> VO.
</h1>

<p>
To access the VO resources, you must agree to the VO's Usage Rules. 
Please fill out all fields in the form below and click on the submit
button at the bottom of the page.
</p>
<p>
After you submit this request, you will receive an email with instructions on how to proceed. 
Your request will not be forwarded to the VO managers until you confirm that you have a valid email 
address by following those instructions.
</p>

<p>IMPORTANT:</p>
<p>
By submitting this information you agree that it may be distributed to and stored by 
VO and site administrators. You also agree that action may be taken to confirm the information you provide 
is correct, that it may be used for the purpose of controlling access to VO resources and that it may be 
used to contact you in relation to this activity.
</p>

<s:form action="submit-request" validate="true">
  <h2>Your distinguished name (DN):</h2>
  <div class="regDN">
      <s:property value="requester.certificateSubject"/>
  </div>
  <h2>Your CA:</h2>
  <div class="regDN">
      <s:property value="requester.certificateIssuer"/>
  </div>
  <ul class="form">
    <li>
      <s:textfield name="name" label="%{'Your name'}" size="80"/>
    </li>
    <li>
      <s:textfield name="surname" label="%{'Your surname'}" size="80"/>
    </li>
    <li>
      <s:textfield name="institution" label="%{'Your institution'}" size="80"/>
    </li>
    <li>
      <s:textfield name="phoneNumber" label="%{'Your phoneNumber'}" size="80"/>
    </li>
    <li>
      <s:textarea name="address" label="%{'Your address'}" rows="5" cols="80"/>
    </li>
    <li>
      <s:textfield name="requester.emailAddress" value="%{requester.emailAddress}" size="80" label="%{'Your email address'}"/>
    </li>
    <li>
      <s:label for="aupAccepted">
      I confirm I have read and agree with the terms expressed in the <a href="#">VO Acceptable Usage Policy
      document</a>
      </s:label>
      <s:checkbox name="aupAccepted"/>
    </li>
    <li>
     <s:submit/>
    </li>
  </ul>     
</s:form>