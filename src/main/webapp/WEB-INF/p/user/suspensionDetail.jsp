<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="suspended">
      <span class="blabel blabel-important">Suspended</span>  
      <span class="blabel blabel-invert-important"><s:property value="suspensionReason"/></span>
</s:if>
