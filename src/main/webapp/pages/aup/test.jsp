<?xml version="1.0"?>
<%@ taglib
  uri="http://java.sun.com/jsp/jstl/core"
  prefix="c"%>
<response>
	<elements>
	<c:forEach var="d" items="${data}">
		<element>${d}</element>
	</c:forEach>
	</elements>
</response>
	