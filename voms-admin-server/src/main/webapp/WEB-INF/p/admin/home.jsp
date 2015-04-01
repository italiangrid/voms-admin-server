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

<h2>VO ${voName} admin dashboard</h2>

<s:if test="model == null">
	You are not a VO admin. You will see nothing around here.
</s:if>
<s:else>
  <p>
    Welcome to the ${voName} virtual organization management page,
    ${currentAdmin.name}.
  </p>
  
	<s:if test="#attr.currentAdmin.voUser">
		<div style="float: right; margin-bottom: .5em">
			<s:url action="home" namespace="/user" var="userHomeURL"/>
			<s:a href="%{#userHomeURL}" cssClass="actionLink">Your vo user home</s:a>
		</div>
	</s:if>
	
	<voms:hasPermissions var="canManage" context="vo" permission="REQUESTS_READ|REQUESTS_WRITE"/>
	
	<s:if test="#attr.canManage">
		<div class="info-tab">
	  		<voms:div cssClass="content" id="pending-req-content">
	  			<tiles2:insertTemplate template="pendingRequests.jsp"/>  
	  		</voms:div>
		</div>
	</s:if>
	<s:else>
		You do not have enough permissions to see administrative requests for this VO.
	</s:else>
</s:else>