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
<%@include
  file="/WEB-INF/p/shared/taglibs.jsp"%>

<div
  id="membersSearchResults">
  
  <div class="reloadable">
    <s:if
      test='(searchResults.searchString eq null) and (searchResults.results.size == 0)'>
        No members found having role '<s:property value="model.name"/> in group '<s:property value="group.name"/>'.
      </s:if>
    <s:elseif
      test="searchResults.results.size == 0">
        No matches found for '<s:property
        value="searchResults.searchString" />'.
    </s:elseif>
    <s:else>
      <s:if test="(searchResults.searchString != null) and (searchResults.searchString != '')">
          <div class="resultsCountHeader">
            <s:property value="searchResults.count"/> match<s:if test="searchResults.count > 1">es</s:if> found for '<s:property value="searchResults.searchString"/>':
          </div>
      </s:if>
      <div class="resultsFooter">
          
          <s:url action="search-member" namespace="/role" var="jsSearchURL">
            <s:param name="searchData.type" value="searchData.type"/>
            <s:param name="searchData.groupId" value="searchData.groupId"/>
            <s:param name="searchData.roleId" value="searchData.roleId"/>
            <s:param name="searchData.text" value="searchData.text"/>
         </s:url>
         
          
          <voms:searchNavBarJS styleClass="resultsCount" searchURL="${jsSearchURL}" 
            linkStyleClass="navBarLink" id="searchResults" searchPanelId="mmResults"/>
               
        </div>
        <table
          cellpadding="0"
          cellspacing="0">
      
          <tr>
            <th style="width: 25%">User information</th>
            <th>Certificates<tiles2:insertTemplate template="../shared/formattedDNControls.jsp"/></th>
            <th/>
          </tr>
          
          <s:iterator value="searchResults.results"
            var="user">
            
            <tr class="tableRow">
            
            <td class="personal-info">
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
               
                <s:url action="load" namespace="/user" var="loadURL">
                    <s:param name="userId" value="id"/>
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
            <s:param name="searchData.type" value="searchData.type"/>
            <s:param name="searchData.groupId" value="searchData.groupId"/>
            <s:param name="searchData.roleId" value="searchData.roleId"/>
            <s:param name="searchData.text" value="searchData.text"/>
         </s:url>
          
          <voms:searchNavBarJS styleClass="resultsCount" searchURL="${jsSearchURL}" 
            linkStyleClass="navBarLink" id="searchResults" searchPanelId="mmResults"/>
               
        </div>
    </s:else>
  </div>  
</div>