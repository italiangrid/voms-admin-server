<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<h1>Welcome to ${voName}, <voms:formatDN dn="${currentAdmin.realSubject}" fields="CN"/>.</h1>

<s:if test="status == 'SUBMITTED'">
<p>
We have already received your membership request but are still waiting for 
you to confirm it. Check your inbox!
</p>
</s:if>
<s:else>
<p>
Your membership request is being handled by the VO administrators. You will
receive a notification of their decision regarding your request shortly.
</p>
</s:else>

