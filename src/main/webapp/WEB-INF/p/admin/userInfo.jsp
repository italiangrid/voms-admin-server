<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="requesterInfo.name != null">
  <div class="name">
    ${requesterInfo.name} ${requesterInfo.surname} 
  </div>
  <div class="institution" style="margin-bottom: 1em">
    ${requesterInfo.institution}
  </div>
</s:if>

<div class="certSubject"><voms:formatDN
  dn="${requesterInfo.certificateSubject}"
  fields="CN" /></div>

<div class="certIssuer"><voms:formatDN
  dn="${requesterInfo.certificateIssuer}"
  fields="CN" /></div>