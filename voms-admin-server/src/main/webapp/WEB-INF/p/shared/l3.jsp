<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>

<%@include file="/WEB-INF/p/shared/meta.jsp"%>

<title>VOMS Admin &gt; ${voName}</title>

<s:url value="/assets" var="assets_prefix" />
<s:url value="/style" var="style_prefix" />

<link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">

<link href="${assets_prefix}/css/bootstrap.min.css" rel=" stylesheet" />
<link href="${assets_prefix}/css/bootstrap-theme.min.css"
	rel=" stylesheet" />

<link href="${assets_prefix}/css/voms-navbar.css" rel=" stylesheet" />

<link href="${assets_prefix}/css/voms.css" rel=" stylesheet" />

<sb:head includeStyles="false" includeScripts="false"
	includeScriptsValidation="true" />

<sj:head jqueryui="false"/>
</head>

<body>


	<tiles2:insertTemplate template="dialogs.jsp" />
	<header>
		<tiles2:insertTemplate template="nav.jsp" />
	</header>

	<section id="content">
	 <header id="content-header">
        <tiles2:insertAttribute name="header" />
    </header>
		<div class="container">
			
			<div class="row">
				<div id="left-nav" class="col-md-2">
					<tiles2:insertAttribute name="menu" />
				</div>
				<div class="col-md-10">

					<div id="body">
						<div id="noflash">
							<tiles2:insertAttribute name="content" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
  <footer>
  
  </footer>
	<%-- Keep these at the end --%>


	<!-- <script src="${assets_prefix}/js/jquery.min.js"></script> -->
	<script src="${assets_prefix}/js/bootstrap.min.js"></script>

	<script src="${assets_prefix}/js/voms.js"></script>

	<script type="text/javascript">
		ajaxBaseURL = '<s:url value="/ajax/"/>';
		memberSearchURL = '<s:url  value="/search/member.action"/>';
	</script>

	<script type="text/javascript">
		var namespaceName = '<s:property value="namespaceName"/>';
		var actionName = '<s:property value="actionName"/>';
	</script>
</body>
</html>