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
	Detailed view of VO membership request # ${request.id}
</div>



<dl>
	<dt class="regLabel">
		Submission date:
	</dt>
	<dd>
		${request.creationDate }
	</dd>
	
	<dt class="regLabel">
		User DN:		
	</dt>
	<dd>
		${request.dn }		
	</dd>
	
	<dt class="regLabel">
		User CA:		
	</dt>
	<dd>
		${request.ca }		
	</dd>
	
	<dt class="regLabel">
		User CN:		
	</dt>
	<dd>
		${request.cn }		
	</dd>
	
	<dt class="regLabel">
		User email address:		
	</dt>
	<dd>
		${request.emailAddress }		
	</dd>
	
	<c:if test="${request.status gt 1}">
		<dt class="regLabel">
			Status:		
		</dt>
		<dd>
			${ request.status eq 2 ? 'approved' : 'rejected'}
		</dd>
		<dt class="regLabel">
			Evaluation date:		
		</dt>
		<dd>
			${request.evaluationDate }
		</dd>
	</c:if>
</dl>

<c:if test="${request.status eq 1}">
	<div>
	You can 
	<c:url value="/VOMembership.do" var="rejectReqURL">
			<c:param name="method" value="reject"/>
			<c:param name="requestId" value="${request.id}"/>
		</c:url>
		<voms:link
			context="vo"
			permission="REQUESTS_READ|REQUESTS_WRITE"
			href="${rejectReqURL}"
			styleClass="actionLink"
			disabledStyleClass="disabledLink"
			>
			reject 
		</voms:link>
		or 
		<c:url value="/VOMembership.do" var="approveReqURL">
			<c:param name="method" value="approve"/>
			<c:param name="requestId" value="${request.id}"/>
		</c:url>
		<voms:link
			context="vo"
			permission="REQUESTS_READ|REQUESTS_WRITE"
			href="${approveReqURL}"
			styleClass="actionLink"
			disabledStyleClass="disabledLink"
			>
			approve
		</voms:link>
		this request for membership.
	</div>
</c:if>

