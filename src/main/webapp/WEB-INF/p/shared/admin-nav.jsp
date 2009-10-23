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

<div id="nav">
  <ul class="sf-menu">
  
  <li><a href="<s:url action='/home/login.action'/>">Home</a></li>
  
  <li class="current">
    <a href="#a">Manage</a>
    <ul>
      <li><a href="#b">Users</a></li>
      <li><a href="#c">Groups</a></li>
      <li><a href="#d">Roles</a></li>
      <li><a href="#e">Attributes</a></li>
      <li><a href="#f">Requests</a></li>
   </ul>
  </li>
  
  <li><a href="#">Configuration</a></li>
  <li><a href="#">Other VOs</a></li>
</ul>
</div>