<%--

    Copyright (c) Members of the EGEE Collaboration. 2006-2009.
    See http://www.eu-egee.org/partners/ for details on the copyright holders.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    Authors:
    	Andrea Ceccanti (INFN)

--%>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/WEB-INF/p/shared/meta.jsp" %>

<title><tiles2:insertAttribute name="title"/></title>

<link rel="stylesheet" type="text/css" href="<s:url value="/style/style.css"/>"/>

<script type="text/javascript" src="<s:url value="/common/js/jquery.js"/>">
</script>

<script type="text/javascript" src="<s:url value="/common/js/jq-sugar.js"/>">
</script>



</head>

<body>
	<div id="header">
		<tiles2:insertAttribute name="header"/>
	</div> <!-- header -->

	<div id="menu">
		<tiles2:insertAttribute name="menu"/>
	</div> <!-- menu -->
	
	<div id="messages">
		<tiles2:insertAttribute name="messages"/>
	</div><!-- messages -->
	
	<div id="body">
		<div id="leftBar">
			<tiles2:insertAttribute name="leftBar"/>
		</div> <!-- leftBar -->

		<div id="centerPane">
			<tiles2:insertAttribute name="centerPane"/>
		</div> <!-- centerPane -->
	</div> <!-- body -->

	<div class="separator">
	&nbsp;
	</div>
	
	<div id="footer">
		<tiles2:insertAttribute name="footer"/>
	</div> <!-- footer -->
</body>
</html>
