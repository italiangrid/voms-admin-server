<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>


<h1>Users:</h1>

<div id="searchPane">
  <s:form validate="true" theme="simple">
    <s:hidden name="searchData.type" value="%{'user'}"/>
    <s:textfield name="searchData.text" size="20" value="%{#session.searchData.text}"/>
    <s:submit value="%{'Search users'}" cssClass="submitButton"/>
  </s:form>
  <s:fielderror fieldName="searchData.text"/>
</div>

<div id="createPane">
	<voms:hasPermissions var="canCreate"
		context="vo"
		permission="rw"/>
	<s:if test="#attr.canCreate">
		<a href="<s:url action="create" method="input"/>">Add a new user</a>
	</s:if>
</div>

<div class="searchResultsPane">
  
  <s:if test='(#session.searchResults.searchString eq null) and (#session.searchResults.results.size == 0)'>
    No users found in this VO.
  </s:if>
  <s:elseif test="#session.searchResults.results.size == 0">
    No users found matching search string '<s:property value="#session.searchResults.searchString"/>'.
  </s:elseif>
  <s:elseif test="#session.searchResults != null">
  
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
  
  <div style="clear: both"></div>
  
    
      
  <s:form>
  <table
    cellpadding="0"
    cellspacing="0"
  >
    <tr>
      <td colspan="2" style="text-align: left">
        <s:submit value="%{'Suspend'}" align="right" action="suspend" theme="simple"/>
        <s:submit value="%{'Delete'}" align="right" action="delete" theme="simple"/>
          
      </td>
      <td style="text-align: right">
        <s:submit value="%{'Add to group'}" align="right" action="add-to-group" theme="simple"/>
      </td>
    </tr>
    <tr>
      <th>User information</th>
      <th>Certificates<tiles2:insertTemplate template="../shared_20/formattedDNControls.jsp"/>
      </th>
      <th>
         
      </th>
    </tr>
    <s:iterator
      value="#session.searchResults.results"
      var="user"
      status="rowStatus"
    >
      <tr class="tableRow">
        
        <td style="width: 25%"> <!-- Personal info -->
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
                <s:set value="subjectString" var="thisCertDN"/>
                <voms:formatDN dn="${thisCertDN}" fields="CN"/>
              </div>
              <div class="certIssuer <s:if test="suspended">suspended-cert</s:if>">
                <s:set value="ca.subjectString" var="thisCertCA"/>
                <voms:formatDN dn="${thisCertCA}" fields="CN"/>
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
            <a href="${deleteURL}" class="actionLink" onclick="openConfirmDialog(this, 'deleteUserDialog','${shortName}'); return false;">
            	delete
            </a>
            
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
  </s:form>
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
  </s:elseif>
</div>