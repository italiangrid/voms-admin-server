<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div id="menu">
 
<ul>

  <li class="home-menu-item">
    <a href="<s:url action="login" namespace="/home"/>">Home</a>  
  </li>
  
  <li>
    <a href="<s:url action="search" namespace="/role"/>">Configuration Info</a>
  </li>
  
  <li class="last-menu-item">
    <a href="<s:url action="search" namespace="/attribute"/>">Other VOs on this server</a>
  </li>


</ul>
</div>

<div id="admin-menu">
 
<ul>

  <li>
    Manage:
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