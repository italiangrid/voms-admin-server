<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="not #attr.disableMembershipEndTime">
    <s:if test="hasExpired()">
      <span class="blabel blabel-important">Membership expired <s:property value="daysSinceExpiration" /> days ago</span>
    </s:if>
    <s:else>
      <s:if test="daysBeforeEndTime <= 15">
         <span class="blabel blabel-inverse"><s:property value="daysBeforeEndTime" /> days to membership expiration</span>
      </s:if>
      <s:else>
         <span class="middle"><s:property value="daysBeforeEndTime" /> days to membership expiration</span>
      </s:else>
    </s:else>
</s:if>