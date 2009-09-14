<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<h1>VO Acceptable usage policy version ${activeVersion.version}</h1>

<s:form validate="true">
  
  <s:hidden name="aupId" value="%{model.id}"/>
  <s:textarea rows="24" cols="100%" value="%{model.activeVersion.URLContent}" readonly="true"/>
  <s:checkbox name="aupAccepted" label="I declare I have read and agree with the AUP terms displayed above" labelposition="right"/>
  <s:submit value="%{'Submit'}" align="left"/>
  
</s:form>

