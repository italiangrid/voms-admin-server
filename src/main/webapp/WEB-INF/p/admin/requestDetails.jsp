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