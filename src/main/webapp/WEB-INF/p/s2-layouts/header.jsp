<%@include file="/WEB-INF/p/s2-common/taglibs.jsp"%>
<%@ page contentType="text/xhtml; charset=UTF-8"%>

<div id="logo" class="clear">
  <a href="#" id="logo-img"> 
  <img 
    src="/Users/andrea/Desktop/design/logo2.png" alt="VOMS Admin" /> 
  </a>
</div>
<div id="version-info">
  version: <span><voms:version/></span>
</div>

<tiles2:insertAttribute name="nav"/>
<tiles2:insertAttribute name="search"/>
