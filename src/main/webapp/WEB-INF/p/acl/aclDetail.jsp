<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1><s:if test="defaultACL">Default</s:if>ACL for context 
  <span class="aclContext">
    <s:property value="context"/>
  </span>
</h1>

<voms:hasPermissions permission="ACL_READ|ACL_WRITE" var="canEdit" context="${model.context}" />
<s:if test="#attr.canEdit">
  <div id="addACLEntryBox">
    <s:url action="add-entry" namespace="/acl" var="addACLEntryURL" method="input">
      <s:param name="aclId" value="id"/>
    </s:url>
    <s:a href="%{addACLEntryURL}" cssClass="vomsLink">Add entry</s:a>
  </div>
</s:if>

<table class="table" cellpadding="0" cellspacing="0">
  <tr class="tableHeaderRow">
    <td>Admin DN &amp; CA</td>
    <td>Container</td>
    <td>Membership</td>
    <td>ACL</td>
    <td>Attributes</td>
    <td>Requests</td>
    <td>Personal info</td>
    <td>Suspend</td>
    <td colspan="2"/>
  </tr>
  <s:iterator value="externalPermissions" var="permission">
    
    <%-- This set is needed for the printPermission tag --%>
    <s:set value="%{#permission}" scope="page" var="permission"/>
    
    <tr>
      <td width="40%">
        <div class="userDN">
          <voms:formatDN dn="${permission.key.dn}" fields="CN"/>
        </div>
        <div class="userCA">
          <voms:formatDN dn="${permission.key.ca.subjectString}" fields="CN"/>
        </div>
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

