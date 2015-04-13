<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="name != null and surname != null">
  <span class="username"><s:property value="name+ ' ' +surname"/></span>
</s:if>
<s:else>
  <s:set value="dn" var="userDN"/>
  <span class="userDN"><voms:formatDN dn="${userDN}" fields="CN"/></span>
</s:else>