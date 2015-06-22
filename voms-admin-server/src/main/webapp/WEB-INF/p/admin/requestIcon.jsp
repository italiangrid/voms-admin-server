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