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
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<div class="header1">
	Welcome to ${voName}, <voms:formatDN dn="${currentAdmin.realSubject}" fields="CN"/>.
</div>

<c:choose>
	<c:when test="${request.status eq 0}">
		<p>
			We have already received your membership request but are still waiting for 
			you to confirm it. Check your inbox!
		</p>
	</c:when>
	<c:otherwise>
		<p>
			Your membership request is being handled by the VO administrators. You will
			receive a notification of their decision regarding your request shortly.
		</p>
	</c:otherwise>
</c:choose>