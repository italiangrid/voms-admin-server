<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div class="header1">
Add an acl entry to 
<s:if test="defaultACL">default</s:if> ACL for context: <span class="highlight">
  <s:property value="context"/>
</span>

</div>

<s:form action="add-entry" namespace="/acl">
  <s:hidden name="aclId" value="%{model.id}"/>
    <table class="form" cellspacing="5">
      <tr>
      <td colspan="3">
        <div class="header3">
          Add an ACL entry for a:
        </div>
      </td>
    </tr>
    <tr>
      <s:if test="#request.voUsers.empty">
        <s:set var="entryTypeList" value="%{#{'non-vo-user':'A non VO member', 
          'role-admin':'VO members with a specific role', 
          'group-admin':'VO members belonging to a specific VOMS group', 
          'anyone':'Anyone'}}"/>
     </s:if>
     <s:else>
        <s:set var="entryTypeList" value="%{#{'vo-user':'A VO member','non-vo-user':'A non VO member', 
          'role-admin':'VO members with a specific role', 
          'group-admin':'VO members belonging to a specific VOMS group', 
          'anyone':'Anyone'}}"/>
     </s:else>
      <td>
      <s:radio 
        name="entryType" 
        list="#entryTypeList" 
        cssClass="aclEntrySelector" 
        labelposition="top"
        />
      </td>
    </tr>
    
    <tr>
      <td>
        
        <div id="vo-user" class="entryType">
          <s:select name="userId" list="#request.voUsers" listKey="id" listValue="defaultCertificate.subjectString" theme="simple"/>
        </div>
        <div id="non-vo-user" class="entryType">
          <s:textfield name="dn" size="40" label="DN" labelposition="top"/>
          <s:select name="caId" list="#request.voCAs" listKey="id" listValue="subjectString" labelposition="top" label="CA"/>
          <s:textfield name="emailAddress" size="40" label="Email" labelposition="top"/>
        </div>
        <div id="role-admin" class="entryType">
          Any user with role <s:select name="roleId" list="#request.voRoles" listKey="id" listValue="name" theme="simple"/> in group
          <s:select name="roleGroupId" list="#request.voGroups" listKey="id" listValue="name" theme="simple"/>.
        </div>
        <div id="group-admin" class="entryType">
          The members of the <s:select name="groupId" list="#request.voGroups" listKey="id" listValue="name" theme="simple"/> group.
        </div>
        
        <div id="anyone" class="entryType">
          
        </div>
      </td>
    </tr>
    <tr>
      <td colspan="4">
        <h2>granting the following permissions</h2>
      </td>
    </tr>
    
    <%@include file="/WEB-INF/p/acl/permissionFormFieldset.jsp"%>
    
    <tr>
      <td colspan="4"/>
    </tr>
    <s:if test="not model.defaultACL">
      <tr>
        <td><h2>propagate entry to children contexts:</h2></td>
        <td><s:checkbox name="propagate"/></td>
      </tr>
    </s:if>
    
    <tr>
      <td>
        <s:submit value="%{'Add entry'}"/>
      </td>
    </tr>
    </table>
</s:form>