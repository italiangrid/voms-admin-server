<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<h2>Manage Attribute classes</h2>

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
      <s:checkbox name="checkUniqueness" label="Check uniqueness" value="false"/>
      <s:submit value="%{'Create'}"/>
</s:form>
</div>

<div class="attributeListTab">
  <s:if test="#request.attributeClasses.size == 0">
    No attribute classes defined for this VO
  </s:if>
  <s:else>
    <table cellpadding="0" cellspacing="0">
      <tr>
        <th>Attribute name</th>
        <th>Uniqueness check</th>
        <th/>
        
      </tr>
      <s:iterator var="attributeClass" value="#request.attributeClasses">
        <tr>
          <td><s:property value="name"/></td>
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
              <s:submit src="%{deleteImg}" type="image"/>
            </s:form>
          </s:if>
          </td>
        </tr>
      </s:iterator>
  </table>
  </s:else>
</div>