<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="requesterInfo.name != null">
  <a href="mailto:<s:property value="requesterInfo.emailAddress"/>">
      ${requesterInfo.name} ${requesterInfo.surname}
  </a> 
  <s:if test="requesterInfo.institution != null">
      <span class="institution">( ${requesterInfo.institution} )</span>
  </s:if>
</s:if>
