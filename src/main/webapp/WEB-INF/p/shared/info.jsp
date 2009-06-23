<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
vo: <span class="highlight">${voName}</span> |
user: <span class="highlight"><voms:formatDN dn="${currentAdmin.realSubject}" fields="CN"/></span>
