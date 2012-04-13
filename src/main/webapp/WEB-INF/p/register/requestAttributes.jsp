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

<h1>
VO membership request confirmed.
</h1>

<p>
You have succesfully confirmed your VO membership request.

The VO administrator has been informed of your request and will
handle it as soon as possible.
</p>

<h2>Group selection</h2>
<p>
Please select the VO groups you would like to be member of:
</p>
<s:form action="request-attributes" namespace="/register">
	<s:token/>
	
	<s:hidden name="requestId" value="%{model.id}" />
	<s:hidden name="confirmationId" value="%{confirmationId}" />
	
	<s:select list="#request.voGroups" listKey="id" listValue="name" name="groupId" />
	<s:submit value="%{'Request group membership'}" />
</s:form>