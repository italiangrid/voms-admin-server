<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div class="usernameHeader">
  	<s:if test="name != null and surname != null">
		<s:property value="name+ ' ' +surname"/>
			<span class="institution">
    			(<s:property value="institution"/>)  
  			</span>
	</s:if>
	<s:else>	
		<s:set value="dn" var="userDN"/>
		User
		<span class="highlight">
			<voms:formatDN dn="${userDN}" fields="CN"/>
		</span>
	</s:else>
</div>

<s:if test="suspended">
<div class="userSuspensionInfo">
  This user is currently <span class="suspended">suspended</span>.
  <div>Reason: <span class="suspensionReason"> ${suspensionReason }</span></div>
</div>
</s:if>
<voms:hasPermissions var="canDelete" context="vo" permission="rw"/>
<voms:hasPermissions var="canSuspend" context="vo" permission="SUSPEND"/>


<tiles2:insertTemplate template="membershipExpiration.jsp"/>

<div class="userAdminActions">
  <s:if test="#attr.canSuspend">
    <s:url action="suspend" var="suspendUsrURL" method="input">
      <s:param name="userId" value="id"/>
    </s:url>
    <s:url action="restore" var="restoreUsrURL">
      <s:param name="userId" value="id"/>
    </s:url>
    <s:if test="suspended">
      <a href="${restoreUsrURL}" class="actionLink">Restore this user</a>
    </s:if>
    <s:else>
      <a href="${suspendUsrURL}" class="actionLink">Suspend this user</a>
    </s:else>
    
    </s:if>
    
	<s:if test="#attr.canDelete">
		<s:url action="delete" var="deleteUsrURL">
			<s:param name="userId" value="id"/>
		</s:url>
	
		<a href="${deleteUsrURL}" class="actionLink" onclick="openConfirmDialog(this, 'deleteUserDialog','${shortName}'); return false">Delete this user</a>
	
	</s:if>
</div>
<tiles2:insertTemplate template="personalInfoPane.jsp">
  <tiles2:putAttribute name="panelName" value="Personal information"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="certificatePane.jsp">
  <tiles2:putAttribute name="panelName" value="Certificates"/>
</tiles2:insertTemplate>
  
<tiles2:insertTemplate template="mappingsPane.jsp"/>

<tiles2:insertTemplate template="attributesPane.jsp">
	<tiles2:putAttribute name="panelName" value="Generic Attributes"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="aupHistoryPane.jsp">
  <tiles2:putAttribute name="panelName" value="AUP acceptance history"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="requestHistoryPane.jsp">
	  <tiles2:putAttribute name="panelName" value="Request history"/>
</tiles2:insertTemplate>


