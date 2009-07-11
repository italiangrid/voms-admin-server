<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h2>Please enter a reason for user certificate suspension</h2>

<div>
  Certificate subject:
</div>
<div>
  <s:property value="certificate.subjectString"/>
</div>
<div>
  Certificate issuer:
</div>

<div>
  <s:property value="certificate.ca.subjectString"/>
</div>
  
<s:form action="suspend-certificate" namespace="/user">
  <s:token/>
  <s:hidden name="userId" value="%{model.id}"/>
  <s:hidden name="certificateId" value="%{certificate.id}"/>
  <s:textfield name="suspensionReason" label="Reason" size="20"/>
  <s:submit value="%{'Suspend user certificate'}"/>
</s:form>