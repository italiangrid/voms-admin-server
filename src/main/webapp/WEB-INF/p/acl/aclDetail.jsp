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
  <s:set value="vomsContext" var="theVomsContext"/>
  <s:set value="showDefaultACL  == true" var="isDefaultACL"/>
</s:if>
<s:else>
    <s:set value="model.context" var="theVomsContext"/>
    <s:set value="model.defaultACL" var="isDefaultACL"/>
</s:else>


<h1><s:if test="#isDefaultACL">Default</s:if> ACL for context 
  <span class="aclContext">
    <s:property value="#theVomsContext"/>
  </span>
</h1>


<s:if test="#isDefaultACL">
  <voms:hasPermissions permission="ACL_READ|ACL_WRITE|ACL_DEFAULT" var="canEdit" context="${theVomsContext}" />
</s:if>
<s:else>
  <voms:hasPermissions permission="ACL_READ|ACL_WRITE" var="canEdit" context="${theVomsContext}" />
</s:else>

<s:if test="#attr.canEdit">
  <div id="addACLEntryBox">
  
    <s:if test="#isDefaultACL">
      <s:url action="add-default-entry" namespace="/acl" var="addACLEntryURL" method="input">
        <s:param name="aclGroupId" value="#theVomsContext.group.id"/>
      </s:url>
    
    </s:if>
    <s:else>
    
      <s:url action="add-entry" namespace="/acl" var="addACLEntryURL" method="input">
        <s:param name="aclId" value="id"/>
      </s:url>
      
    </s:else>
    
    
    <s:a href="%{addACLEntryURL}" cssClass="actionLink">Add entry</s:a>
  </div>
</s:if>


<s:if test="model == null">
  This ACL is not defined yet.  
</s:if>
<s:else>

  <div class="reloadable">

  <voms:hasPermissions permission="ACL_READ" var="canRead" context="${theVomsContext}" />

  <s:if test="not #attr.canRead">
    You don't have sufficient permissions to see the content of this ACL.
  </s:if>
  <s:else>
    <table class="table" cellpadding="0" cellspacing="0">
      <tr>
        <th>Administrator</th>
        <th>Container</th>
        <th>Membership</th>
        <th>ACL</th>
        <th>Attributes</th>
        <th>Requests</th>
        <th>Personal info</th>
        <th>Suspend</th>
        <th colspan="2"/>
      </tr>
      
      
      
      <s:iterator value="externalPermissions" var="permission">
        
        <%-- This set is needed for the printPermission tag --%>
        <s:set value="%{#permission}" scope="page" var="permission"/>
        
        <tr class="tableRow">
          <td width="40%">
            <div class="userDN">
              <voms:formatDN dn="${permission.key.dn}" fields="CN"/>
            </div>
            <div class="userCA">
              <voms:formatDN dn="${permission.key.ca.subjectString}" fields="CN"/>
            </div>
            <s:if test="%{#permission.key.emailAddress != null}">
            	<div class="aclEmail">
            		${permission.key.emailAddress}
            	</div>
            </s:if>
            </td>
            <voms:printPermission var="permission" />
            <td class="actions">
              <s:url action="edit-entry" namespace="/acl" var="editACLEntryURL" method="input">
                <s:param name="aclId" value="%{model.id}"/>
                <s:param name="adminId" value="key.id"/>
              </s:url>
              <s:if test="#attr.canEdit">
                <s:a href="%{editACLEntryURL}">edit</s:a>
              </s:if>
            </td>
            <td class="actions">
              <s:url action="delete-entry" namespace="/acl" var="deleteACLEntryURL" method="input">
                <s:param name="aclId" value="%{model.id}"/>
                <s:param name="adminId" value="key.id"/>
              </s:url>
              <s:if test="#attr.canEdit">
                <s:a href="%{deleteACLEntryURL}">delete</s:a>
              </s:if>
            </td>
        </tr>
      </s:iterator>
    </table>
  </s:else>
</div>
</s:else>
