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

  <s:set value="model.name" var="role"/>
  
    <s:if test="not attributeClasses.empty">
      <div class="attributeCreationTab">
        <s:form action="set-attribute" namespace="/role" onsubmit="ajaxSubmit(this,'generic-attrs-content'); return false;" id="setRoleAttributeForm">
          <s:hidden name="roleId" value="%{model.id}" id="setRoleAttributeRoleId"/>
          <s:token/>
          <table cellpadding="" cellspacing="" class="noborder">
            <tr>
              <td>
                <s:select name="groupId" 
                  list="voGroups" 
                  listKey="id" 
                  listValue="name"  
                  label="Group" id="setRoleAttributeGroupId" onchange="enableSetRoleAttributeForm();"/>
              </td>
            </tr>
            <tr>
              <td>
                <s:select name="attributeName" 
                  list="attributeClasses" 
                  listKey="name" 
                  listValue="name" 
                  label="Attribute name"/>
              </td>
              </tr>
              
              <tr>
                <td>
                  <s:textarea label="Attribute value" name="attributeValue" rows="4" cols="30" value="" />
                </td>
              </tr>
              <tr>
                <td>
                  <s:submit value="%{'Set attribute'}" id="setRoleAttributeSubmit"/>
                </td>
              </tr>
          </table>       
        </s:form>
      </div>
    </s:if>
    <s:else>
      No attribute classes defined for this vo.
    </s:else>
    
  <div class="reloadable">
  
  
    <s:if test="attributes.isEmpty">
        <s:if test="not #request.attributeClasses.empty">
          No attributes defined for this role.
        </s:if>
    </s:if>
    <s:else>
      
      <table class="table" cellpadding="0" cellspacing="0">
            
            <tr>
              <th>Group</th>
              <th>Attribute name</th>
              <th>Attribute value</th>
              <th colspan="2"/>
            </tr>
            
            <s:iterator var="attribute" value="attributes">
              
              <s:set value="%{group+'/Role='+model.name}" var="attributeRowContext"/>
              <voms:authorized permission="ATTRIBUTES_READ" context="${attributeRowContext}">
                <tr class="tableRow"> 
                  <td><s:property value="group"/></td>
                  <td><s:property value="name"/></td>
                  <td><s:property value="value"/></td>
                  <td>
                      <s:form action="delete-attribute" 
                        namespace="/role" 
                        onsubmit="ajaxSubmit(this,'generic-attrs-content'); return false;" 
                        cssClass="deleteRoleAttributeForm">
                        <s:token/>
                        <s:hidden name="groupId" value="%{#attribute.group.id}"/>
                        <s:hidden name="roleId" value="%{#attribute.role.id}"/>
                        <s:hidden name="attributeName" value="%{#attribute.name}"/>
                        <s:submit value="%{'delete'}" id="delete_role_attribute_%{#attribute.name}"/>
                      </s:form>
                  </td>
                </tr>
              </voms:authorized>
            </s:iterator>
      </table>
    </s:else>
  </div>