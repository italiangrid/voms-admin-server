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
    permission="ATTRIBUTES_READ"/>
    

<s:if test="#attr.canRead">

<h1>Generic attributes:</h1>

<div id="searchPane">
<s:form validate="true" theme="simple">
  <s:hidden name="searchData.type" value="%{'attribute'}"/>
  <s:textfield name="searchData.text" size="20"/>
  <s:submit value="%{'Search attributes'}" cssClass="submitButton"/>
</s:form>
</div>

<div id="createPane">
<voms:hasPermissions var="canCreate" 
            context="/${voName}" 
            permission="ATTRIBUTES_READ|ATTRIBUTES_WRITE"/>
  
  <s:if test="#attr.canCreate">
    <s:url action="create" namespace="/attribute" var="manageAttributeURL"  method="input"/>
    <s:a href="%{manageAttributeURL}">Manage attribute classes</s:a>
  </s:if>
</div>

<div class="searchResultsPane">
<tiles2:insertTemplate template="../shared/errorsAndMessages.jsp"/>

<s:if test='(searchResults.searchString eq null) and (searchResults.results.size == 0)'>
No attribute mappings defined in this VO.
</s:if>
<s:elseif test="searchResults.results.size == 0">
  No attribute mappings found matching search string '<s:property value="searchResults.searchString"/>'.
</s:elseif> 
<s:else>
  <table
    class="table"
    cellpadding="0"
    cellspacing="0"
  >
    <tr>
      <th>Attribute name</th>
      <th>Attribute value</th>
      <th>User</th>
    </tr>
    <s:iterator
      value="searchResults.results"
      var="userAttribute"
      status="rowStatus"
    >
      <tr class="tableRow">
        <td style="width: 20%">
          <s:property value="%{#userAttribute[0]}"/>
         </td>
         <td style="width: 20%">
          <span class="highlight">
            <s:property value="%{#userAttribute[2]}"/>
          </span>
         </td>
         <td>
            <s:if test="%{#userAttribute[1].name != null}">
              <div class="username">
                <s:property value="%{#userAttribute[1].name + ' ' + #userAttribute[1].surname}"/>
              </div>
            </s:if>
            <s:iterator value="%{#userAttribute[1].certificates}" var="cert">
            <div style="margin: 1em 0;">
              <div class="userDN"><s:set value="subjectString"
                var="thisCertDN" /> <voms:formatDN dn="${thisCertDN}" fields="CN" />
              </div>

              <div class="userCA"><s:set value="ca.subjectString"
                var="thisCertCA" /> <voms:formatDN dn="${thisCertCA}" fields="CN" />
              </div>
            </div>
          </s:iterator>
         </td>
        </tr>
    </s:iterator>
  </table>
  
  <s:url action="search" namespace="/attribute" var="searchURL"/>
  
  <div id="resultsFooter">
  <voms:searchNavBar context="vo" 
      permission="ATTRIBUTES_READ" 
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
  You do not have enough permissions to browse this VO generic attributes.
</s:else>