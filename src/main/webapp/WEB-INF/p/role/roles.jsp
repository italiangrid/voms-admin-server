<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div id="searchPane">
<s:form validate="true" theme="simple">
  <s:hidden name="searchData.type" value="%{'role'}"/>
  <s:textfield name="searchData.text" size="20"/>
  <s:submit value="%{'Search roles'}" cssClass="submitButton"/>
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
<s:if test='(searchResults.searchString eq null) and (searchResults.results.size == 0)'>
No roles defined for this VO.
</s:if>
<s:elseif test="searchResults.results.size == 0">
  No roles found matching search string '<s:property value="searchResults.searchString"/>'.
</s:elseif> 
<s:else>
  <table
    class="table"
    cellpadding="0"
    cellspacing="0"
  >
    <s:iterator
      value="searchResults.results"
      var="role"
      status="rowStatus"
    >
      <tr class="tableRow">

        <td width="95%">
          <div class="roleName">
            <s:url action="edit" namespace="/role" var="editURL" method="load">
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
              <s:submit src="%{deleteImg}" type="image"/>
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