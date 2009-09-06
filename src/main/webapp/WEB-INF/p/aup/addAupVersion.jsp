<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1> Add a new version for '<s:property value="name"/>' AUP</h1> 

<s:actionerror/>

<s:form validate="true">
  <s:hidden name="aupId" value="%{model.id}"/>
  <s:textfield name="version" label="Version"/>
  <s:textfield name="url" label="URL" size="40"/>
  <s:submit value="%{'Add version'}"/>
</s:form>