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

<h1>Group managers</h1>

<voms:hasPermissions 
    var="isVOAdmin"
    context="vo"
    permission="ALL"/>

<s:if test="not #attr.isVOAdmin">
  You don't have sufficient privileges to access this information.  
</s:if>
<s:else>
  <tiles2:insertTemplate template="../shared/errorsAndMessages.jsp"/>  
  
  <s:if test="groupManagers.size  == 0">
    No group managers defined for this VO.
    <s:a action="create!input"> Create one</s:a>
  </s:if>
  <s:else>
    
    <s:form>
    <table class="table">
      <tr>
        <td colspan="5" style="text-align: right;">
          <s:form>
            <s:submit 
              action="create!input" 
              theme="simple" value="Add group manager"/>            
          </s:form>
        </td>
      </tr>
      <tr>
        <th>Name</th>
        <th>Description</th>
        <th>Email</th>
        <th>Managed Groups</th>
        <th/>
      </tr>
      
      <s:iterator value="groupManagers">
      <tr class="tableRow">
        <td width="20%"> <!-- Name field -->
          <s:property value="name"/>
        </td>
        
        <td width="20%"> <!-- Description field -->
          <s:property value="description"/>
        </td>
        
        <td>
          <s:property value="emailAddress"/>
        </td>
        
        <td><!-- Managed groups -->
          
          <div>
          <s:iterator value="managedGroups">
            <s:property value="name"/>
          </s:iterator>
          </div>
          
        </td>
        <td>
          <s:form>
            <s:submit 
              action="delete" 
              theme="simple"
              value="Delete">
              
              <s:hidden name="id" value="%{id}"/>
              
            </s:submit>
          </s:form>
        </td>
      </tr>
      </s:iterator>
    </table>
    </s:form>
  </s:else>
</s:else>
