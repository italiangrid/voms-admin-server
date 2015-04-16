<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<div class="requestType">
<s:if test="#request instanceof org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest">
  VO membership
</s:if>
<s:elseif test="#request instanceof org.glite.security.voms.admin.persistence.model.request.GroupMembershipRequest">
  Group membership
</s:elseif>
<s:elseif test="#request instanceof org.glite.security.voms.admin.persistence.model.request.RoleMembershipRequest">
  Role assignment
</s:elseif>
<s:elseif test="#request instanceof org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest">
  Membership removal
</s:elseif>
<s:elseif test="#request instanceof org.glite.security.voms.admin.persistence.model.request.CertificateRequest">
  New certificate
</s:elseif>
</div>