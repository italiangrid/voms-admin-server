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

<voms:hasPermissions var="isVOAdmin" context="vo" permission="ALL" />


<ul id="voms-menu" class="nav nav-pills nav-stacked">
	<li id="voms-menu-admin-home"><a
		href="<s:url action="login" namespace="/home"/>"><i
			class="glyphicon glyphicon-home"></i> Home</a>
	</li>

	<li id="voms-menu-admin-requests"> <a
		href="<s:url action="index" namespace="/admin"/>"><i
			class="glyphicon glyphicon-inbox"></i> Requests</a>
	</li>

	<li id="voms-menu-user-search"><a
		href="<s:url action="search" namespace="/user"/>"><i
			class="fa fa-user"></i> Users</a></li>
			
	<li id="voms-menu-group-search"><a
		href="<s:url action="search" namespace="/group"/>"><i
			class="fa fa-users"></i> Groups</a></li>
			
	<li id="voms-menu-role-search"> <a
		href="<s:url action="search" namespace="/role"/>"><i
			class="fa fa-square"></i> Roles</a>
	</li>
	
	<li id="voms-menu-attribute-search"><a
		href="<s:url action="search" namespace="/attribute"/>"><i
			class="fa fa-flag"></i> Attributes</a></li>

	<li id="voms-menu-settings"> <a
		href="<s:url action="manage" namespace="/acl"/>"><i
			class="fa fa-cog"></i> Settings</a>
	</li>

</ul>