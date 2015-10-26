<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015

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
<s:url value="https://phonebook.cern.ch/phonebook/#personDetails/" 
  var="phonebookURL">
  <s:param name="id" value="%{orgDbId}"/>
</s:url>

<voms:hasPermissions var="canDelete" context="vo" permission="rw"/>

<div>
  CERN HR database id: <a href="${phonebookURL}">${orgDbId}</a>
  
  <s:if test="#attr.canDelete">
	  <s:form 
	    action="change-orgdb-id" 
	    namespace="/user" 
	    theme="simple" 
	    cssStyle="display: inline">
	    <s:hidden name="userId" value="%{id}"/>
	    <s:submit value="%{'Change HR id'}"/>
	  </s:form>  
  </s:if>
</div>

