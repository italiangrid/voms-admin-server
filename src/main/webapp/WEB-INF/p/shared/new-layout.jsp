<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>

<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<%@include file="/WEB-INF/p/shared/meta.jsp" %>

<title><tiles2:insertAttribute name="title"/></title>

<link rel="stylesheet" type="text/css" href="<s:url value="/style/2.5/style.css"/>"/>

<script type="text/javascript" src="<s:url value="/common/js/jquery.js"/>">
</script>

<script type="text/javascript" src="<s:url value="/common/js/hoverIntent.js"/>"></script>
<script type="text/javascript" src="<s:url value="/common/js/superfish.js"/>"></script>

<script type="text/javascript" src="<s:url value="/common/js/jq-sugar.js"/>">
</script>

</head>

<body>
    <div id="container">
    
      <div id="info" class="clear">
        <tiles2:insertAttribute name="info"/>
      </div>
      
	   <div id="header" class="clear">
         <tiles2:insertAttribute name="header"/>
	   </div> <!-- header -->
     
	   <div id="messages" class="clear">
		  <tiles2:insertAttribute name="messages"/>
	   </div><!-- messages -->

	   <div id="body" class="clear">
		  <tiles2:insertAttribute name="body"/>
	   </div> <!-- body -->
     
    </div><!-- container -->
    
    <div id="footer" class="clear">
      <tiles2:insertAttribute name="footer"/>      
    </div> <!-- footer -->
</body>
</html>
