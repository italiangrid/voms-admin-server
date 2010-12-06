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

<h1>Edit <s:if test="defaultACL">default</s:if> ACL entry:</h1>

<s:form>
  <s:token/>
  <s:hidden name="aclId" value="%{model.id}" />
  <s:hidden name="adminId" value="%{admin.id}" />

  <table class="form noborder aclManagement" width="100%">
    <tr>
      <td style="width: 10%;"><h1>Admin:</h1></td>
      <td class="admin">
      <div class="userDN"><voms:formatDN dn="${admin.dn}" fields="CN" />
      </div>
      <div class="userCA"><voms:formatDN dn="${admin.ca.subjectString}"
        fields="CN" /></div>
      </td>
    </tr>
 
    <tr>
      <td><h1>Context:</h1></td>
      
      <td class="context"><s:property value="context" /></td>
    </tr>
    <tr>
    </tr>
    </table>
    
    
    <table class="form noborder">
    
    <%@include file="/WEB-INF/p/acl/permissionFormFieldset.jsp"%>
    
    <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
    <s:if test="not model.defaultACL and model.context.groupContext">
    <tr>
      <td>
      <h1>Propagate to children contexts?</h1>
      </td>
      <td><s:checkbox name="propagate" theme="simple"/></td>
    </tr>
    </s:if>
    <s:else>
      <s:hidden name="propagate" value="false"/>
    </s:else>
    <tr>
      <td colspan="4"/>
    </tr>
    <tr>
      <td colspan="3"/>
      <td><s:submit value="%{'Save entry'}" /></td>
    </tr>
  </table>
</s:form>