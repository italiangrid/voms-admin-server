<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015

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
<h4>Request information</h4>

<dl class="req-detail-list">
  <dt>Type</dt>
  <dd>${request.typeName}</dd>
  <dt>ID</dt>
  <dd>${request.id}</dd>
  <dt>Status</dt>
  <dd>${request.status}</dd>
  <dt>Submitted on</dt>
  <dd>${request.creationDate}</dd>
</dl>

<s:if
  test="not #request instanceof org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest">
  <dl class="req-detail-list">
    <s:if
      test="#request instanceof org.glite.security.voms.admin.persistence.model.request.GroupMembershipRequest">
      <dt>Requested group</dt>
      <dd>${request.groupName}</dd>

      <dt>Reason</dt>
      <dd>${request.userMessage}</dd>
    </s:if>

    <s:if
      test="#request instanceof org.glite.security.voms.admin.persistence.model.request.RoleMembershipRequest">
      <dt>Requested role</dt>
      <dd>${request.groupName}/Role=${request.roleName}</dd>

      <dt>Reason</dt>
      <dd>${request.userMessage}</dd>
    </s:if>

    <s:if
      test="#request instanceof org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest">
      <dt>Reason</dt>
      <dd>${request.reason}</dd>
    </s:if>

    <s:if
      test="#request instanceof org.glite.security.voms.admin.persistence.model.request.CertificateRequest">
      <dt>Req. cert. subject</dt>
      <dd>${request.certificateSubject}</dd>
      <dt>Req. cert. issuer</dt>
      <dd>${request.certificateIssuer}</dd>
    </s:if>
  </dl>
</s:if>

<h4>Personal information</h4>
<s:if test="#request.requesterInfo.voMember">
  <p>
    See
    <s:url
      action="load"
      namespace="/user"
      var="loadUserAction">
      <s:param
        name="userId"
        value='%{#request.requesterInfo.getInfo(@org.glite.security.voms.admin.persistence.model.request.RequesterInfo@VO_USER_ID)}' />
    </s:url>
    <a
      href="${loadUserAction}"
      target="_blank"> user profile </a>
  </p>
</s:if>
<s:else>
  <dl class="req-detail-list">
    <dt>Address</dt>
    <dd>${request.requesterInfo.address}</dd>
    <dt>Phone number</dt>
    <dd>${request.requesterInfo.phoneNumber}</dd>
  </dl>

</s:else>

<h4>Certificate information</h4>
<dl class="req-detail-list">
  <dt>Subject</dt>
  <dd>${request.requesterInfo.certificateSubject}</dd>
  <dt>Issuer (CA)</dt>
  <dd>${request.requesterInfo.certificateIssuer}</dd>
</dl>

<s:if
  test="#request instanceof org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest">
  <s:if
    test="#request.status == @org.glite.security.voms.admin.persistence.model.request.Request$STATUS@SUBMITTED">
    <p style="clear: both;">
      <span class="warning-text"><i
        class="fa fa-exclamation-triangle fa-lg"></i> The user has not yet
        confirmed his identity.</span> You can approve the request anyway if you wish,
      or reject it if you want the user to submit a new membership request.
    </p>
  </s:if>
  <tiles2:insertTemplate template="attributeRequestManagement.jsp" />
</s:if>
