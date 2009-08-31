<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1 class="error">An unexpected error has occured!</h1>

<div id="detailedExceptionMessage">
  <dl>
    <s:if test="exception.message != null">
      <dt>Error Message:</dt>
      <dd class="error">
        <s:property value="exception.message"/></dd>
    </s:if>
    <dt>Exception:</dt>
    <dd><s:property value="exception.class.name"/></dd>
    <dt>Caused by:</dt>
    <dd><s:property value="exception.cause"/></dd>
    <dt>Stack trace:</dt>
    <dd>
      <s:textarea value="%{exceptionStack}" 
        cssClass="stackTrace" 
        wrap="false" 
        readonly="true"
        rows="30"
        cols="100%"/>
    </dd>
  </dl>
</div>