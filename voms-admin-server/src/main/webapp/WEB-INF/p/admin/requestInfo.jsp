<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if
  test="#request instanceof org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest">
  <s:if
    test="#request.status == @org.glite.security.voms.admin.persistence.model.request.Request$STATUS@SUBMITTED">
	  wants to be a member of this VO but has <strong>NOT</strong> yet confirmed 
	  his identity.
	</s:if>
  <s:else>
    <s:if test="requesterInfo.getMultivaluedInfo('requestedGroup').size() > 0">
    wants to be a member of this VO and has requested to be assigned to 
    <s:property
        value="requesterInfo.getMultivaluedInfo('requestedGroup').size()" />
    VO groups.
  </s:if>
    <s:else>
    wants to be a member of this VO.
  </s:else>
  </s:else>
</s:if>

<s:elseif
  test="#request instanceof org.glite.security.voms.admin.persistence.model.request.GroupMembershipRequest">
  wants to be a member of group <span class="groupName"><s:property
      value="#request.groupName" /></span>.
  To motivate this request the user has provided the following reason:
  <blockquote class="request-reason">
    <s:property value="#request.userMessage" />
  </blockquote>
</s:elseif>


<s:elseif
  test="#request instanceof org.glite.security.voms.admin.persistence.model.request.RoleMembershipRequest">
    is requesting role 
    <span class="userRoleName"><s:property value="#request.groupName" />/Role=<s:property
      value="#request.roleName" /></span>.
    To motivate this request the user has provided the following reason:
    <blockquote class="request-reason">
    <s:property value="#request.userMessage" />
  </blockquote>
</s:elseif>

<s:elseif
  test="#request instanceof org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest">
	wants to be <strong>removed</strong> from this VO for
	the following reason:
  <div class="reason">
    <s:property value="#request.reason" />
  </div>
</s:elseif>

<s:elseif
  test="#request instanceof org.glite.security.voms.admin.persistence.model.request.CertificateRequest">
	wants to register a new certificate:
	<ul>
    <li><s:property value="#request.certificateSubject" /></li>
    <li><s:property value="#request.certificateIssuer" /></li>
  </ul>

</s:elseif>