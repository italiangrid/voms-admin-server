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

<voms:hasPermissions var="canDelete" context="vo" permission="rw"/>
<voms:hasPermissions var="canSuspend" context="vo" permission="SUSPEND"/>

<h2>
  <tiles2:insertTemplate template="userName.jsp"/>
	  <s:if test="suspended">
	    <span 
	     class="label label-danger pull-right"
	     data-toggle="tooltip" 
       data-placement="bottom" 
       title="User CANNOT get VOMS credentials for this VO">
       suspended</span>
	  </s:if>
	  <s:else>
	    <span class="label label-success pull-right" 
	     data-toggle="tooltip" 
	     data-placement="bottom" 
	     title="User can get VOMS credentials for this VO">active</span>
	  </s:else>
</h2>

<p>
<tiles2:insertTemplate template="userInfo.jsp"/>
</p>

<tiles2:insertTemplate template="userToolbar.jsp"/>

<tiles2:insertTemplate template="userPanels.jsp"/>