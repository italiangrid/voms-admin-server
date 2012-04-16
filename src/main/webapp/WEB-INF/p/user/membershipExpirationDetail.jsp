<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="not #attr.disableMembershipEndTime">
    <s:if test="hasExpired()">
      <span class="blabel blabel-important">Membership expired <s:property value="daysSinceExpiration" /> days ago</span>
    </s:if>
    <s:else>
      <s:if test="daysBeforeEndTime <= 15">
         <span class="blabel blabel-warning"><s:property value="daysBeforeEndTime" /></span>
      </s:if>
      <s:else>
         <span class="middle"><s:property value="daysBeforeEndTime" /></span>
      </s:else>
          <span style="vertical-align: middle">days to membership expiration.</span>
    </s:else>
      
</s:if>