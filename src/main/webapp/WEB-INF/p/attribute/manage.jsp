<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<h1>Generic Attribute classes:</h1>

<div class="createTab">

<s:form
      action="create"
      namespace="/attribute"
      validate="true"
    >
    
      <s:fielderror/>
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