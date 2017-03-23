<%@include file="/jsps/taglibs.jsp"%>
<%@include file="/jsps/requests/modals.jsp"%>

<s:set
  value="pendingRequests.{? #this instanceof org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest and 
      #this.status == @org.glite.security.voms.admin.persistence.model.request.Request$STATUS@CONFIRMED }"
  var="voRequests" />
  
<s:set
  value="pendingRequests.{? #this.typeName == 'Group membership request'}"
  var="groupRequests" />

<s:set
  value="pendingRequests.{? #this.typeName == 'Role membership request'}"
  var="roleRequests" />
  
<s:set
  value="pendingRequests.{? #this.typeName == 'Certificate request'}"
  var="certificateRequests" />
  
<s:set
  value="pendingRequests.{? #this.typeName == 'Membership removal request'}"
  var="removalRequests" />
    
<div role="tabpanel">

	<ul class="nav nav-tabs" role="tablist" id="request-tab">
		<li role="presentation" class="active"><a href="#vo-requests"
			aria-controls="vo-requests" role="tab" data-toogle="tab">VO
				membership requests 
				<s:if test="#voRequests.size > 0">
					<span class="badge">
					<s:property value="#voRequests.size"/>
					</span>
				</s:if>
				</a></li>

		<li role="presentation"><a href="#group-requests"
			aria-controls="group-requests" role="tab" data-toogle="tab">
				Group requests 
				<s:if test="#groupRequests.size > 0">
				  <span class="badge">
            <s:property value="#groupRequests.size"/>
          </span></s:if>
        </a></li>

		<li role="presentation"><a href="#role-requests" aria-controls="role-requests"
			role="tab" data-toogle="tab"> Role requests 
			 <s:if test="#roleRequests.size > 0">
			  <span class="badge">
        <s:property value="#roleRequests.size"/>
        </span>
        </s:if>
        </a></li>

		<li role="presentation"><a href="#certificate-requests"
			aria-controls="certificate-requests" role="tab" data-toogle="tab">
				Certificate requests 
				<s:if test="#certificateRequests.size > 0">
				 <span class="badge">
        <s:property value="#certificateRequests.size"/>
        </span> 
				</s:if>
				</a></li>

		<li role="presentation"><a href="#removal-requests"
			aria-controls="removal-requests" role="tab" data-toogle="tab">
				Removal requests 
				<s:if test="#removalRequests.size > 0">
         <span class="badge">
        <s:property value="#removalRequests.size"/>
        </span> 
        </s:if>
				
			</a></li>

	</ul>
	<!-- Tab panes -->
	<div class="tab-content">
		<div role="tabpanel" class="tab-pane active" id="vo-requests">
		  <div class="req-panel">
			 <tiles2:insertTemplate template="vo.jsp" />
			</div>
		</div>
		<div role="tabpanel" class="tab-pane" id="group-requests">
			<tiles2:insertTemplate template="group.jsp" />
		</div>

		<div role="tabpanel" class="tab-pane" id="role-requests">
			<tiles2:insertTemplate template="role.jsp" />
		</div>

		<div role="tabpanel" class="tab-pane" id="certificate-requests">
			<tiles2:insertTemplate template="certificate.jsp" />
		</div>

		<div role="tabpanel" class="tab-pane" id="removal-requests">
			<tiles2:insertTemplate template="removal.jsp" />
		</div>
	</div>
</div>


