<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<a href="<s:url value="/home/login.action"/>" id="logo-img">
  <img src="<s:url value="/img/va-logo.png"/>" alt="VOMS Admin" /> 
</a>

  
  
  <div id="vo-info">
    for VO: <span>${voName}</span>
  </div>
  
  <div id="admin-info">
    Current user: 
    <span title="${currentAdmin.realSubject}">
        <voms:formatDN dn="${currentAdmin.realSubject}" fields="CN"/>
    </span>
  </div>
