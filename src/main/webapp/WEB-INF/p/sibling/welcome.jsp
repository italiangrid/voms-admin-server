<%@ taglib
  uri="http://java.sun.com/jsp/jstl/core"
  prefix="c"%>
  
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<!-- HTTP 1.1 -->
<meta
  http-equiv="Cache-Control"
  content="no-store" />

<!-- HTTP 1.0 -->
<meta
  http-equiv="Pragma"
  content="no-cache" />

<!-- Prevents caching at the Proxy Server -->
<meta
  http-equiv="Expires"
  content="0" />

<meta
  http-equiv="Content-Type"
  content="text/html; charset=utf-8" />
<title>VOMS VOs running on this server</title>


<link
  rel="stylesheet"
  type="text/css"
  href='<c:url value="/style/style.css"/>' />

<%--
<script
  type="text/javascript"
  src='<c:url value="/common/js/jquery.js"/>'>

      
    </script>

<script
  type="text/javascript"
  src='<c:url value="/common/js/jquery-ui.js"/>'>
      
    </script>

<script
  type="text/javascript"
  src='<c:url value="/common/js/jquery.cookie.js"/>'>
      
    </script>

<script
  type="text/javascript"
  src='<c:url value="/common/js/jq-sugar.js"/>'>
    </script>
    
--%>
</head>
<body>
<div id="header"><a
  href=""
  " id="logo-img"> <img
  src="<c:url value='/img/va-logo.png'/>"
  alt="VOMS Admin" /> </a></div>
<!-- header -->
<div id="body">
  <div id="content">

    <div id="listConfiguredVOsPane" style="margin-top: 2em;">

      <h1>
        List of VOs configured on this server:
      </h1>
      <table>
        <% 
          String prefix = "https://"+ pageContext.getRequest().getLocalName()+":8443/voms";
          pageContext.setAttribute("prefix",prefix);
        %>
    
        <c:forEach var="voURL" varStatus="status" items="${configuredVOs}">
          <tr class="${ (status.index) %2 eq 0 ? 'tableRowEven': 'tableRowOdd'}">
            <td>
              <div class="voLink">
                <a href="<c:out value="${prefix}/${voURL}"/>">
                  ${voURL}
                </a>
              </div>
            </td>
          </tr> 
        </c:forEach>
      </table>
    </div>
  </div>
</div>
<div id="footer" style="text-align: center;">VOMS Admin version ${version}</div>
</div>
</body>
</html>