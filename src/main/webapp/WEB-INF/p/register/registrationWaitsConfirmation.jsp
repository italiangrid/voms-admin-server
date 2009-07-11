<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<h1>Welcome to ${voName}, <voms:formatDN dn="${currentAdmin.realSubject}" fields="CN"/>.</h1>

<p>
We have already received your membership request but are still waiting for 
you to confirm it. Check your inbox!
</p>


