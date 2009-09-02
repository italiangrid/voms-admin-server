<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

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
                </s:text>
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
                <span class="aupVersionName"><s:property value="aup.name"/> version <s:property value="aup.activeVersion.version"/></span>.
                Click <a href="${signAUPURL}">here</a> to sign the AUP.
              </div>
            </s:iterator>
          </dd>
        </s:if>
	</dl>		
</div>
<tiles2:insertTemplate template="personalInfoPane.jsp">
  <tiles2:putAttribute name="panelName" value="Your personal information"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="certificatePane.jsp">
  <tiles2:putAttribute name="panelName" value="Your certificates"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="mappingsRequestPane.jsp"/>
<tiles2:insertTemplate template="requestHistoryPane.jsp"/>
<tiles2:insertTemplate template="aupHistoryPane.jsp">
  <tiles2:putAttribute name="panelName" value="Your AUP acceptance history"/>
</tiles2:insertTemplate>


<%-- 

<h1>
Groups and roles you're in:
</h1>

<div class="voMembershipInfo">
<table class="table" cellpadding="0" cellspacing="0">   
    <tr class="tableHeaderRow">
      <td>Group name</td>
      <td>Roles</td>
    </tr>
    
    <s:iterator value="mappingsMap" var="mapping">
      <tr>
      <td width="50%">
          <div class="group">
            <s:property value="key"/>
          </div>
        </td>
       <td>
          <div class="roles">
            <s:iterator value="value" var="role">
              <div class="roleCell">
                <s:property value="name"/>
              </div>
            </s:iterator>
         </div>
      </td>
      </tr>
    </s:iterator>
</table>
</div>

<s:if test="not attributes.empty">
<h2>
Your generic attributes:
</h2>

<div class="genericAttributesInfo">
  <table class="table" cellpadding="0" cellspacing="0">
    
    <tr class="tableHeaderRow">
      <td>Attribute name</td>
      <td>Attribute value</td>
    </tr>
    
    <s:iterator value="attributes" var="attribute">
      <tr>
        <td>
          <div class="attributeName">
            <s:property value="name"/>
          </div>
        </td>
        <td>
          <div class="attributeValue">
            <s:property value="value"/>
          </div>
        </td>
      </tr>
    </s:iterator>
   </table>
</div>
</s:if>
--%>




 