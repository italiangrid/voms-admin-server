<%--

    Copyright (c) Members of the EGEE Collaboration. 2006-2009.
    See http://www.eu-egee.org/partners/ for details on the copyright holders.

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
    	Andrea Ceccanti (INFN)

--%>

<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div id="suspendCertificateDialog" title="Suspend certificate?"
	style="display: none" class="dialog">
	<p>Suspend certificate:</p>
	<div class="dialogMessage"></div>
	<label for="suspensionReason">Suspension reason:</label> <input
		type="text" size="40" name="suspensionReason"
		class="text ui-widget-content ui-corner-all" value=""
		id="suspendCertificateDialog_suspensionReasonInputField" />
</div>

<div id="suspendUserDialog" title="Suspend user?" style="display: none"
	class="dialog">
	<p>Suspend user:</p>
	<div class="dialogMessage"></div>
	<label for="suspensionReason">Suspension reason:</label> <input
		type="text" size="40" name="suspensionReason"
		class="text ui-widget-content ui-corner-all" value=""
		id="suspendUserDialog_suspensionReasonInputField" />
</div>

<div id="suspendMultiUserDialog" title="Suspend multiple users?"
	style="display: none" class="dialog">
	<p>Do you really want to suspend selected users?</p>
	<label for="suspensionReason">Suspension reason:</label> <input
		type="text" size="40" name="suspensionReason"
		class="text ui-widget-content ui-corner-all" value=""
		id="suspendMultiUserDialog_suspensionReasonInputField" />
</div>

<div id="deleteMultiUserDialog" title="Delete multiple users?"
	style="display: none" class="dialog">
	<p>Do you really want to delete selected users? This operation
		cannot be undone!</p>
	<div class="dialogMessage"></div>
</div>

<div id="deleteUserDialog" title="Delete user?" style="display: none"
	class="dialog">
	<p>Delete user:</p>
	<div class="dialogMessage"></div>
</div>

<div id="deleteGroupDialog" title="Delete group?" style="display: none"
	class="dialog">
	<p>Delete group:</p>
	<div class="dialogMessage"></div>
</div>

<div id="deleteRoleDialog" title="Delete role?" style="display: none"
	class="dialog">
	<p>Delete role:</p>
	<div class="dialogMessage"></div>
</div>

<div id="deleteAttributeClassDialog" title="Delete attribute class?"
	style="display: none" class="dialog">

	Delete attribute class:
	<div class="dialogMessage"></div>


	<p>All the associated mappings will be dropped from the database...
	</p>
</div>

<div id="suspensionReasonDialog" title="Suspend user certificate?"
	style="display: none" class="dialog">
	You are suspending the following certificate:
	<div class="dialogMessage"></div>

	<s:textfield name="suspensionReason" size="20"
		label="Please provide a reason for the suspension" />
</div>

<div id="triggerReacceptanceDialog"
	title="Trigger reacceptance of VO AUP?" style="display: none"
	class="dialog">
	Do you really want to trigger the reacceptance of the currently active
	version of the VO Acceptable Usage Policy? If you confirm, all the VO
	users will be notified via email and asked to sign again the VO AUP
	rules, version:
	<div class="dialogMessage"></div>
</div>

<div id="changeActiveAUPVersionDialog"
	title="Change active AUP version?" style="display: none" class="dialog">
	Do you really want to change the currently active version of the VO
	Acceptable Usage Policy?<br /> If you confirm, all the VO users who
	has not signed AUP version:
	<div class="dialogMessage"></div>
	<p>will be notified by email and requested to sign the AUP.</p>
</div>

<div id="deleteAUPVersionDialog" title="Delete AUP version?"
	style="display: none" class="dialog">

	Delete aup version:
	<div class="dialogMessage"></div>

	<p>All the associated acceptance records will be dropped from the
		database...</p>
</div>

 <s:iterator value="{'ConfirmCertificateSuspensionDialog','createUserDialog','createGroupDialog','createRoleDialog','ConfirmUserSuspensionDialog', 'ConfirmUserDeleteDialog'}" var="dialogID">
   <tiles2:insertTemplate template="dialog-template.jsp">
       <tiles2:putAttribute name="dialogID" value="${dialogID}"/>
   </tiles2:insertTemplate>
 </s:iterator>

