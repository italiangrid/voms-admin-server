<%@include file="/jsps/taglibs.jsp"%>

<div id="reject-vo-reqs-modal" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Reject requests?</h4>
			</div>
			<div class="modal-body">
				<p>
				  You are about to reject <span id="rej-req-count">X</span> VO membership requests.<br>
				  Are you sure?
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-danger">Reject
					requests</button>
			</div>
		</div>
	</div>
</div>
