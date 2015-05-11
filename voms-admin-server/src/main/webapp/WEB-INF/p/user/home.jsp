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
  <s:if test="suspended">
    <div>
      <span class="blabel blabel-important">Suspended</span>  
      <span class="blabel blabel-invert-important"><s:property value="suspensionReason"/></span>
    </div>
  </s:if>
  
   <div class="expirationInfo cont">
    <tiles2:insertTemplate template="membershipExpirationDetail.jsp"/>
   </div>
            
   <div class="cont aupInfo">
    <tiles2:insertTemplate template="aupStatusDetail.jsp"/>
   </div>
        
   <s:if test="hasPendingSignAUPTasks()">
      <div class="alert alert-error">
        <strong>You have a pending request to sign the current version of the Acceptable Usage Policy (AUP) !</strong><br/>
        <s:iterator value="tasks.{? (#this instanceof org.glite.security.voms.admin.persistence.model.task.SignAUPTask 
        and #this.status.toString() != 'COMPLETED')}">
          <div style="padding-top: 1em">
            <s:url action="sign" namespace="/aup" method="input" var="signAUPURL">
              <s:param name="aupId" value="aup.id"/>
            </s:url>
            Click <a href="${signAUPURL}">here</a> to sign the AUP.
          </div>
        </s:iterator>
      </div>
    </s:if>			
</div>

<s:if test="#request.registrationEnabled">
	<s:if test="pendingMembershipRemovalRequests.empty">
  		<div style="clear: both; float: right; margin-bottom: .5em">
    		<a href="${requestMembershipRemovalURL}" class="actionLink">Apply for membership removal</a> 
  		</div>
  	</s:if>
</s:if>

<tiles2:insertTemplate template="personalInfoPane.jsp">
  <tiles2:putAttribute name="panelName" value="Your personal information"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="certificateRequestPane.jsp">
  <tiles2:putAttribute name="panelName" value="Your certificates"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="mappingsRequestPane.jsp"/>

<tiles2:insertTemplate template="attributesPane.jsp">
  <tiles2:putAttribute name="panelName" value="Your generic attributes"/>
</tiles2:insertTemplate>

<s:if test="#request.registrationEnabled">
  <tiles2:insertTemplate template="aupStatusPane.jsp">
    <tiles2:putAttribute name="panelName" value="Your AUP acceptance status"/>
  </tiles2:insertTemplate>
</s:if>

<s:if test="#request.registrationEnabled">
  <tiles2:insertTemplate template="requestHistoryPane.jsp">
	   <tiles2:putAttribute name="panelName" value="Your request history"/>
  </tiles2:insertTemplate>
</s:if>

</s:else>