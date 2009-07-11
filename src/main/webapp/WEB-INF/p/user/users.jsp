<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div id="searchPane">
  <s:form validate="true" theme="simple">
    <s:hidden name="searchData.type" value="%{'user'}"/>
    <s:textfield name="searchData.text" size="20"/>
    <s:submit value="%{'Search users'}" cssClass="submitButton"/>
  </s:form>
</div>

<div id="createPane">
  <a href="#">New user</a>
</div>

<div class="searchResultsPane">
  
  <s:if test='(searchResults.searchString eq null) and (searchResults.results.size == 0)'>
    No users found in this VO.
  </s:if>
  <s:elseif test="searchResults.results.size == 0">
    No users found matching search string '<s:property value="searchResults.searchString"/>'.
  </s:elseif>
  <s:else>
  <h2>Users:</h2>
  
  <table
    class="table"
    cellpadding="0"
    cellspacing="0"
  >
    <s:iterator
      value="searchResults.results"
      var="user"
      status="rowStatus"
    >
      <tr class="tableRow">

        <td width="95%">
          <div class="userDN">
            <s:url action="edit" namespace="/user" var="editURL">
              <s:param name="userId" value="id"/>
            </s:url>
            <s:a href="%{editURL}">
              <s:property value="dn" />
            </s:a>
          </div>
         </td>
         
         
          <voms:hasPermissions var="canSuspend" 
            context="/${voName}" 
            permission="CONTAINER_READ|MEMBERSHIP_READ|SUSPEND"/>
            
            <s:if test="#attr.canSuspend and not suspended">
              <td>
              <s:url action="suspend" namespace="/user" method="input" var="suspendURL">
                <s:param name="userId" value="%{#user.id}"/>
              </s:url>
              <s:a href="%{suspendURL}" cssClass="actionLink">
                suspend
              </s:a>
              </td>
            </s:if>
            <s:if test="#attr.canSuspend and suspended">
              <td>
              <s:url action="restore" namespace="/user" var="restoreURL">
                <s:param name="userId" value="%{#user.id}"/>
              </s:url>
              
              <s:a href="%{restoreURL}" cssClass="actionLink">
                restore
              </s:a>
              </td>
            </s:if>
         
         <td>
         <voms:hasPermissions var="canDelete" 
            context="/${voName}" 
            permission="CONTAINER_READ|CONTAINER_WRITE|MEMBERSHIP_READ|MEMBERSHIP_WRITE"/>
            
          <s:if test="#attr.canDelete">
            <s:form action="delete" namespace="/user">
              <s:url value="/img/delete_16.png" var="deleteImg" />
              <s:token/>
              <s:hidden name="userId" value="%{#user.id}"/>
              <s:submit src="%{deleteImg}" type="image"/>
            </s:form>
          </s:if>
         </td>
      </tr>
    </s:iterator>
  </table>
  
  <s:url action="search" namespace="/user" var="searchURL"/>
  
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