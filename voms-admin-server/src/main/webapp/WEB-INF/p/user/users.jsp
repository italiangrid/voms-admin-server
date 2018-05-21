<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016

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

<voms:hasPermissions
  var="canRead"
  context="vo"
  permission="r" />

<voms:hasPermissions
  var="canSuspend"
  context="vo"
  permission="SUSPEND" />
  
<voms:hasPermissions
  var="canReadPI"
  context="vo"
  permission="PERSONAL_INFO_READ" />

<s:if test="#attr.canRead">
  <h1>Users:</h1>

  <div id="searchPane">
    <s:form
      validate="true"
      theme="simple"
      action="search">
      <s:hidden
        name="searchData.type"
        value="%{'user'}" />
      <s:textfield
        name="searchData.text"
        size="20"
        value="%{#session.searchData.text}" />
      <s:submit
        value="%{'Search users'}"
        cssClass="submitButton" />
      <span>Limit to:</span>
      <s:label for="limitToSuspendedUsers">
        <span class="blabel blabel-important baseline">Suspended</span>
      </s:label>
      <s:checkbox
        id="limitToSuspendedCheck"
        name="limitToSuspendedUsers"
        onclick="$('#limitToAupCheck').attr('checked', false); this.form.submit()" />

      <s:if test="#attr.canReadPI">
        <s:label for="limitToSuspendedUsers">
          <span class="blabel blabel-warning baseline">Pending sign AUP
            request</span>
        </s:label>
        <s:checkbox
          id="limitToAupCheck"
          name="limitToUsersWithPendingSignAUPRequest"
          onclick="$('#limitToSuspendedCheck').attr('checked', false); this.form.submit()" />
      </s:if>
      <s:else>
        <s:hidden name="limitToUsersWithPendingSignAUPRequest" value="%{'false'}"/>
      </s:else>
      
      <s:label for="searchData.maxResults">Show:</s:label>
      <s:select
        name="searchData.maxResults"
        list="{'10','50','100'}"
        value="%{#session.searchData.maxResults}"
        onchange="this.form.submit()" />
    </s:form>
    <s:fielderror fieldName="searchData.text" />
  </div>
  
  <div id="createPane">
    <voms:hasPermissions
      var="canCreate"
      context="vo"
      permission="rw" />
    <s:if test="#attr.canCreate">
      <a href="<s:url action="create-user-input" namespace="/user" />">Add a new user</a>
    </s:if>
  </div>

  <div class="searchResultsPane">
    <tiles2:insertTemplate template="../shared/errorsAndMessages.jsp" />
    <s:if
      test='(#session.searchResults.searchString eq null) and (#session.searchResults.results.size == 0)'>
    No users found.
  </s:if>
    <s:elseif test="#session.searchResults.results.size == 0">
    No users found matching search string '<s:property
        value="#session.searchResults.searchString" />'.
  </s:elseif>
    <s:elseif test="#session.searchResults != null">

      <s:form id="multiUserOpsForm" action="bulk-restore">
        <table class="table">
          <s:token />
          <tr class="userBulkOperations">
            <td style="border: none"><s:checkbox
                name="notSet"
                id="userSelectorTrigger"
                disabled="%{(not #attr.canCreate and not #attr.CanSuspend)}"
                theme="simple" /></td>
            <td
              colspan="3"
              style="border: none;"><s:submit
                value="%{'Suspend'}"
                align="right"
                action="bulk-suspend"
                theme="simple"
                cssClass="userActionButton"
                onclick=" return openSuspendDialog(this, 'suspendMultiUserDialog','');"
                disabled="%{#attr.canSuspend == false}" /> 
                <s:submit
                value="%{'Restore'}"
                align="right"
                action="bulk-restore"
                theme="simple"
                cssClass="userActionButton"
                disabled="%{#attr.canSuspend == false}" /> 
                <s:if
                test="not #attr.disableMembershipEndTime and #attr.canSuspend == true and #attr.readOnlyMembershipExpiration == false">
                <s:submit
                  value="%{'Extend membership'}"
                  align="right"
                  action="bulk-extend-membership-expiration"
                  theme="simple"
                  cssClass="userActionButton"
                  disabled="%{#attr.canSuspend == false}" 
                  onclick="openConfirmDialog(this, 'extendMembershipMultiDialog', ''); return false"
                  
                  />
              </s:if> <s:submit
                value="%{'Delete'}"
                align="right"
                action="bulk-delete"
                theme="simple"
                cssClass="userActionButton"
                disabled="%{#attr.canCreate == false}"
                onclick="openConfirmDialog(this, 'deleteMultiUserDialog', ''); return false" />

              <s:url
                value="/user/search.action"
                var="searchURL" />
              <div class="resultsNav">
                <tiles2:insertTemplate template="../shared/searchNavBar.jsp" />
              </div></td>
          </tr>
          <tr style="border-bottom: 1px solid #c8c8c8;">
            <th />

            <th>User information</th>
            <th />
          </tr>
          <s:iterator
            value="#session.searchResults.results"
            var="user"
            status="rowStatus">
            <tr class="tableRow">
              <td style="width: 1%; vertical-align: top; padding-top: .8em;">
                <s:checkbox
                  name="userIds"
                  fieldValue="%{id}"
                  cssClass="userCheckbox"
                  theme="simple"
                  value="%{'false'}"
                  disabled="%{#attr.canCreate == false and #attr.canSuspend == false}" />
              </td>
              <td style="width: 90%">
                <tiles2:insertTemplate template="userInfo.jsp"/>
              </td>
              <td style="vertical-align: bottom;">
                <!--  Actions -->
                <div
                  class="actions"
                  style="float: right;">

                  <s:url
                    action="load"
                    var="userLoadURL">
                    <s:param
                      name="userId"
                      value="id" />
                  </s:url>
                  <s:a
                    href="%{userLoadURL}"
                    cssClass="actionLink">
              more info     
          	</s:a>
                </div>
              </td>
            </tr>
          </s:iterator>
        </table>
      </s:form>

      <s:url
        value="/user/search.action"
        var="searchURL" />

      <div class="resultsFooter">
        <tiles2:insertTemplate template="../shared/searchNavBar.jsp" />
      </div>
    </s:elseif>
  </div>
</s:if>
<s:else>
  You do not have enough permissions to browse this VO users.
</s:else>
