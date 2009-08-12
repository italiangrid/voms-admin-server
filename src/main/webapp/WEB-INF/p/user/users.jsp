<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<h1>Users:</h1>

<div id="searchPane">
  <s:form validate="true" theme="simple">
    <s:hidden name="searchData.type" value="%{'user'}"/>
    <s:textfield name="searchData.text" size="20"/>
    <s:submit value="%{'Search users'}" cssClass="submitButton"/>
  </s:form>
</div>

<div id="createPane">
  <a href="#">Add a new user</a>
</div>

<div class="searchResultsPane">
  
  <s:if test='(searchResults.searchString eq null) and (searchResults.results.size == 0)'>
    No users found in this VO.
  </s:if>
  <s:elseif test="searchResults.results.size == 0">
    No users found matching search string '<s:property value="searchResults.searchString"/>'.
  </s:elseif>
  <s:else>
  
  <table
    cellpadding="0"
    cellspacing="0"
  >
  
    <tr>
      <th>User information</th>
      <th>Certificates</th>
      <th></th>
    </tr>
    <s:iterator
      value="searchResults.results"
      var="user"
      status="rowStatus"
    >
      <tr class="tableRow">
        
        <td> <!-- Personal info -->
          <div class="personal-info">
            
            <s:if test="name != null and name != ''">
              <div class="username">
                <s:property value="%{#user.name + ' ' + #user.surname}"/>
              </div>
            </s:if>
            <s:else>
              <div class="unspecified">
                No name specified for this user.
              </div>
            </s:else>
            <s:if test="institution != null and institution != ''">
              <div class="institution">
                <s:property value="institution"/>
              </div>
            </s:if>
            <s:else>
              <div class="unspecified">
                No institution specified for this user.
              </div>
            </s:else>
            
            <div class="email">
              <a href="mailto:<s:property value="emailAddress"/>">
                <s:property value="emailAddress"/>
              </a>
            </div>
            
            <s:if test="suspended">
              <div class="warning">
                This user is suspended.
                <br />Reason: 
                <span class="suspensionReason"><s:property value="suspensionReason"/></span>
              </div>
              
            </s:if>
          </div>
        </td>

        <td> <!-- Certificate information -->
          <s:if test="#user.suspended">
                <div class="warning">
                  Certificates listed below are suspended due to membership suspension:
                </div>
          </s:if>
          <ol class="certificate-info">
          
          <s:iterator value="certificates" var="cert">
            <li>
            
              
              <div class="certSubject <s:if test="suspended">suspended-cert</s:if>">
                <s:property value="subjectString"/>
              </div>
              <div class="certIssuer <s:if test="suspended">suspended-cert</s:if>">
                <s:property value="ca.subjectString"/>
              </div>
            </li>
          </s:iterator>
          </ol>
         </td>
         
         <td class="actions">
         
            <s:url action="edit" namespace="/user" var="editURL">
              <s:param name="userId" value="id"/>
            </s:url>
            <s:a href="%{editURL}" cssClass="actionLink">
              more info
              
            </s:a>
         
            <voms:hasPermissions var="canSuspend" 
            context="/${voName}" 
            permission="CONTAINER_READ|MEMBERSHIP_READ|SUSPEND"/>
            
            <s:if test="#attr.canSuspend and not suspended">
         
              <s:url action="suspend" namespace="/user" method="input" var="suspendURL">
                <s:param name="userId" value="%{#user.id}"/>
              </s:url>
              <s:a href="%{suspendURL}" cssClass="actionLink">
                suspend
              </s:a>
         
            </s:if>
            <s:if test="#attr.canSuspend and suspended">
         
              <s:url action="restore" namespace="/user" var="restoreURL">
                <s:param name="userId" value="%{#user.id}"/>
              </s:url>
              
              <s:a href="%{restoreURL}" cssClass="actionLink">
                restore
              </s:a>
         
            </s:if>
         
         <voms:hasPermissions var="canDelete" 
            context="/${voName}" 
            permission="CONTAINER_READ|CONTAINER_WRITE|MEMBERSHIP_READ|MEMBERSHIP_WRITE"/>
            
          <s:if test="#attr.canDelete">
            <s:url action="delete" namespace="/user" var="deleteURL">
              <s:param name="userId" value="id"/>
            </s:url>
            <s:a href="%{deleteURL}" cssClass="actionLink">
                delete
            </s:a>
            
            
            <%--
            <s:form action="delete" namespace="/user">
              <s:url value="/img/delete_16.png" var="deleteImg" />
              <s:token/>
              <s:hidden name="userId" value="%{#user.id}"/>
              <s:submit src="%{deleteImg}" type="image" cssClass="actionLink"/>
            </s:form>
            --%>
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