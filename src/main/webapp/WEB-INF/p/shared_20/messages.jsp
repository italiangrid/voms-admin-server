<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div id="loadDiv">
  <s:url value="/img/ajax-loader.gif" var="ajaxLoaderURL"/>
  <img src="${ajaxLoaderURL}"/>
</div>

<s:actionerror cssClass="actionErrors"/>
<s:actionmessage cssClass="message"/>

<s:property value="#authorizationException"/>