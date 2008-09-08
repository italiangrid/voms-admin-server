<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://org.glite.security.voms.tags" prefix="voms"%>

<c:url value="/style/style.css" var="stylePath" scope="session"/>
<c:url value="/common/js/functions.js" var="functionsPath" scope="session"/>
<c:url value="/common/js/prototype.js" var="prototypePath" scope="session"/>


<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><tiles:insert attribute="title"/></title>
<link rel="stylesheet" type="text/css" href="${stylePath}"/>


<script type="text/javascript" src="${prototypePath}">
</script>

<script type="text/javascript" src="${functionsPath}">
</script>

</head>
<body>

	<voms:currentAdmin var="currentAdmin"/>
	<div id="header">
		<tiles:insert attribute="header"/>
	</div> <!-- header -->

	<div id="menu">
		<tiles:insert attribute="menu"/>
	</div> <!-- menu -->
	
	<!-- 
	<div id="headerInfo">
		<table width="100%">
			<tr>
				<td style="text-align: left">
					VO name: <span id="voName">${voName}</span>.
				</td>
				<td style="text-align: right">
					Current user: <span id='adminDN' title="${currentAdmin.realSubject}"><voms:formatDN dn="${currentAdmin.realSubject}" fields="CN"/></span>.				
				</td>
			</tr>
		</table>
	</div>
	-->
	
	<div id="messages">
		<tiles:insert attribute="messages"/>
	</div>
	
	<div id="body">
		<div id="leftBar">
			<tiles:insert attribute="leftBar" />
		</div> <!-- leftBar -->

		<div id="centerPane">
			<tiles:insert attribute="centerPane" />
		</div> <!-- centerPane -->
	</div> <!-- body -->

	<div class="separator">
	&nbsp;
	</div>
	
	<div id="footer">
		<tiles:insert attribute="footer"/>
	</div> <!-- footer -->
</body>
</html>
