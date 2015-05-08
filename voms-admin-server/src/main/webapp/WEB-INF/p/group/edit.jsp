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


<h1>Edit group description</h1>

<voms:hasPermissions 
			var="canCreate" 
            context="vo" 
            permission="CONTAINER_READ|CONTAINER_WRITE" />

<s:if test="not #attr.canCreate">
You do not have enough privileges to edit the group description.
</s:if>
<s:else>

<div>
    <s:actionerror/>
    
	<s:form
	  id="saveGroupDescription" 
      action="save-group-description"
      namespace="/group"
      validate="true">
      
      <s:token/>
    
      <s:hidden name="groupId" value="%{id}" />
      <div class="groupName"><s:property value="name"/></div>
      <s:textarea name="theDescription" label="Group description" value="%{description}" cols="78"/>
      
      <s:submit value="%{'Update group description!'}" align="left"/>
    </s:form>
</div>

 </s:else>