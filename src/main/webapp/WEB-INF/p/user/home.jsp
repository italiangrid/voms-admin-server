<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="model == null">
	You're not a VO user. You'll see nothing around here.
</s:if>
<s:else>

<h1>
  Welcome to the <span class="voName">${voName}</span> VO,
</h1>

<div id="welcomeUserName">
  	<s:if test="name != null and surname != null">
		<s:property value="name+ ' ' +surname"/>
			<span class="institution">
    			(<s:property value="institution"/>)  
  			</span>
	</s:if>
	<s:else>
		<s:property value="dn"/>
	</s:else>
</div>

<s:url action="request-membership-removal" method="input" var="requestMembershipRemovalURL">
  <s:param name="userId" value="id"/>
</s:url>

<div class="membershipInfo">
	<dl>
		<s:if test="suspended">		
			<dt>Your membership is currently <span class="suspensionWarning">suspended</span> for the following reason:</dt>
			<dd class="suspensionReason"><s:property value="suspensionReason"/></dd>
		</s:if>
		<s:else>
			<dt>Your membership will expire on:</dt>
			<dd class="userMembershipEndTime">
                <s:text name="format.datetime">
                  <s:param value="endTime"/>
                </s:text>.
			</dd>
		</s:else>
        
        <s:if test="hasPendingSignAUPTasks()">
          <dt>You have pending sign AUP tasks for:</dt>
          <dd>
            <s:iterator value="tasks.{? (#this instanceof org.glite.security.voms.admin.model.task.SignAUPTask 
            and #this.status.toString() != 'COMPLETED')}">
              <div>
                <s:url action="sign" namespace="/aup" method="input" var="signAUPURL">
                  <s:param name="aupId" value="aup.id"/>
                </s:url>
                <span class="aupVersionName"><s:property value="aup.name"/> version <s:property value="aup.activeVersion.version"/></span>.<br/>
                Click <a href="${signAUPURL}">here</a> to sign the AUP. Sign it before <span style="font-weight: bold">
                	<s:text name="format.datetime">
                		<s:param value="expiryDate"/>
                	</s:text></span>, or your membership will be suspended.
              </div>
            </s:iterator>
          </dd>
        </s:if>
	</dl>		
</div>

<s:if test="#request.registrationEnabled">
  <div style="clear: both; float: right; margin-bottom: .5em">
    <a href="${requestMembershipRemovalURL}" class="actionLink">Apply for membership removal</a> 
  </div>
</s:if>

<tiles2:insertTemplate template="personalInfoPane.jsp">
  <tiles2:putAttribute name="panelName" value="Your personal information"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="certificatePane.jsp">
  <tiles2:putAttribute name="panelName" value="Your certificates"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="attributesPane.jsp">
  <tiles2:putAttribute name="panelName" value="Your generic attributes"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="mappingsRequestPane.jsp"/>

<tiles2:insertTemplate template="aupHistoryPane.jsp">
  <tiles2:putAttribute name="panelName" value="Your AUP acceptance history"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="requestHistoryPane.jsp">
	  <tiles2:putAttribute name="panelName" value="Your request history"/>
</tiles2:insertTemplate>
</s:else>