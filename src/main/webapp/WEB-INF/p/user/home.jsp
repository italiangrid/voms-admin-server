<%--

    Copyright (c) Members of the EGEE Collaboration. 2006-2009.
    See http://www.eu-egee.org/partners/ for details on the copyright holders.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    Authors:
    	Andrea Ceccanti (INFN)

--%>
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
            <s:iterator value="tasks.{? (#this instanceof org.glite.security.voms.admin.persistence.model.task.SignAUPTask 
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

<tiles2:insertTemplate template="certificateRequestPane.jsp">
  <tiles2:putAttribute name="panelName" value="Your certificates"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="attributesPane.jsp">
  <tiles2:putAttribute name="panelName" value="Your generic attributes"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="mappingsRequestPane.jsp"/>

<s:if test="#request.registrationEnabled">
  <tiles2:insertTemplate template="aupHistoryPane.jsp">
    <tiles2:putAttribute name="panelName" value="Your AUP acceptance history"/>
  </tiles2:insertTemplate>
</s:if>

<s:if test="#request.registrationEnabled">
  <tiles2:insertTemplate template="requestHistoryPane.jsp">
	   <tiles2:putAttribute name="panelName" value="Your request history"/>
  </tiles2:insertTemplate>
</s:if>

</s:else>