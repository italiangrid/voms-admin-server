<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
  
  <voms:authorized permission="ATTRIBUTES_WRITE" context="vo">
  
    <s:if test="not #request.attributeClasses.empty">
      <div class="attributeCreationTab">
        <s:form action="set-attribute" namespace="/user" onsubmit="ajaxSubmit(this,'generic-attrs-content'); return false;">
          <s:token/>
          <s:hidden name="userId" value="%{model.id}"/>
          
          <table cellpadding="" cellspacing="" class="noborder">
            <tr>
              <td>
                <s:select name="attributeName" 
                  list="#request.attributeClasses" 
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
          No attributes defined for this user.
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
                    <s:form action="delete-attribute" namespace="/user" onsubmit="ajaxSubmit(this,'generic-attrs-content'); return false;">
                      <s:token/>
                      <s:hidden name="userId" value="%{model.id}"/>
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