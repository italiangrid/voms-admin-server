<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<h2>Create a new VO role</h2>

<div class="createTab">
  <voms:hasPermissions var="canCreate" 
            context="/${voName}" 
            permission="CONTAINER_READ|CONTAINER_WRITE"/>
  
  <s:if test="#attr['canCreate']">
    <s:form 
      action="create"
      namespace="/role"
    >
      <s:textfield name="roleName" size="20" value="" label="Role name"/>
      <s:submit value="%{'Create!'}"/>
      <s:token/>
    </s:form>
  </s:if>
</div>
