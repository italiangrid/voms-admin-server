<%@include file="/jsps/taglibs.jsp"%>

<p>
<s:if test="#voRequests.size == 0">
  No requests found.
</s:if>

<s:else>
<table class="table table-striped">
	<thead>
		<tr>
			<th>
			 <input 
			   id="vo-req-toggler" 
			   type="checkbox">
			 </th>
			<th>Requester</th>
			<th>Certificate</th>
			<th>
				<div class="pull-right">
					<button id="vo-req-approve-btn" 
					type="button" class="btn btn-success btn-sm vo-req-buttons" disabled="disabled">
					 <span class="glyphicon glyphicon-ok"></span> 
					Approve request</button>
					<button id="vo-req-reject-btn" type="button" 
					class="btn btn-danger btn-sm vo-req-buttons"  disabled="disabled"
					data-toggle="modal" data-target="#reject-vo-reqs-modal">
					 <span class="glyphicon glyphicon-remove"></span>
					Reject request</button>
				</div>
			</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="voRequests" var="req">
			<tr>
				<td class="col-xs-1">
				  <input 
				  class="vo-req-checkbox"
				  type="checkbox" name="reqId" value="${id}">
				</td>
				<td class="col-xs-4">
					<address>
						<strong>${requesterInfo.name} ${requesterInfo.surname}</strong><br>
						${requesterInfo.institution}
						<s:if test="%{requesterInfo.address != null && requesterInfo.address != ''}">
							<br>
							${requesterInfo.address}
            </s:if>
						<s:if test="%{requesterInfo.phoneNumber != null && requesterInfo.phoneNumber != ''}">
							<br>
							<i class="glyphicon glyphicon-earphone"></i>
							${requesterInfo.phoneNumber}
            </s:if>
						<br> <a href="mailto:${requesterInfo.emailAddress}">
							${requesterInfo.emailAddress}</a>
					</address>
				</td>
				<td>${requesterInfo.certificateSubject}<br> <small>${requesterInfo.certificateIssuer}</small>
				</td>
				<td>
				  <div class="pull-right">
				    <div class="btn-group btn-group-sm" role="group">
				    <button
              type="button" class="btn btn-success vo-req-i-buttons">
              <span class="glyphicon glyphicon-ok"></span>
           </button> 
          <button type="button" class="btn btn-danger vo-req-i-buttons">
           <span class="glyphicon glyphicon-remove"></span></button>
           </div> 
				  </div>
				</td>
			</tr>
		</s:iterator>
	</tbody>
</table>
</s:else>
<p>