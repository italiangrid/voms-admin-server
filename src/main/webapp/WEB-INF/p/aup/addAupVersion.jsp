<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h2> Add a new version for '<s:property value="name"/>' AUP</h2> 
<s:form validate="true">
  <s:hidden name="aupId" value="%{model.id}"/>
  <s:textfield name="version" label="Version"/>
  <s:textfield name="url" label="URL"/>
  <s:submit value="%{'Add version'}"/>
</s:form>