<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div id="suspendMultiUserDialog" title="Suspend multiple users?" style="display: none" class="dialog">
  <p>
  Do you really want to suspend selected users?
  </p>
  <label for="suspensionReason">Suspension reason:</label>
  <input type="text" size="40" name="suspensionReason" class="text ui-widget-content ui-corner-all" value="" id="suspensionReasonDialogField"/>
</div>

<div id="deleteMultiUserDialog" title="Delete multiple users?" style="display: none" class="dialog">
  <p>
  Do you really want to delete selected users? This operation cannot be undone!
  </p>
  <div class="dialogMessage"></div>
</div>

<div id="deleteUserDialog" title="Delete user?" style="display: none" class="dialog">
	<p>
	Delete user:
	</p>
	<div class="dialogMessage"></div>
</div>

<div id="deleteGroupDialog" title="Delete group?" style="display: none" class="dialog">
	<p>
	Delete group:
	</p>
	<div class="dialogMessage"></div>
</div>

<div id="deleteRoleDialog" title="Delete role?" style="display: none" class="dialog">
	<p>
	Delete role:
	</p>
	<div class="dialogMessage"></div>
</div>

<div id="deleteAttributeClassDialog" title="Delete attribute class?" style="display: none" class="dialog">
	
	Delete attribute class:
	<div class="dialogMessage"></div>
	
	
	<p>
		All the associated mappings will be dropped from the database...
	</p>
</div>

<div id="suspensionReasonDialog" title="Suspend user certificate?" style="display: none" class="dialog">
	You are suspending the following certificate:
	<div class="dialogMessage"></div>
	
	<s:textfield name="suspensionReason" size="20" label="Please provide a reason for the suspension"/>
</div>

<div id="triggerReacceptanceDialog" title="Trigger reacceptance of VO AUP?" style="display: none" class="dialog">
	Do you really want to trigger the reacceptance of the currently active version of the VO Acceptable Usage Policy?
	If you confirm, all the VO users will be notified via email and asked to sign again the VO AUP rules, version:
	<div class="dialogMessage"></div>
</div> 

<div id="deleteAUPVersionDialog" title="Delete AUP version?" style="display: none" class="dialog">
	
	Delete aup version:
	<div class="dialogMessage"></div>
	
	<p>
		All the associated acceptance records will be dropped from the database...
	</p>
</div>