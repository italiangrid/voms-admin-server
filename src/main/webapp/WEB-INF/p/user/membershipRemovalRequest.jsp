<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>Request membership removal</h1>

<tiles2:insertTemplate template="../shared_20/errorsAndMessages.jsp"/>

<s:form validate="true">
  <s:hidden name="userId" value="%{id}"/>
  <s:textfield name="reason" label="Please provide a reason for your removal request"/>
  <s:submit value="%{'Submit'}"/>
</s:form>