    <%@include file="/WEB-INF/p/shared/taglibs.jsp"%>
    <tr>
      <td>All permissions: <s:checkbox name="selectedPermissions"
        id="allPermsCheckbox" fieldValue="%{'ALL'}" /></td>
    </tr>

    <tr>
      <td />
      <td>Read</td>
      <td>Write</td>
      <td />
    </tr>

    <tr>
      <td>Container rights:</td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'CONTAINER_READ'}" cssClass="permissionCheckbox"
        value="%{permission.has('CONTAINER_READ')}" /></td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'CONTAINER_WRITE'}" cssClass="permissionCheckbox"
        value="%{permission.has('CONTAINER_WRITE')}" /></td>
    </tr>

    <tr>
      <td />
      <td>Read</td>
      <td>Write</td>
      <td />
    </tr>
    <tr>
      <td>Membership rights:</td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'MEMBERSHIP_READ'}" cssClass="permissionCheckbox"
        value="%{permission.has('MEMBERSHIP_READ')}" /></td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'MEMBERSHIP_WRITE'}" cssClass="permissionCheckbox"
        value="%{permission.has('MEMBERSHIP_WRITE')}" /></td>
    </tr>
    <tr>
      <td />
      <td>List</td>
      <td>Set</td>
      <td>Default</td>
    </tr>
    <tr>
      <td>ACL management rights:</td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'ACL_READ'}" cssClass="permissionCheckbox"
        value="%{permission.has('ACL_READ')}" /></td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'ACL_WRITE'}" cssClass="permissionCheckbox"
        value="%{permission.has('ACL_WRITE')}" /></td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'ACL_DEFAULT'}" cssClass="permissionCheckbox"
        value="%{permission.has('ACL_DEFAULT')}" /></td>
    </tr>
    <tr>
      <td />
      <td>List</td>
      <td>Define</td>
      <td />
    </tr>
    <tr>
      <td>Generic Attributes rights:</td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'ATTRIBUTES_READ'}" cssClass="permissionCheckbox"
        value="%{permission.has('ATTRIBUTES_READ')}" /></td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'ATTRIBUTES_WRITE'}" cssClass="permissionCheckbox"
        value="%{permission.has('ATTRIBUTES_WRITE')}" /></td>
    </tr>
    <tr>
      <td />
      <td>List</td>
      <td>Manage</td>
      <td />
    </tr>
    <tr>
      <td>User request management rights:</td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'REQUESTS_READ'}" cssClass="permissionCheckbox"
        value="%{permission.has('REQUESTS_READ')}" /></td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'REQUESTS_WRITE'}" cssClass="permissionCheckbox"
        value="%{permission.has('REQUESTS_WRITE')}" /></td>
    </tr>
    <tr>
      <td />
      <td>Read</td>
      <td>Write</td>
      <td />
    </tr>
    <tr>
      <td>User personal information access rights:</td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'PERSONAL_INFO_READ'}" cssClass="permissionCheckbox"
        value="%{permission.has('PERSONAL_INFO_READ')}" /></td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'PERSONAL_INFO_WRITE'}" cssClass="permissionCheckbox"
        value="%{permission.has('PERSONAL_INFO_WRITE')}" /></td>
    </tr>
    <tr>
      <td />
      <td>Suspend</td>
      <td colspan="2" />
    </tr>
    <tr>
      <td>User suspension rights:</td>
      <td><s:checkbox name="selectedPermissions"
        fieldValue="%{'SUSPEND'}" cssClass="permissionCheckbox"
        value="%{permission.has('SUSPEND')}" /></td>
    </tr>