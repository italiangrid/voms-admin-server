<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1 class="error">An unexpected error has occured!</h1>

<div id="detailedExceptionMessage">
  <dl>
    <s:if test="exception.message != null">
      <dd>Error Message:</dd>
      <dt class="error">
        <s:property value="exception.message"/></dt>
    </s:if>
    <dd>Exception:</dd>
    <dt><s:property value="exception.class.name"/></dt>
    <dd>Caused by:</dd>
    <dt><s:property value="exception.cause"/></dt>
    <dd>Stack trace:</dd>
    <dt>
      <s:textarea value="%{exceptionStack}" 
        cssClass="stackTrace" 
        wrap="false" 
        readonly="true"
        rows="20"
      />
    </dt>
  </dl>
</div>