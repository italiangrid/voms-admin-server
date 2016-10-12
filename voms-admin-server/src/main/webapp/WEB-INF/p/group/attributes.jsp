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

  <s:set value="model.name" var="groupName"/>
  <voms:authorized permission="ATTRIBUTES_WRITE" context="${groupName}">
    <s:if test="not attributeClasses.empty">
      <div class="attributeCreationTab">
        <s:form action="set-attribute" namespace="/group" onsubmit="ajaxSubmit(this,'generic-attrs-content'); return false;">
          <s:token/>
          <s:hidden name="groupId" value="%{model.id}"/>
          
          <table cellpadding="" cellspacing="" class="noborder">
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
                  <s:submit value="%{'Set attribute'}"/>
                </td>
              </tr>
          </table>       
        </s:form>
      </div>
    </s:if>
    <s:else>
      No attribute classes defined for this vo.
    </s:else>
  </voms:authorized>
    
  <div class="reloadable">
  
  <voms:authorized permission="ATTRIBUTES_READ" context="vo">
    <s:if test="attributes.isEmpty">
        <s:if test="not #request.attributeClasses.empty">
          No attributes defined for this group.
        </s:if>
    </s:if>
    <s:else>
      
      <table class="table" cellpadding="0" cellspacing="0">
            
            <tr>
              <th>Attribute name</th>
              <th>Attribute value</th>
              <th colspan="2"/>
            </tr>
            
            <s:iterator var="attribute" value="attributes">
              
              <tr class="tableRow">  
                <td><s:property value="name"/></td>
                <td><s:property value="value"/></td>
                <td>
                  <voms:authorized permission="ATTRIBUTES_WRITE" context="vo">
                    <s:form action="delete-attribute" namespace="/group" onsubmit="ajaxSubmit(this,'generic-attrs-content'); return false;">
                      <s:token/>
                      <s:hidden name="groupId" value="%{model.id}"/>
                      <s:hidden name="attributeName" value="%{#attribute.name}"/>
                      <s:submit value="%{'delete'}"/>
                    </s:form>
                  </voms:authorized>
                </td>
              </tr>
            </s:iterator>
      </table>
    </s:else>
  </voms:authorized>
  </div>
  
  <voms:hasPermissions context="${groupName}" var="canReadAttributes" permission="ATTRIBUTES_READ"/>
  <s:if test="not #attr.canReadAttributes">
  	You do not have enough permissions to browse this VO generic attributes. 
  </s:if>
  