<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<h1>Create a new VO role</h1>

<div class="createTab">
  <voms:hasPermissions var="canCreate" 
            context="/${voName}" 
            permission="CONTAINER_READ|CONTAINER_WRITE"/>
  
  <s:if test="#attr.canCreate">
    <s:form 
      action="create"
      namespace="/role"
      validate="true"
    >
      <s:token/>
      <s:textfield name="roleName" size="20" value="" label="Role name"/>
      <s:submit value="%{'Create!'}"/>
    </s:form>
  </s:if>
</div>
