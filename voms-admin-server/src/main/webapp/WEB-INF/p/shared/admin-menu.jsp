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


<tiles2:insertTemplate template="menu.jsp"/>

<voms:hasPermissions 
    var="isVOAdmin"
    context="vo"
    permission="ALL"/>
    
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
  
  <s:if test="#request.registrationEnabled">
  
    <li>
      <a href="<s:url action="load" namespace="/aup"/>"class="vomsLink">AUPs</a>
    </li>
    
    <s:if test="#attr.isVOAdmin">
      <li>
        <a href="<s:url action="index" namespace="/manager"/>"class="vomsLink">Group managers</a>
      </li>
      
      <li>
        <a href="<s:url action="index" namespace="/audit"/>"class="vomsLink">Audit log</a>
      </li>
    </s:if>
    
  </s:if>

</ul>
</div>