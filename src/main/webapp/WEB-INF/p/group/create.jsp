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
<h1>Create a new group:</h1>

<div class="createTab">
  <voms:hasPermissions var="canCreate" 
            context="/${voName}" 
            permission="CONTAINER_READ|CONTAINER_WRITE"
            />
  
  <s:if test="#attr.canCreate">
    <s:actionerror/>
    
    <s:form 
      action="create"
      namespace="/group"
      validate="true"
    >
      
      <s:select list="#request['voGroups']" name="parentGroupName" label="Parent group"/>
      <s:token/>
      <s:textfield name="groupName" size="20" value="" label="Group name"/>
      
      <%--
      <s:textarea name="description" label="Group description"/>
      <s:checkbox name="isRestricted" label="Restricted?" value="true"/>
      --%>
      
      
      <s:submit value="%{'Create!'}" align="left"/>
    </s:form>
  </s:if>
</div>