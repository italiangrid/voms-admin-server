<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015

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
<h1>Generic Attribute classes:</h1>

<div class="createTab">
<s:actionerror/>
<s:form
      action="create"
      namespace="/attribute"
      validate="true"
    >
    
      <s:token/>
      <s:textfield name="attributeName" size="20" value="" label="Name"/>
      <s:textarea name="attributeDescription" cols="20" rows="3" value="" label="Description"/>
      <s:checkbox name="checkUniqueness" label="Check uniqueness" value="false" labelposition="right" />
      <s:submit value="%{'Create'}" align="left" cssStyle="margin-top: 1em"/>
</s:form>
</div>

<div class="attributeListTab" style="margin-top: 2em">
  <s:if test="#request.attributeClasses.size == 0">
    No attribute classes defined for this VO
  </s:if>
  <s:else>
    <table cellpadding="0" cellspacing="0">
      <tr>
        <th>Attribute name</th>
        <th>Description</th>
        <th>Uniqueness check</th>
        <th/>
        
      </tr>
      <s:iterator var="attributeClass" value="#request.attributeClasses">
        <tr class="tableRow">
          <td style="width: 20%"><s:property value="name"/></td>
          <td style="width: 40%"><s:property value="description"/></td>
          <td><s:property value="unique"/></td>
          <td>
            <voms:hasPermissions var="canDelete" 
              context="/${voName}" 
              permission="ATTRIBUTES_READ|ATTRIBUTES_WRITE"/>
            
          <s:if test="#attr.canDelete">
            <s:form action="delete" namespace="/attribute">
              <s:url value="/img/delete_16.png" var="deleteImg"/>
              <s:token/>
              <s:hidden name="attributeName" value="%{name}"/>
              <s:submit value="%{'delete'}" onclick="openConfirmDialog(this, 'deleteAttributeClassDialog','%{name}'); return false"/>
            </s:form>
          </s:if>
          </td>
        </tr>
      </s:iterator>
  </table>
  </s:else>
</div>
