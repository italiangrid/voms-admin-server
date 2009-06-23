<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>

<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <%@include file="/WEB-INF/p/shared/meta.jsp"%>
  
  <title>VOMS Admin &gt; ${voName}</title>
  
  <link rel="stylesheet" type="text/css"
    href="<s:url value="/style/2.5/traditional.css"/>" />
  
  <script type="text/javascript" src="<s:url value="/common/js/jquery.js"/>">
  	
  </script>
  
  <script type="text/javascript"
    src="<s:url value="/common/js/hoverIntent.js"/>"></script>
  <script type="text/javascript" src="<s:url value="/common/js/superfish.js"/>"></script>
  
  <script type="text/javascript" src="<s:url value="/common/js/jq-sugar.js"/>">
  </script>

  <s:head/>
</head>

<body>
  <div id="wrap">
    <div id="wrapped-content">
      
      <div id="open-bar">
        <tiles2:insertAttribute name="info"/>
      </div>
      <div id="header">
        <h1>
          <a href="<s:url value="/home/login.action"/>" id="logo">VOMS ADMIN</a>
        </h1>
        <!-- NAV -->
        <tiles2:insertAttribute name="nav"/>
        
      </div>
    
      <div id="body">
        <div id="messages" class="hidden">
          <tiles2:insertAttribute name="messages"/>
        </div>
        <!-- MENU -->
        <tiles2:insertAttribute name="menu"/>
        
        <div id="content">
          <tiles2:insertAttribute name="content"/>
        </div>
      </div>
    </div>
  </div>
  <div id="footer">
    <div class="version">
      Voms Admin version <voms:version/>
    </div>
  </div>
</body>
</html>