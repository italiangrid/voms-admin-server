<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="application/json; charset=UTF-8" %>
{
  "vos": [
    <c:forEach var="endpoint" items="${endpoints}">
      { "name" : "${endpoint.voName}",
        "url" : "${endpoint.URL}",
        "active": "${statusMap[endpoint.voName]}"
      }
    </c:forEach>
  ]
}