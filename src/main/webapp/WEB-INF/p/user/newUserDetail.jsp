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

<%--
<dl>
    <dt>Member since:</dt><dd><s:property value="creationTime"/></dd>
    <dt>Expiration time:</dt><dd><s:property value="endTime"/></dd>
</dl>
--%>

<tiles2:insertTemplate template="personalInfoPane.jsp">
  <tiles2:putAttribute name="panelName" value="Personal information"/>
</tiles2:insertTemplate>

<tiles2:insertTemplate template="certificatePane.jsp">
  <tiles2:putAttribute name="panelName" value="Certficates"/>
</tiles2:insertTemplate>
  
<tiles2:insertTemplate template="mappingsPane.jsp"/>

<tiles2:insertTemplate template="attributesPane.jsp"/>


