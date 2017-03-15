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

<s:url action="new-user" namespace="/user" var="newUserAction"/>
<s:url action="new-group" namespace="/group" var="newGroupAction"/>
<s:url action="new-role" namespace="/role" var="newRoleAction"/>

<div class="voms-header">
	<div class="container">
		<div class="col-md-2">
			<h3>${voName}</h3>
		</div>
		<div>
			<s:form action="search" theme="bootstrap"
				namespace="/user" cssClass="form-inline">
				
				<s:hidden name="searchData.type" value="%{'user'}" />

				<s:textfield name="searchData.text"
					value="%{#session.searchData.text}" 
					requiredLabel="false"
					placeholder="Search VO..." 
					/>
			  <s:submit cssClass="hidden" />
			  
        <div class="btn-group pull-right">
          <button type="button" class="btn btn-success dropdown-toggle"
            data-toggle="dropdown" aria-expanded="false">
            <i class="glyphicon glyphicon-plus"></i>
            <span class="caret"></span>
          </button>
          <ul class="dropdown-menu" role="menu">
            <li><a href="${newUserAction}" data-toggle="modal" data-target="#createUserDialog">New user</a></li>
            <li><a href="${newGroupAction}" data-toggle="modal" data-target="#createGroupDialog">New group</a></li>
            <li><a href="${newRoleAction}" data-toggle="modal" data-target="#createRoleDialog">New role</a></li>
            <li><a href="#">New generic attribute class</a></li>
          </ul>
        </div>
			</s:form>
		</div>
	</div>
</div>