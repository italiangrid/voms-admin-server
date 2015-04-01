<!DOCTYPE html>
<%@include file="/jsps/taglibs.jsp"%>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <s:url value="/assets" var="assets_prefix" />
      <s:url action="index" var="home_url" namespace="/v4/requests" />

        <link href="${assets_prefix}/css/bootstrap.min.css" rel=" stylesheet" />
        <link href="${assets_prefix}/css/bootstrap-theme.min.css"
              rel=" stylesheet" />
        <link href="${assets_prefix}/css/voms.css" rel=" stylesheet" />
        <link href="${assets_prefix}/css/voms-navbar.css" rel=" stylesheet" />


        <title>VOMS Admin server for VO ${voName}</title>
  </head>
  <body>

    <div class="container">
      <%@include file="/jsps/navbar.jsp"%>
      <tiles2:insertAttribute name="body"/>
    </div>

    <%-- Keep these at the end --%>
    <script src="${assets_prefix}/js/jquery.min.js"></script>
    <script src="${assets_prefix}/js/bootstrap.min.js"></script>
    <script src="${assets_prefix}/js/voms.js"></script>

  </body>
</html>

