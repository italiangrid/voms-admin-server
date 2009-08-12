<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>
 VO Membership request details
</h1>

<s:form>
<ul> 
  <li>
    <h2>Personal information</h2>
  </li>
  <li>
    Name: <s:property value="requesterInfo.name"/>
  </li>
  <li>
    Surname: <s:property value="requesterInfo.surname"/>
  </li>
  <li>
    Institution: <s:property value="requesterInfo.institution"/>
  </li>
  <li>
    Address: <s:property value="requesterInfo.address"/>
  </li>
  <li>
    Phone number: <s:property value="requesterInfo.phoneNumber"/>
  </li>
  <li>
    Email: <s:property value="requesterInfo.emailAddress"/>
  </li>
  
  <li><h2>Certificate information</h2></li>
  
  <li>
    Subject: <s:property value="requesterInfo.certificateSubject"/>
  </li>
  <li>
    Issuer: <s:property value="requesterInfo.certificateIssuer"/>
  </li>
  
  <li/>
  
  <li>
    Submitted on : <s:property value="creationDate"/>
  </li>
  
  <li>
    <s:radio list="{'approve', 'reject'}" 
      value="%{'reject'}" name="decision"/>
  </li>
  <li>
    <s:hidden name="requestId" value="%{model.id}"/>
    <s:submit/>
  </li>
</ul>
</s:form>