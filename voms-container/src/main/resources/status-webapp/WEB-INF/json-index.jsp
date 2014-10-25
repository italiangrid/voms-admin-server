<%@ page trimDirectiveWhitespaces="true" contentType="application/json; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
{
  "vos": [
    <c:forEach var="vo" items="${voNames}">
      { "name" : "${vo}",
        "url" : "https://${host}:${port}/voms/${vo}",
        "active" : "${statusMap[vo]}"
      }
    </c:forEach>
  ]
}