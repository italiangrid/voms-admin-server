<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:form
  action="decision"
  onsubmit="ajaxSubmit($(this),'pending-req-content'); return false;"
  cssClass="decisionForm"
  theme="simple">
  <s:token />

  <s:hidden
    name="requestId"
    value="%{id}" />

  <s:hidden
    name="decision"
    value="reject" />

  <s:submit
    name="submit"
    value="approve"
    onclick="this.form.decision.value = this.value" />

  <s:submit
    name="submit"
    value="reject"
    onclick="this.form.decision.value = this.value" />
</s:form>