<%-- JSTL tags --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:layout>
	<jsp:body>
      <c:choose>
      <c:when test="${empty voNames}">
        <div class="container">
          <div class="row">
            <div class="span6 offset3">
              <div class="alert alert-warning">No locally configured VOs.
              </div>
            </div>
          </div>
        </div>
      </c:when>
      <c:otherwise>
        <div class="container">
          <div class="row">
            <div class="span6 offset3">
              <table class="table table-hover table-bordered center-table">
                <tbody>
                  <c:forEach var="vo" items="${voNames}">
                    <tr>
                      <td>
                        <div class="endpoint">
                          <a href="https://${host}:${port}/voms/${vo}">${vo}</a>
                        </div>
                        <div class="status">
                          <c:choose>
                            <c:when test="${statusMap[vo]}">
                              <span class="vo-info vo-info-active">active</span>
                            </c:when>
                            <c:otherwise>
                              <span class="vo-info vo-info-down">currently down</span>
                            </c:otherwise>
                          </c:choose>
                        </div>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </c:otherwise>
    </c:choose>
  </jsp:body>
</t:layout>