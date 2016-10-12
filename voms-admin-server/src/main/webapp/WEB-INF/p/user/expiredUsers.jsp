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

<s:if test="! #attr.canRead">
  You do not have enough permissions to browse this VO users.
</s:if>
<s:else>
  <h1>Users whose membership has expired:</h1>

  <div class="searchResultsPane">
    <tiles2:insertTemplate template="../shared/errorsAndMessages.jsp" />
    <s:if
      test='(#session.searchResults.searchString eq null) and (#session.searchResults.results.size == 0)'>
    No users with expired membership found in this VO.
    </s:if>
    <s:elseif test="#session.searchResults != null">

      <s:form id="multiUserOpsForm">
        <table
          cellpadding="0"
          cellspacing="0"
          class="table">
          <s:token />
          <tr class="userBulkOperations">
            <td style="border: none"><s:checkbox
                name="notSet"
                id="userSelectorTrigger"
                disabled="%{(not #attr.canCreate and not #attr.CanSuspend)}"
                theme="simple" /></td>
            <td
              colspan="2"
              style="border: none;"><s:submit
                value="%{'Extend membership'}"
                align="right"
                action="bulk-extend-membership-expiration"
                theme="simple"
                cssClass="userActionButton"
                disabled="%{#attr.canSuspend == false or #attr.canCreate == false}" />


              <s:submit
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
              <td style="width: 90%"><tiles2:insertTemplate
                  template="userInfo.jsp" /></td>
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
</s:else>