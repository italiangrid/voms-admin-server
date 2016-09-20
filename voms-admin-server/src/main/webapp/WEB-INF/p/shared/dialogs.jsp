<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div
  id="suspendCertificateDialog"
  title="Suspend certificate?"
  style="display: none"
  class="dialog">
  <p>Suspend certificate:</p>
  <div class="dialogMessage"></div>
  <label for="suspensionReason">Suspension reason:</label> <input
    type="text"
    size="40"
    name="suspensionReason"
    class="text ui-widget-content ui-corner-all"
    value=""
    id="suspendCertificateDialog_suspensionReasonInputField" />
    
    <div class="alert alert-error">
      <strong>Please provide a reason for the suspension!</strong>
    </div>
</div>

<div
  id="suspendUserDialog"
  title="Suspend user?"
  style="display: none"
  class="dialog">
  <p>Suspend user:</p>
  <div class="dialogMessage"></div>
  <label for="suspensionReason">Suspension reason:</label> <input
    type="text"
    size="40"
    name="suspensionReason"
    class="text ui-widget-content ui-corner-all"
    value=""
    id="suspendUserDialog_suspensionReasonInputField" />
    
    <div class="alert alert-error">
      <strong>Please provide a reason for the suspension!</strong>
    </div>
</div>

<div
  id="suspendMultiUserDialog"
  title="Suspend multiple users?"
  style="display: none"
  class="dialog">
  <p>Do you really want to suspend selected users?</p>
  <label for="suspensionReason">Suspension reason:</label> <input
    type="text"
    size="40"
    name="suspensionReason"
    class="text ui-widget-content ui-corner-all"
    value=""
    id="suspendMultiUserDialog_suspensionReasonInputField" />
    
    <div class="alert alert-error">
      <strong>Please provide a reason for the suspension!</strong>
    </div>
</div>

<div
  id="deleteMultiUserDialog"
  title="Delete multiple users?"
  style="display: none"
  class="dialog">
  <p>Do you really want to delete selected users? This operation cannot be undone!</p>
  <div class="dialogMessage"></div>
</div>

<div
  id="deleteUserDialog"
  title="Delete user?"
  style="display: none"
  class="dialog">
  <p>Delete user:</p>
  <div class="dialogMessage"></div>
</div>

<div
  id="deleteGroupDialog"
  title="Delete group?"
  style="display: none"
  class="dialog">
  <p>Delete group:</p>
  <div class="dialogMessage"></div>
</div>

<div
  id="deleteRoleDialog"
  title="Delete role?"
  style="display: none"
  class="dialog">
  <p>Delete role:</p>
  <div class="dialogMessage"></div>
</div>

<div
  id="deleteAttributeClassDialog"
  title="Delete attribute class?"
  style="display: none"
  class="dialog">

  Delete attribute class:
  <div class="dialogMessage"></div>


  <p>All the associated mappings will be dropped from the database...</p>
</div>

<div
  id="suspensionReasonDialog"
  title="Suspend user certificate?"
  style="display: none"
  class="dialog">
  You are suspending the following certificate:
  <div class="dialogMessage"></div>
  <s:textfield
    name="suspensionReason"
    size="20"
    label="Please provide a reason for the suspension" />
    
    <div class="alert alert-error">
      <strong>Please provide a reason for the suspension!</strong>
    </div>
</div>

<div
  id="changeReacceptancePeriodDialog"
  title="Change acceptance period of VO AUP?"
  style="display: none"
  class="dialog">
  Do you really want to change the acceptance period of the currently active version of the VO Acceptable Usage Policy?
  If you confirm, many users could be asked to sign again the VO AUP.
  <div style="margin-bottom: 1em; margin-top: 0.5em;">
    <input
      type="text"
      value=""
      placeholder="Type yes to confirm..." />
  </div>
  <div class="alert alert-error">
    <strong>Type 'yes' to change the AUP acceptance period!</strong>
  </div>
</div>

<div
  id="triggerReacceptanceDialog"
  title="Trigger acceptance of VO AUP?"
  style="display: none"
  class="dialog">
  Do you really want to trigger the acceptance of the currently active version of the VO Acceptable Usage Policy? If you
  confirm, all the VO users will be notified via email and asked to sign again the VO AUP rules.
  <div style="margin-bottom: 1em; margin-top: 0.5em;">
    <input
      type="text"
      value=""
      placeholder="Type yes to confirm..." />
  </div>
  <div class="alert alert-error">
    <strong>Type 'yes' to trigger the AUP acceptance!</strong>
  </div>
</div>

<div
  id="changeActiveAUPVersionDialog"
  title="Change active AUP version?"
  style="display: none"
  class="dialog">
  Do you really want to change the currently active version of the VO Acceptable Usage Policy?<br /> If you confirm,
  all the VO users who has not signed AUP version:
  <div class="dialogMessage"></div>
  <p>will be notified by email and requested to sign the AUP.</p>
</div>

<div
  id="deleteAUPVersionDialog"
  title="Delete AUP version?"
  style="display: none"
  class="dialog">

  Delete aup version:
  <div class="dialogMessage"></div>

  <p>All the associated acceptance records will be dropped from the database...</p>
</div>

<div
  id="confirmRejectedRequestDialog"
  title="Reject request(s)?"
  style="display: none"
  class="dialog">

  <div id="rejected-reqs-details"></div>

  <p>
    <input
      id="confirmRejectedRequestDialog_input"
      type="textarea"
      placeholder="Please provide a reason for rejecting the requests..."
      style="width: 100%; heigth: auto;"></input>

  </p>
</div>

<div
  id="confirmOrgDbIdChangeDialog"
  title="Change HR id for user?"
  style="display: none"
  class="dialog">
  <div class="dialogMessage"></div>
  <p>All personal information about the user (name, surname, institution, email) will be linked to the CERN HR
    membership record with ID:</p>
  <div class="new-hr-id"></div>
  <p>The actual synchronization will happen at the next run of the CERN HR membership synchronization background
    task. Proceed?</p>

  <div style="margin-bottom: 1em; margin-top: 0.5em;">
    <input
      class="hr-id-confirm"
      type="text"
      value=""
      placeholder="Type yes to confirm..." />
  </div>

  <div class="alert alert-error">
    <strong>Type 'yes' to change the HR id!</strong>
  </div>
</div>

<div
  id="confirmOwnCertificateRemovalDialog"
  title="Remove secondary certificate?"
  style="display: none"
  class="dialog">

  <p style="margin-bottom: 1em">The following certificate will be removed from your membership:</p>
  <div class="userDN"></div>
  <div class="userCA"></div>
  <p>Proceed?</p>

  <div style="margin-bottom: 1em; margin-top: 0.5em;">
    <input
      class="rm-cert-confirm"
      type="text"
      value=""
      placeholder="Type yes..." />
  </div>
  <div class="alert alert-error">
    <strong>Type 'yes' to remove the certificate!</strong>
  </div>
</div>

<div
  id="confirmCertificateRemovalDialog"
  title="Remove user certificate?"
  style="display:none"
  class="dialog">
  
  <p style="margin-bottom: 1em">The following certificate:</p>
  <div class="userDN"></div>
  <div class="userCA"></div>
  <p style="margin-bottom: 1em">will be removed from the set of certificates linked to user:</p>
  <div class="userInfo"></div>
  <p>Proceed?</p>
  <div style="margin-bottom: 1em; margin-top: 0.5em;">
    <input
      class="rm-cert-confirm"
      type="text"
      value=""
      placeholder="Type yes..." />
  </div>
  <div class="alert alert-error">
    <strong>Type 'yes' to remove the certificate!</strong>
  </div>
  
  
  </div>
