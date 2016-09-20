<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
 
 <tiles2:insertTemplate template="../shared/errorsAndMessages.jsp"/>
<s:form>
<h1>
Add an entry to the 
<s:if test="defaultACL">default</s:if> ACL of context: <span class="groupname">
  <s:property value="context"/></span> for 
  <s:select 
        name="entryType" 
        list="entryTypeMap" 
        cssClass="aclEntrySelector" 
        id="aclEntryTypeSelector" theme="simple"
        value="%{entryType}"
        />

</h1>
  	<s:token/>
  	<s:hidden name="aclId" value="%{model.id}"/>
    
    <table class="form noborder" cellspacing="0" cellpadding="0">
      <tr>
      <td colspan="4">
        <div class="label">
          
        </div>
      </td>
    </tr>
    
    <tr>
      <td class="entryTypeSelectorRow">
        
        <div id="aclPrincipalSelector">
          <div id="vo-user" class="entryType">
            <s:select name="userId" list="#request.voUsers" listKey="id" listValue="defaultCertificate.subjectString" label="i.e., the VO member whose certificate subject is"/>
          </div>
          <div id="non-vo-user" class="entryType">
            <s:textfield name="dn" size="40" label="Certificate subject" labelposition="top"/>
            <s:select name="caId" list="#request.voCAs" listKey="id" listValue="subjectString" labelposition="top" label="CA" id="caSelect"/>
            <s:textfield name="emailAddress" size="40" label="Email" labelposition="top"/>
          </div>
          <div id="role-admin" class="entryType">
            <s:select name="roleId" list="#request.voRoles" listKey="id" listValue="name" label="i.e., any user with role"/>
            <s:select name="roleGroupId" list="#request.voGroups" listKey="id" listValue="name" label="in group"/>
          </div>
          <div id="group-admin" class="entryType">
            <s:select name="groupId" list="#request.voGroups" listKey="id" listValue="name" label="All the members of group"/>
          </div>
          
          <div id="anyone" class="entryType">
            
          </div>
        </div>
      </td>
    </tr>
    </table>
    <table class="form noborder" cellspacing="5">
    <tr>
      <td colspan="4">
        <h1>granting the following permissions</h1>
      </td>
    </tr>
    
    <tiles2:insertTemplate template="permissionFormFieldset.jsp"/>
    
    <tr>
      <td colspan="4"/>
    </tr>
    <s:if test="not model.defaultACL and model.context.groupContext">
      <tr>
        <td><h1 id="propagateHandle" class="clickable">Propagate entry to children contexts:</h1></td>
        <td><s:checkbox name="propagate" theme="simple" id="propagateCheckbox" value="true"/></td>
      </tr>
    </s:if>
    <tr>
      <td colspan="4">
        <s:submit value="%{'Add entry'}" id="addACLEntrySubmit"/>
      </td>
    </tr>
    </table>
</s:form>