<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="hasPendingSignAUPTasks()">
  
  <span class="blabel blabel-warning baseline">Pending sign AUP request</span>
  
  <s:set var="daysBeforeExpiration" value="getPendingSignAUPTask(#attr.defaultAUP).daysBeforeExpiration"/>
  
  <s:if test="#daysBeforeExpiration < 0">
   <span class="badge badge-error" style="font-size: smaller;">expired</span>
  </s:if>
  <s:elseif test="#daysBeforeExpiration < 3">
    <span class="badge badge-error" style="font-size: smaller;"><s:property value="#daysBeforeExpiration"/> days left</span>
  </s:elseif> 
  <s:else>
    <span class="badge badge-info" style="font-size: smaller;"><s:property value="#daysBeforeExpiration"/> days left</span>
  </s:else>
  
</s:if>
