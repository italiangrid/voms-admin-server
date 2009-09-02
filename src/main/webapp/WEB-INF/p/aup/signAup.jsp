<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<h1>Sign ${name} version: ${activeVersion.version}</h1>

<s:form validate="true">
  
  <s:hidden name="aupId" value="%{model.id}"/>
  <s:textarea rows="20" cols="80" value="%{model.activeVersion.URLContent}"/>
  <s:checkbox name="aupAccepted" label="I declare I have read and agree with the AUP terms displayed above" labelposition="right"/>
  <s:submit value="%{'Submit'}"/>
  
</s:form>

