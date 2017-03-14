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

<%-- 
  This is needed as this fragment is included both from user detail
  user home and user list page.
  In the home and user details the page, the reference to the user is 
  kept in the model.
  In the list users page is kept in #user.
 --%>

<s:if test="! #user">
  <s:set
    var="user"
    value="model" />
</s:if>

<voms:hasPermissions
  var="canReadPI"
  context="vo"
  permission="PERSONAL_INFO_READ" />

<div class="status-detail">
  <tiles2:insertTemplate template="suspensionDetail.jsp" />
  
  <s:if test="#attr.canReadPI or (#attr.currentAdmin.is(#user))">
    <tiles2:insertTemplate template="aupStatusDetail.jsp" />

    <s:if test="not #attr.disableMembershipEndTime">
      <tiles2:insertTemplate template="membershipExpirationDetail.jsp" />
    </s:if>
  </s:if>
</div>