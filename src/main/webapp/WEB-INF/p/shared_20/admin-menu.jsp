<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>


<tiles2:insertTemplate template="menu.jsp"/>

<div id="admin-menu">
 
<ul>

  <li>
    Browse:
  </li>
  <li class="first-menu-item">
    <a href="<s:url action="search" namespace="/user"/>" class="vomsLink">Users</a> 
  </li>
  
  <li>
    <a href="<s:url action="search" namespace="/group"/>" class="vomsLink">Groups</a>
  </li>
  
  <li>
    <a href="<s:url action="search" namespace="/role"/>" class="vomsLink">Roles</a>
  </li>
  
  <li>
    <a href="<s:url action="search" namespace="/attribute"/>" class="vomsLink">Attributes</a>
  </li>
  <li>
    <a href="<s:url action="manage" namespace="/acl"/>" class="vomsLink">ACLs</a>
  </li>
  
  <li>
    <a href="<s:url action="load" namespace="/aup"/>"class="vomsLink">AUPs</a>
  </li>

</ul>
</div>