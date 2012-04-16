<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="hasPendingSignAUPTasks()">
  <span class="blabel blabel-warning">Pending sign AUP request</span>
</s:if>
