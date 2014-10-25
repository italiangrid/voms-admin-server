<%-- JSTL tags --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:layout>
	<jsp:body>
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
  </jsp:body>
</t:layout>