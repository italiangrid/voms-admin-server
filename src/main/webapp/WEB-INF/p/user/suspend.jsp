<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h2>Please enter a reason for user suspension</h2>
<s:form action="suspend" namespace="/user">
  <s:token/>
  <s:hidden name="userId" value="%{model.id}"/>
  <s:textfield name="suspensionReason" label="Reason" size="20"/>
  <s:submit value="%{'Suspend user'}"/>
</s:form>