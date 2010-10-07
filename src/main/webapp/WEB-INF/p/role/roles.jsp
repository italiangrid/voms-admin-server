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
<voms:hasPermissions var="canRead"
    context="vo"
    permission="CONTAINER_READ"/>
    
<s:if test="#attr.canRead">
<h1>Roles:</h1>
<div id="searchPane">
<s:form validate="true" theme="simple">
  <s:hidden name="searchData.type" value="%{'role'}"/>
  <s:textfield name="searchData.text" size="20" value="%{#session.searchData.text}"/>
  <s:submit value="%{'Search roles'}" cssClass="submitButton"/>
  <s:fielderror name="searchData.text"/>
</s:form>
</div>

<div id="createPane">

<div class="createTab">
  <voms:hasPermissions var="canCreate" 
            context="/${voName}" 
            permission="CONTAINER_READ|CONTAINER_WRITE"/>
  
  <s:if test="#attr.canCreate">
    <s:url action="create" namespace="/role" var="createRoleURL" method="input"/>
    <s:a href="%{createRoleURL}">New role</s:a>
  </s:if>
</div>

</div>


<div class="searchResultsPane">
<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp"/>
<s:if test='(#session.searchResults.searchString eq null) and (#session.searchResults.results.size == 0)'>
No roles defined for this VO.
</s:if>
<s:elseif test="#session.searchResults.results.size == 0">
  No roles found matching search string '<s:property value="#session.searchResults.searchString"/>'.
</s:elseif> 
<s:else>
           
    <table
      class="table"
      cellpadding="0"
      cellspacing="0"
    >
      <s:iterator
        value="#session.searchResults.results"
        var="role"
        status="rowStatus"
      >
        <tr class="tableRow">
  
          
          <td width="95%">
            
            <div class="roleName">
              <s:url action="edit" namespace="/role" var="editURL">
                <s:param name="roleId" value="id"/>
              </s:url>
              <s:a href="%{editURL}">
                <s:property value="name" />
              </s:a>
            </div>
           </td>
           <td>
            
            
            <voms:hasPermissions var="canDelete" 
              context="/${voName}" 
              permission="CONTAINER_READ|CONTAINER_WRITE"/>
            
            <s:if test="#attr['canDelete']">
              <s:form action="delete" namespace="/role">
                <s:url value="/img/delete_16.png" var="deleteImg"/>
                <s:token/>
                <s:hidden name="roleId" value="%{id}"/>
                <s:submit value="%{'delete'}" onclick="openConfirmDialog(this, 'deleteRoleDialog','%{name}'); return false"/>
              </s:form>
            </s:if>
            
           </td>
        </tr>
      </s:iterator>
    </table>
  
  
  <s:url action="search" namespace="/role" var="searchURL"/>
  
  <div class="resultsFooter">
  
  <voms:searchNavBar context="vo" 
      permission="r" 
      disabledLinkStyleClass="disabledLink"
      id="searchResults"
      linkStyleClass="navBarLink"
      searchURL="${searchURL}"
      styleClass="resultsCount"
      />
   </div>
</s:else>
</div>
</s:if>
<s:else>
  You do not have enough permissions to browse this VO roles.
</s:else>