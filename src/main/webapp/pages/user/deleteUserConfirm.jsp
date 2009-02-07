<%--
 Copyright (c) Members of the EGEE Collaboration. 2006.
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://org.glite.security.voms.tags" prefix="voms"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>


<c:choose>

<c:when test="${! empty vomsUser}">

	<div class="confirmForm">
		<html:form action="/DeleteUser" method="post" focus="cancelButton">
			<div class="confirmMessage"> 
				Delete user ?
				<div class="userDNCA">
					<div class="userDN">${vomsUser.dn}</div>
					<div class="userCA">${vomsUser.ca.subjectString}</div>
				</div>
			</div>
			<html:hidden property="id" value="${vomsUser.id}"/>
			<html:submit styleClass="submitButton" value="Delete"/>
			<html:cancel styleId="cancelButton" styleClass="submitButton"/>
		</html:form>
	</div>
</c:when>

<c:otherwise>
	<div class="error">
	 	No user found in page context!
 	 </div>
</c:otherwise>

</c:choose>