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
<div id="membersSearchResults">
  <div class="reloadable">
    <s:if test='(searchResults.searchString eq null) and (searchResults.results.size == 0)'>
        No members found having role '<s:property value="model.name" /> in group '<s:property value="group.name" />'.
      </s:if>
    <s:elseif test="searchResults.results.size == 0">
        No matches found for '<s:property value="searchResults.searchString" />'.
    </s:elseif>
    <s:else>
      <s:if test="(searchResults.searchString != null) and (searchResults.searchString != '')">
        <div class="resultsCountHeader">
          <s:property value="searchResults.count" />
          match
          <s:if test="searchResults.count > 1">es</s:if>
          found for '
          <s:property value="searchResults.searchString" />
          ':
        </div>
      </s:if>
      <div class="resultsFooter">
        <s:url action="search-member" namespace="/role" var="jsSearchURL">
          <s:param name="searchData.type" value="searchData.type" />
          <s:param name="searchData.groupId" value="searchData.groupId" />
          <s:param name="searchData.roleId" value="searchData.roleId" />
          <s:param name="searchData.text" value="searchData.text" />
        </s:url>
        <voms:searchNavBarJS styleClass="resultsCount" searchURL="${jsSearchURL}" linkStyleClass="navBarLink"
          id="searchResults" searchPanelId="mmResults" />
      </div>
      <table cellpadding="0" cellspacing="0">
        <s:iterator value="searchResults.results" var="user">
          <tr class="tableRow">
            <td style="width: 90%">
              <tiles2:insertTemplate template="../user/userInfo.jsp" />
            </td>
            <td class="actions">
              <s:url action="load" namespace="/user" var="loadURL">
                <s:param name="userId" value="id" />
              </s:url>
              <s:a href="%{loadURL}" cssClass="actionLink">
                        more info
                    </s:a>
            </td>
          </tr>
        </s:iterator>
      </table>
      <div class="resultsFooter">
        <s:url action="search-member" namespace="/role" var="jsSearchURL">
          <s:param name="searchData.type" value="searchData.type" />
          <s:param name="searchData.groupId" value="searchData.groupId" />
          <s:param name="searchData.roleId" value="searchData.roleId" />
          <s:param name="searchData.text" value="searchData.text" />
        </s:url>
        <voms:searchNavBarJS styleClass="resultsCount" searchURL="${jsSearchURL}" linkStyleClass="navBarLink"
          id="searchResults" searchPanelId="mmResults" />
      </div>
    </s:else>
  </div>
</div>