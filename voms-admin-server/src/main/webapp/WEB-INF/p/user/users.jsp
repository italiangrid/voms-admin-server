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
    permission="r"/>
    
<voms:hasPermissions var="canSuspend"
    context="vo"
    permission="SUSPEND"/>

<s:if test="!(#attr.canRead)">
 You do not have enough permissions to browse this VO users.
</s:if>
<s:else>
<h2>Users</h2>

<s:url action="bulk-suspend" 
  namespace="/user" 
  var="bulkSuspendAction"/>

<s:url 
  action="bulk-restore" 
  namespace="/user" 
  var="bulkRestoreAction"/>
  
<s:url 
  action="bulk-delete" 
  namespace="/user" 
  var="bulkDeleteAction"/>

<s:url 
  action="bulk-extend-membership-expiration" 
  namespace="/user" 
  var="bulkExtendMembershipExpirationAction"/>
  
<s:url 
  action="bulk-create-acceptance-record" 
  namespace="/user" 
  var="bulkCreateAcceptanceRecordAction"/>

<div class="searchResultsPane">
  
  <s:if test='(#session.searchResults.searchString eq null) and (#session.searchResults.results.size == 0)'>
    No users found.
  </s:if>
  <s:elseif test="#session.searchResults.results.size == 0">
    No users found matching search string '<s:property value="#session.searchResults.searchString"/>'.
  </s:elseif>
  <s:elseif test="#session.searchResults != null">
  
  <s:url value="/user/search.action" var="searchURL"/>
  
  <s:form id="multiUserOpsForm" namespace="/user" cssClass="form-horizontal">
  
    <s:token/>
      
    
    
  <table class="table">
    
    <tr class="user-toolbar">
      <td>
        <input id="bulk-user-selector" 
          type="checkbox" 
          class="form-control" 
          name="notSet"/>
      </td>
      <td colspan="3">
      <div class="btn-group" role="group">
      
      <button type="submit" class="btn btn-default btn-bulk-user" 
        formaction="${bulkRestoreAction}">Restore</button>
        
      <s:if test="not #attr.disableMembershipEndTime">
        <button type="submit" 
          class="btn btn-default btn-bulk-user"
          name="bulkAction" 
          formaction="${bulkExtendMembershipExpirationAction}">Extend membership</button>
      </s:if>
      <button type="submit" 
        class="btn btn-default btn-bulk-user"
        formaction="${bulkCreateAcceptanceRecordAction}"
        >Sign AUP on behalf of user</button>
    </div>
    
    <div class="btn-group pull-right" role="group">
      <button type="submit" 
        class="btn btn-warning btn-bulk-user"
        formaction="${bulkSuspendAction}">Suspend</button>
        
      <button type="submit" 
        class="btn btn-danger btn-bulk-user" 
        formaction="${bulkDeleteAction}">Delete</button>  
    </div>    
  
          
      </td>
    </tr>
    <s:iterator
      value="#session.searchResults.results"
      var="user"
      status="rowStatus"
    >
      <tr class="tableRow">
        <td style="width: 1%; ">
          <s:checkbox 
            name="userIds" 
            fieldValue="%{id}" 
            cssClass="user-checkbox" 
            theme="simple" 
            value="%{'false'}"
            disabled="%{#attr.canCreate == false and #attr.canSuspend == false}"/>
        </td>
        <td style="width: 40%"> <!-- Personal info -->
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
              <div class="institution" style="padding-top: .5em">
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
              <div>
                <span class="blabel blabel-important">Suspended</span>  
                <span class="blabel blabel-invert-important"><s:property value="suspensionReason"/></span>
              </div>
            </s:if>
            <div class="cont aupInfo">
              <tiles2:insertTemplate template="aupStatusDetail.jsp"/>
            </div>
            <div class="expirationInfo cont">
               <tiles2:insertTemplate template="membershipExpirationDetail.jsp"/>
            </div>
            </div>
        </td>

        <td> <!-- Certificate information -->
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
          
          <td style="vertical-align: bottom;"> <!--  Actions -->
          <div class="actions" style="float: right;">
          
          	<s:url action="load" var="userLoadURL">
          		<s:param name="userId" value="id"/>	
          	</s:url> 
          	<s:a href="%{userLoadURL}" cssClass="actionLink">
              more info     
          	</s:a>	  	
          </div>
         </td>
      </tr>
    </s:iterator>
  </table>
  </s:form>
  
  <s:url value="/user/search.action" var="searchURL"/>
    <div class="resultsFooter">
      <tiles2:insertTemplate template="../shared/searchNavBar.jsp"/>
    </div>
  </s:elseif>
</div>
</s:else>