<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if
	test="not pendingRequests.{? #this instanceof org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest and #this.status == @org.glite.security.voms.admin.persistence.model.request.Request$STATUS@SUBMITTED }.empty">
	<h1>Unconfirmed VO membership requests:</h1>
	<table>
		<thead>
			<tr>
				<th>Requester</th>
				<th>Personal Info</th>
				<th />
			</tr>
		</thead>
		<tbody>
		<s:iterator
			value="pendingRequests.{? #this instanceof org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest and 
			#this.status == @org.glite.security.voms.admin.persistence.model.request.Request$STATUS@SUBMITTED }">
			
			<tr>
				<td><tiles2:insertTemplate template="userInfo.jsp" flush="true" />
				</td>


				<td class="personalInfo">
				<dl>
					<dt>Address:</dt>
					<dd>${requesterInfo.address}</dd>
					<dt>Phone number:</dt>
					<dd>${requesterInfo.phoneNumber}</dd>
					<dt>Email:</dt>
					<dd>${requesterInfo.emailAddress}</dd>
				</dl>
				</td>

				<td style="vertical-align: bottom; text-align: right;">
				<s:form
					action="drop-request" onsubmit="ajaxSubmit($(this),'pending-req-content'); return false;">
					<s:hidden name="requestId" value="%{id}" />
					<s:submit value="%{'Drop this request'}"/>
				</s:form>
				</td>
			</tr>
		</s:iterator>
		</tbody>
	</table>
	
</s:if>