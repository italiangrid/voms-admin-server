<%@include file="/WEB-INF/p/s2-common/taglibs.jsp"%>

<div id="logo" class="clear">
  <a href="#" id="logo-img"> 
  <img 
    src="/img/logo.png" alt="VOMS Admin" /> 
  </a>
</div>
<div id="version-info">
  version: <span><voms:version/></span>
</div>

<tiles2:insertAttribute name="nav"/>
<tiles2:insertAttribute name="search"/>
