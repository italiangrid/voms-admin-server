<%--

    Copyright (c) Members of the EGEE Collaboration. 2006-2009.
    See http://www.eu-egee.org/partners/ for details on the copyright holders.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    Authors:
    	Andrea Ceccanti (INFN)

--%>
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
    <a href="<s:url action="load" namespace="/aup"/>">AUPs</a>
  </li>
  
  <li class="bottom-menu-item">
    <a href="#subscriptions">Subscriptions</a>
  </li>
</ul>