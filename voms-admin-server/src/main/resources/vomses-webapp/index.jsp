<%-- JSTL tags --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<title>VOMSES Web application</title>
<!-- <link rel="stylesheet" href="css/main.css"/> -->
<link rel="stylesheet" href="css/bootstrap.min.css" />

<style type="text/css">
html,body {
	text-align: center;
	height: 100%;
}

#wrap {
  min-height: 100%;
  height: auto !important;
  height: 100%;
  margin: 0 auto -40px;
}

#push,
#footer {
	text-align: center;
	height: 40px;
}

#footer {
  background-color: #f5f5f5;
  padding-top: .3em;
}

.endpoint {
	float: left;
}

.status {
	float: right;
}

.vo-info {
	font-size: smaller;
	font-weight: bold;
}

.vo-info-down {
	color: #b94a48;
}

.vo-info-active {
	color: #468847;
}
</style>
</head>
<body>
  <div id="wrap">
		<div class="container">
			<div class="page-header">
				<h1>VOMS Admin endpoints</h1>
				<h3 class="muted">${host}</h3>
				<small>This page lists the locally configured Virtual
					Organizations</small>
			</div>
			<p></p>
		</div>
		<c:choose>
			<c:when test="${empty endpoints}">
				<div class="container">
					<div class="row">
						<div class="span6 offset3">
							<div class="alert alert-warning">No locally configured VOs.
							</div>
						</div>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="container">
	
					<div class="row">
						<div class="span6 offset3">
							<div class="alert alert-info">
								<small>Status information is computed checking the VOMS
									admin services state</small>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="span6 offset3">
							<table class="table table-hover table-bordered center-table">
								<tbody>
									<c:forEach var="endpoint" items="${endpoints}">
										<tr>
											<td>
												<div class="endpoint">
													<a
														href="https://${endpoint.host}:${endpoint.port}/voms/${endpoint.voName}">${endpoint.voName}</a>
												</div>
												<div class="status">
													<c:choose>
														<c:when test="${statusMap[endpoint.voName]}">
															<span class="vo-info vo-info-active">active</span>
														</c:when>
														<c:otherwise>
															<span class="vo-info vo-info-down">currently down</span>
														</c:otherwise>
													</c:choose>
												</div>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		<div id="push"></div>
  </div>
  <div id="footer">
		<div class="container">
				<div id="footer">
					<small class="muted">VOMS Admin version ${version}</small>
				</div>
    </div>
  </div>
</body>
</html>