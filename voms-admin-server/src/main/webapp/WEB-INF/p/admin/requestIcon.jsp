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
<s:if
  test="#request instanceof org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest">
  <s:if test="#request.status == @org.glite.security.voms.admin.persistence.model.request.Request$STATUS@SUBMITTED">
    <i class="fa fa-exclamation-triangle fa-lg warning-icon"></i>
  </s:if>
  <s:else>
      <i class="fa fa-user-plus default-icon fa-lg"></i>
  </s:else>
</s:if>

<s:elseif test="#request instanceof org.glite.security.voms.admin.persistence.model.request.GroupMembershipRequest">
  <i class="fa fa-users default-icon fa-lg"></i>
</s:elseif>

<s:elseif test="#request instanceof org.glite.security.voms.admin.persistence.model.request.RoleMembershipRequest">
  <i class="fa fa-graduation-cap default-icon fa-lg"></i>
</s:elseif>

<s:elseif
  test="#request instanceof org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest">
  <i class="fa fa-minus-square default-icon fa-lg"></i>
</s:elseif>

<s:elseif
  test="#request instanceof org.glite.security.voms.admin.persistence.model.request.CertificateRequest">
  <i class="fa fa-key default-icon fa-lg"></i>
</s:elseif>