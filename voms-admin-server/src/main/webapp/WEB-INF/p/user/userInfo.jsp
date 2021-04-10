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
<voms:hasPermissions var="canReadPI" context="vo" permission="PERSONAL_INFO_READ" />
<s:if test="#attr.canReadPI or #attr.currentAdmin.is(#user)">
  <div class="personal-info">
    <s:if test="name != null and name != ''">
      <div class="username">
        <s:property value="%{#user.name + ' ' + #user.surname}" />
      </div>
      <s:if test="#attr.currentAdmin.is(#user)">
        <div class="badge-container">
             <span class="blabel">you</span>
        </div>
      </s:if>
    </s:if>
    <s:else>
      <div class="username">
        User
        <s:property value="id" />
      </div>
    </s:else>
    <s:if test="institution != null and institution != ''">
      <div class="institution" style="padding-top: .5em">
        <s:property value="institution" />
      </div>
    </s:if>
    <div class="email">
      <a href="mailto:<s:property value="emailAddress"/>">
        <s:property value="emailAddress" />
      </a>
    </div>
    <tiles2:insertTemplate template="membershipStatusDetail.jsp" />
  </div>
  <div style="font-weight: bold; color: #505050;">Certificates:</div>
</s:if>
<s:else>
  <div class="username">
    User <s:property value="id" />
  </div>
</s:else>
<ol class="certificate-info">
  <s:iterator value="certificates" var="cert">
    <li>
      <div class="userDN <s:if test="suspended">suspended-cert</s:if>">
        <s:set value="subjectString" var="thisCertDN" />
        ${thisCertDN}
      </div>
      <div class="userCA <s:if test="suspended">suspended-cert</s:if>">
        <s:set value="ca.subjectString" var="thisCertCA" />
        ${thisCertCA}
      </div>
    </li>
  </s:iterator>
</ol>