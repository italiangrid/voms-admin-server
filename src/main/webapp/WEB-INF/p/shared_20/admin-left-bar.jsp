<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div id="leftBar">

<div id="adminLeftBar">
  <div class="leftBarHeader">
    Manage
  </div>
  
  <a href="<s:url action="search" namespace="/user"/>" class="vomsLink">Users</a>
  <a href="<s:url action="search" namespace="/group"/>" class="vomsLink">Groups</a>
  <a href="<s:url action="search" namespace="/role"/>" class="vomsLink">Roles</a>
  <a href="<s:url action="search" namespace="/attribute"/>" class="vomsLink">Attributes</a>
  <a href="<s:url action="load" namespace="/aup"/>"class="vomsLink">AUPs</a>
</div>

</div>