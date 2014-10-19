<%@ page trimDirectiveWhitespaces="true"  contentType="text/plain; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:forEach var="vo" items="${voNames}">
${vo} : ${ statusMap[vo] ? "active" : "down"}<% out.println(); %> 
</c:forEach>