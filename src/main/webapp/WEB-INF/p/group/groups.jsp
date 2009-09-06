<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>Groups:</h1>
<div id="searchPane">
  <s:form validate="true" theme="simple">
    <s:hidden name="searchData.type" value="%{'group'}"/>
    <s:textfield name="searchData.text" size="20" cssClass="text"/>
    <s:submit value="%{'Search groups'}" cssClass="submitButton"/>
  </s:form>
  <s:fielderror fieldName="searchData.text"/>
</div>


<div id="createPane">
  <voms:hasPermissions var="canCreate" 
            context="/${voName}" 
            permission="CONTAINER_READ|CONTAINER_WRITE"/>
  
  <s:if test="#attr.canCreate">
    <s:url action="create" namespace="/group" method="input" var="createGroupURL"/>
    <s:a href="%{createGroupURL}">New group</s:a>
  </s:if>
</div>

<div class="searchResultsPane">

<s:if test='(searchResults.searchString eq null) and (searchResults.results.size == 0)'>
No groups found in this VO.
</s:if>
<s:elseif test="searchResults.results.size == 0">
  No groups found matching search string '<s:property value="searchResults.searchString"/>'.
</s:elseif> 
<s:else>
  <table
    class="table"
    cellpadding="0"
    cellspacing="0"
  >
    <s:iterator
      value="searchResults.results"
      var="group"
      status="rowStatus"
    >
      <tr class="tableRow">

        <td>
          <div class="groupName">
            <s:url action="edit" namespace="/group" var="editURL">
              <s:param name="groupId" value="id"/>
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
            
          <s:if test="(not rootGroup) and #attr['canDelete']">
            <s:form action="delete" namespace="/group">
              <s:url value="/img/delete_16.png" var="deleteImg"/>
              <s:token/>
              <s:hidden name="groupId" value="%{id}"/>
              
              <s:submit value="%{'delete'}" onclick="openConfirmDialog(this, 'deleteGroupDialog','%{name}'); return false"/>
            </s:form>
          </s:if>
          
         </td>
      </tr>
    </s:iterator>
  </table>
  <s:url action="search" namespace="/group" var="searchURL"/>
  
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
 

