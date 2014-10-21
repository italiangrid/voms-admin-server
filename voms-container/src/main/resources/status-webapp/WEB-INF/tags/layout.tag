<%@tag description="layout Tag" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<title>VOMS Admin Server</title>

<link rel="stylesheet" href="<c:url value="/css/bootstrap.min.css"/>" />

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
}

#version{
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
    <div id="body">
      <jsp:doBody/>
    </div>
    <div id="push"></div>
  </div>
  <div id="footer">
    <div class="container">
        <div id="version">
          <small class="muted">VOMS Admin version ${version}</small>
        </div>
    </div>
  </div>
</body>
</html>