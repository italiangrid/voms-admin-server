<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h2>Sign <s:property value="name"/> current version</h2>

<s:form validate="true">
  
  <s:hidden name="aupId" value="%{model.id}"/>
  <s:textarea rows="20" cols="80" value="%{model.activeVersion.URLContent}"/>
  <s:checkbox name="aupAccepted" label="I declare I have read and agree with the AUP terms displayed above"/>
  <s:submit value="%{'Submit'}"></s:submit>
  
</s:form>

