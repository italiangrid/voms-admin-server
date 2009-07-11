<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:url action="load" namespace="/acl" var="editACLURL">
  <s:param name="aclId" value="ACL.id"/>
</s:url>

<s:url action="load-default" namespace="/acl" var="editDefaultACLURL">
  <s:param name="aclGroupId" value="id"/>
</s:url>

<s:a href="%{editACLURL}">Edit ACL for this group</s:a>
<s:a href="%{editDefaultACLURL}">Edit default ACL for this group</s:a>