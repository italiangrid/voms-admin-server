<%-- JSTL tags --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">
        <title>VOMSES Web application</title>
        <!-- <link rel="stylesheet" href="css/main.css"/> -->
        <link rel="stylesheet" href="css/bootstrap.min.css"/>
        
        <style type="text/css">
            html,body { text-align: center }
            
            #footer {
                text-align: center;
                padding-top: .5em;
            }
            
        </style>
    </head>
    <body>
         
        <div class="container">
            <h3>VOMS Admin service endpoints</h3>
            
            <table class="table table-striped table-hover">
                <tbody>
                    <c:forEach var="endpoint" items="${endpoints}">
	                    <tr>
	                        <a href="https://${endpoint.host}:${endpoint.port}/voms/${endpoint.voName}">${endpoint.voName}</a>
	                    </tr>
                    </c:forEach>
                </tbody>
            </table>
            
        </div>
        
        <div class="navbar navbar-fixed-bottom">
            <div class="navbar-inner">
                <div class="container">
                    <div id="footer">
                        <small>
                            VOMS Admin version ${version}
                        </small>    
                    </div>
                </div>
            </div>
        </div>
                
    </body>
</html>