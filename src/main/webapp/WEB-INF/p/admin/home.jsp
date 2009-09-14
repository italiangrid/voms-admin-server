<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>
  Welcome to the <span class="voName">${voName}</span> VO, <voms:formatDN dn="${dn}" fields="CN"/>
</h1>

<s:if test="#attr.currentAdmin.voUser">
	<div style="float: right; margin-bottom: .5em">
		<s:url action="home" namespace="/user" var="userHomeURL"/>
		<s:a href="%{#userHomeURL}" cssClass="actionLink">Your vo user home</s:a>
	</div>
</s:if>
<div class="info-tab">
  <h2><span>Pending administrative requests</span></h2>
  <voms:div cssClass="content" id="pending-req-content">
  <tiles2:insertTemplate template="pendingRequests.jsp"/>  
  </voms:div>
</div>