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
  text-align: center
}

#footer {
  text-align: center;
  padding-top: .5em;
  color: white;
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
  <div class="container">
      <div class="page-header">
        <h1>VOMS Admin endpoints</h1>
        <h3 class="muted">${host}</h3>      
      </div>    
  </div>
  <div class="container">
    <div class="row">
          <div class="span6 offset3">
            <div class="alert alert-error">
              <strong>404!</strong>
              There's nothing nice to be seen  here.
            </div>
          </div>
    </div>
    <div class="row">
      <a href="/" class="btn btn-success">Back to VO index</a>
    </div>
  </div>  
  
  <div class="navbar navbar-fixed-bottom navbar-inverse">
    <div class="navbar-inner">
      <div class="container">
        <div id="footer">
          <small> VOMS Admin version ${version} </small>
        </div>
      </div>
    </div>
  </div>
</body>
</html>