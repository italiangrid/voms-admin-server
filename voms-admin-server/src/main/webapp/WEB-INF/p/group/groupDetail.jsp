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

<s:url action="manage" namespace="/acl" var="editACLURL">
  <s:param name="aclGroupId" value="id"/>
</s:url>

<s:set value="name" var="thisGroup"/>

<h1>
  Group <span class="groupname"><s:property value="name"/></span>

  <voms:authorized permission="ACL_READ" context="${thisGroup}">
  <s:a href="%{editACLURL}" cssClass="actionLink" cssStyle="margin-left:1em">View ACL</s:a>
  </voms:authorized>
</h1>

<s:if test="description != null">
  <h2>Description:</h2>
	<div class="alert alert-block">
		<s:property value="description"/>
	</div>
</s:if>

<s:if test="managers.size() != 0">
  <h2>Group managers:</h2>
  <div class="alert alert-block">
    <ul>
    <s:iterator value="managers">
      <li><s:property value="name"/> - <s:property value="emailAddress"/></li>
    </s:iterator>
    </ul>
  </div>
</s:if>
<div class="info-tab">
  <h2><span>Membership information</span></h2>
  <voms:div id="group-membership-content" cssClass="content">
    <div>
      <s:form theme="simple" action="search-member" namespace="/group" onsubmit="ajaxSubmit(this,'mmResults'); return false;">
        <s:hidden name="searchData.groupId" value="%{id}" id="groupSearchGroupId"/>
        <s:textfield name="searchData.text" size="20" id="groupSearchText"/>
        <s:submit value="%{'Search members'}" cssClass="submitButton"/>
      </s:form>
      
      
    </div>
    
    <div id="mmResults">
      <s:action name="search-member" namespace="/group" executeResult="true">
        <s:param name="searchData.groupId" value="id"/>
      </s:action>
    </div>
  </voms:div>
</div>

<div class="info-tab">
  <h2><span>Attributes information</span></h2>
  <voms:div id="generic-attrs-content" cssClass="content">
        
        <tiles2:insertTemplate template="attributes.jsp"/>
        
  </voms:div>
</div>

 


<s:url action="load-default" namespace="/acl" var="editDefaultACLURL">
  <s:param name="aclGroupId" value="id"/>
</s:url>






