<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="model == null">
	You are not a VO admin. You will see nothing around here.
</s:if>
<s:else>
	<h1>
	  Welcome to the <span class="voName">${voName}</span> VO, <voms:formatDN dn="${dn}" fields="CN"/>
	</h1>
	
	<s:if test="#attr.currentAdmin.voUser">
		<div style="float: right; margin-bottom: .5em">
			<s:url action="home" namespace="/user" var="userHomeURL"/>
			<s:a href="%{#userHomeURL}" cssClass="actionLink">Your vo user home</s:a>
		</div>
	</s:if>
	
	<voms:hasPermissions var="canManage" context="vo" permission="REQUESTS_READ|REQUESTS_WRITE"/>
	
	<s:if test="#attr.canManage">
		<div class="info-tab">
	  		<h2><span>Pending administrative requests</span></h2>
	  		<voms:div cssClass="content" id="pending-req-content">
	  			<tiles2:insertTemplate template="pendingRequests.jsp"/>  
	  		</voms:div>
		</div>
	</s:if>
	<s:else>
		You do not have enough permissions to see administrative requests for this VO.
	</s:else>
</s:else>