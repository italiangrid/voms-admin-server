<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<h1>Create a new group:</h1>

<div class="createTab">
  <voms:hasPermissions var="canCreate" 
            context="/${voName}" 
            permission="CONTAINER_READ|CONTAINER_WRITE"
            />
  
  <s:if test="#attr.canCreate">
    
    <s:form 
      action="create"
      namespace="/group"
      validate="true"
    >
      
      <s:select list="#request['voGroups']" name="parentGroupName" label="Parent group"/>
      <s:textfield name="groupName" size="20" value="" label="Group name"/>
      <s:token/>
      <s:submit value="%{'Create!'}"/>
    </s:form>
  </s:if>
</div>