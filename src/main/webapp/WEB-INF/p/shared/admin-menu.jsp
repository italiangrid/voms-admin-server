<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<ul id="admin-menu">
  <li class="home-menu-item">
    <a href="#home">Home</a>
  </li>
  
  <li class="menu-separator" />
  
  <li class="top-menu-item">
    <a href="<s:url action="search" namespace="/user"/>">Users</a>
  </li>
  
  <li>  
    <a href="<s:url action="search" namespace="/group"/>">Groups</a>
  </li>
  
  <li>
    <a href="<s:url action="search" namespace="/role"/>">Roles</a>
  </li>
  
  <li>
    <a href="<s:url action="search" namespace="/attribute"/>">Attributes</a>
  </li>
  
  <li>
    <a href="#acls">ACLs</a>
  </li>
  
  <li>
    <a href="#ap">AUPs</a>
  </li>
  
  <li class="bottom-menu-item">
    <a href="#subscriptions">Subscriptions</a>
  </li>
</ul>