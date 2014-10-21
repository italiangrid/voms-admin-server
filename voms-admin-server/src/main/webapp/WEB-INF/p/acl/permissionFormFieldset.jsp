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
    <tr>
      <td colspan="4">
        <div id="permissionShortcuts">
          <span class="label">Select:</span>
            <span class="clickable" id="allPermissionHandle">all</span>|<span class="clickable" id="browsePermissionHandle">browse</span>|<span class="clickable" href="" id="noPermissionHandle">none</span>
        </div>
      </td>
    </tr>
    <tr class="tableRowOdd borderTop">
      <td id="containerHandle" class="permHeader clickable">Container rights:</td>
      <th class="permissionName"><s:checkbox name="selectedPermissions"
        fieldValue="%{'CONTAINER_READ'}" cssClass="permissionCheckbox"
        value="%{permission.has('CONTAINER_READ')}" theme="simple"/>
        CONTAINER_READ
      </th>
      
      <th class="permissionName">
        <s:checkbox name="selectedPermissions"
        fieldValue="%{'CONTAINER_WRITE'}" cssClass="permissionCheckbox"
        value="%{permission.has('CONTAINER_WRITE')}" theme="simple"/>
        CONTAINER_WRITE</th>
      <th />
    </tr>

    <tr class="tableRowOdd">
      <td colspan="4">
        <div class="permissionHelp">
          These flags are used to control access to the operations 
          that list/alter the VO internal structure (groups and roles list/creations/deletions, 
          user creations/deletions).
        </div>
      </td>
      
    </tr>

    <tr>
      <th id="membershipHandle" class="permHeader clickable">Membership rights</th>  
      <th class="permissionName">
        <s:checkbox name="selectedPermissions"
        fieldValue="%{'MEMBERSHIP_READ'}" cssClass="permissionCheckbox"
        value="%{permission.has('MEMBERSHIP_READ')}" theme="simple"/>
      MEMBERSHIP_READ</th>
      <th class="permissionName">
        <s:checkbox name="selectedPermissions"
        fieldValue="%{'MEMBERSHIP_WRITE'}" cssClass="permissionCheckbox"
        value="%{permission.has('MEMBERSHIP_WRITE')}" theme="simple" />
      MEMBERSHIP_WRITE</th>
      <th />
    </tr>
    <tr>
      <td colspan=41">
        <div class="permissionHelp">
          These flags are used to control access to operations that manage/list membership in group and roles.
        </div>
      </td>
    </tr>
    <tr class="tableRowOdd">
      <th id="aclHandle" class="permHeader  clickable">ACL management rights:</th>
      <th class="permissionName"><s:checkbox name="selectedPermissions"
        fieldValue="%{'ACL_READ'}" cssClass="permissionCheckbox"
        value="%{permission.has('ACL_READ')}" theme="simple"/>
      ACL_READ</th>
      <th class="permissionName"><s:checkbox name="selectedPermissions"
        fieldValue="%{'ACL_WRITE'}" cssClass="permissionCheckbox"
        value="%{permission.has('ACL_WRITE')}" theme="simple"/>
      ACL_WRITE</th>
      <th class="permissionName"><s:checkbox name="selectedPermissions"
        fieldValue="%{'ACL_DEFAULT'}" cssClass="permissionCheckbox"
        value="%{permission.has('ACL_DEFAULT')}" theme="simple"/>
        ACL_DEFAULT</th>
    </tr>
    <tr class="tableRowOdd">
      <td colspan="4">
        <div class="permissionHelp">
          These flags are used to control access to operations that manage VO Access Control Lists (ACLs).
        </div>
      </td>
    </tr>
    <tr class="clickable">
      <th id="gaHandle" class="permHeader  clickable">Generic Attributes rights:</th>
      <th class="permissionName"><s:checkbox name="selectedPermissions"
        fieldValue="%{'ATTRIBUTES_READ'}" cssClass="permissionCheckbox"
        value="%{permission.has('ATTRIBUTES_READ')}" theme="simple"/>
      ATTRIBUTES_READ</th>
      <th class="permissionName"><s:checkbox name="selectedPermissions"
        fieldValue="%{'ATTRIBUTES_WRITE'}" cssClass="permissionCheckbox"
        value="%{permission.has('ATTRIBUTES_WRITE')}" theme="simple"/>
      ATTRIBUTES_WRITE</th>
      <th />
    </tr>
    <tr>
      <td colspan="4">
        <div class="permissionHelp">
          These flags are used to control access to operations that manage generic attributes 
          (at the user, group, or role level).
        </div>
      </td>
    </tr>
    <tr class="tableRowOdd clickable">
      <th id="reqHandle" class="permHeader  clickable">User request management rights:</th>
      <th class="permissionName"><s:checkbox name="selectedPermissions"
        fieldValue="%{'REQUESTS_READ'}" cssClass="permissionCheckbox"
        value="%{permission.has('REQUESTS_READ')}" theme="simple"/>
      REQUESTS_READ</th>
      <th class="permissionName"><s:checkbox name="selectedPermissions"
        fieldValue="%{'REQUESTS_WRITE'}" cssClass="permissionCheckbox"
        value="%{permission.has('REQUESTS_WRITE')}" theme="simple"/>
      REQUESTS_WRITE</th>
      <th />
    </tr>
    <tr class="tableRowOdd">
      <td colspan=41">
        <div class="permissionHelp">
          These flags are used to control access to operations that manage user requests regarding the vo, 
          group membership, role assignment, generic attribute assignment, renewal of the membership etc...
        </div>
      </td>
    </tr>
    <tr class="clickable">
      <th id="piHandle" class="permHeader  clickable">User personal information access rights:</th>
      <th class="permissionName"><s:checkbox name="selectedPermissions"
        fieldValue="%{'PERSONAL_INFO_READ'}" cssClass="permissionCheckbox"
        value="%{permission.has('PERSONAL_INFO_READ')}" theme="simple"/>
      PERSONAL_INFO_READ</th>
      <th class="permissionName"><s:checkbox name="selectedPermissions"
        fieldValue="%{'PERSONAL_INFO_WRITE'}" cssClass="permissionCheckbox"
        value="%{permission.has('PERSONAL_INFO_WRITE')}" theme="simple"/>
        PERSONAL_INFO_WRITE</th>
      <th />
    </tr>
    <tr>
      <td colspan="4">
        <div class="permissionHelp">
          These flags regulate access to user personal information stored in VOMS Admin.
        </div>  
      </td>
    </tr>
    <tr class="tableRowOdd clickable">
      <th id="suspendHandle" class="permHeader  clickable">User suspension rights:</th>
      <th class="permissionName"><s:checkbox name="selectedPermissions"
        fieldValue="%{'SUSPEND'}" cssClass="permissionCheckbox"
        value="%{permission.has('SUSPEND')}" theme="simple"/>
      SUSPEND</th>
      <th colspan="2" />
    </tr>
    <tr class="tableRowOdd borderBottom">
      <td colspan="4">
        <div class="permissionHelp">
          This flag state regulate access to user suspension opearation.
        </div>
      </td>
    </tr>
    
    