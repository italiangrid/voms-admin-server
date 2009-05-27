<%@include file="/WEB-INF/p/s2-common/taglibs.jsp"%>

<div id="logo">
  <a href="<s:url value="/"/>" id="logo-img">
    <img 
    src="<s:url value="/img/logo.png"/>" alt="VOMS Admin" /> 
  </a>
  <div id="version-info">
    version: <span><voms:version/></span>
  </div>
</div>


<tiles2:insertAttribute name="nav"/>
<tiles2:insertAttribute name="search"/>
