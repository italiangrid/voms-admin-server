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
<%--

<div id="aclHelp" style="display: none">
  <tiles2:insertTemplate template="aclHelp.jsp"/>
</div>

<div id="aclHelpBorder">
  <div id="showACLHelpHandle">
    Show help
  </div>
</div>
 --%>


<s:if test="! #attr.voGroups.empty">
<h1>Manage the Access Control List for</h1>



<div>
    
  <s:form id="aclSelectionForm" action="ajax-load" namespace="/acl" theme="simple">
    <s:label for="groupId" cssClass="label" value="%{'Group:'}" theme="simple"/>
    <s:select 
      name="aclGroupId"
      list="#attr.request.voGroups"
      listKey="id" listValue="name" 
      label="Group" 
      id="aclGroupSelector"  
      onchange="ajaxSubmit($('#aclSelectionForm').map(function(){return this;}),'aclShowPane');"/>
    <s:label for="roleId" cssClass="label" value="%{'Role:'}" theme="simple"/>
    <s:select 
      list="#attr.request.voRoles" 
      name="aclRoleId"
      label="Role" 
      id="aclRoleSelector"
      listKey="id" listValue="name" 
      headerKey="-1" 
      headerValue="--" 
      onchange="ajaxSubmit($('#aclSelectionForm').map(function(){return this;}),'aclShowPane');"/>
      
      
      <s:label for="showDefaultACL" cssClass="label" value="%{'Show default ACL:'}" theme="simple"/>
        <s:checkbox 
          name="showDefaultACL"  
          onchange="ajaxSubmit($('#aclSelectionForm').map(function(){return this;}),'aclShowPane');"
          id="defaultACLSelector"
          theme="simple"
          />
  </s:form>
</div>

<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp"/>
<div id="aclShowPane"></div>

</s:if>
<s:else>
	You don't have the rights to list the groups structure (and their ACL in this VO).
</s:else>



  



</div>