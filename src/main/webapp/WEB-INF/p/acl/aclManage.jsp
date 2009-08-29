<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>


<div id="aclHelp" style="display: none">
  <tiles2:insertTemplate template="aclHelp.jsp"/>
</div>

<div id="aclHelpBorder">
  <div id="showACLHelpHandle">
    Show help
  </div>
</div>

<h1>Manage ACL for</h1>



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

<div id="aclShowPane">

  



</div>