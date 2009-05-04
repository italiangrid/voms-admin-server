<%--
 Copyright (c) Members of the EGEE Collaboration. 2008.
 See http://www.eu-egee.org/partners/ for details on the copyright
 holders.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Authors:
     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
--%>
<%@ taglib
  uri="http://java.sun.com/jsp/jstl/core"
  prefix="c"%>
<%@ taglib
  uri="http://java.sun.com/jsp/jstl/functions"
  prefix="fn"%>
<%@ taglib
  uri="http://org.glite.security.voms.tags"
  prefix="voms"%>
<%@ taglib
  uri="http://struts.apache.org/tags-html"
  prefix="html"%>
<%@ taglib
  uri="http://struts.apache.org/tags-tiles"
  prefix="tiles"%>
<%@ taglib
  uri="http://struts.apache.org/tags-bean"
  prefix="bean"%>

<div id="aupPane"><c:choose>

  <c:when test="${empty aups}">
    No AUPs defined in this VO, <html:link action="/CreateAUP">create one</html:link>.
  </c:when>

  <c:otherwise>

    <div class="header2">AUPs:</div>

    <table
      class="table"
      cellpadding="0"
      cellspacing="0">


      <c:forEach
        var="aup"
        items="${aups}"
        varStatus="status">
        <tr class="aupRow">
          <td><c:url
            value="/ShowAUP.do"
            var="editAUPurl">
            <c:param
              name="id"
              value="${aup.id}" />
          </c:url><voms:link
            context="vo"
            permission="r"
            href="${editAUPurl}"
            styleClass="actionLink"
            disabledStyleClass="disabledLink">${aup.name}</voms:link></td>
        </tr>
      </c:forEach>
    </table>
  </c:otherwise>
</c:choose></div>


