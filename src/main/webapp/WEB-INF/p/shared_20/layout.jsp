<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>

<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
<%@include file="/WEB-INF/p/shared_20/global-page-variables.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <%@include file="/WEB-INF/p/shared/meta.jsp"%>
    
    <title>VOMS Admin &gt; ${voName}</title>
    
    <link rel="stylesheet" type="text/css"
      href="<s:url value="/style/style.css"/>" />
    
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
    <%-- <voms:currentAdmin var="currentAdmin"/> --%>
    
  <div id="header">
    <tiles2:insertAttribute name="header"/>
  </div> <!-- header -->
  
  <tiles2:insertAttribute name="menu"/>
  
  <div id="messages">
    <tiles2:insertAttribute name="messages"/>
  </div>
  
  <div id="body">
      <tiles2:insertAttribute name="leftBar" />

    <div id="content">
      <tiles2:insertAttribute name="content" />
    </div> <!-- centerPane -->
  </div> <!-- body -->
  
  <div id="footer">
    <tiles2:insertAttribute name="footer"/>
  </div> <!-- footer -->
  </body>
</html>