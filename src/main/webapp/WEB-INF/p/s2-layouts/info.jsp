<%@include file="/WEB-INF/p/s2-common/taglibs.jsp"%>

<div id="vo-info">
  vo: <span id="vo-name">${voName}</span>
</div>

<div id="user-info">
 Identity: <span class="userDN" title="${currentAdmin.realSubject}"><voms:formatDN dn="${currentAdmin.realSubject}" fields="CN"/></span>
</div>