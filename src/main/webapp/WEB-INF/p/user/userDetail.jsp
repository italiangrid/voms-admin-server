<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h2>User detail</h2>

<h3>Personal information</h3>

<s:form>
  
  <s:textfield name="name" value="%{model.name}" disabled="true" label="Name" size="30"/>
  <s:textfield name="surname" value="%{model.surname}" disabled="true" label="Surname" size="30"/>
  <s:textfield name="institution" value="%{model.institution}" disabled="true" label="Institution" size="30"/>
  <s:textarea name="address" value="%{model.address}" disabled="true" label="Address" rows="4" cols="30"/>
  <s:textfield name="phoneNumber" value="%{model.phoneNumber}" disabled="true" label="Phone" size="30"/>
  <s:textfield name="emailAddress" value="%{model.emailAddress}" disabled="true" label="Email" size="30"/>
  <s:submit value="%{'Change personal information'}" disabled="true"/>
  
</s:form>

<h3>Certificate information</h3>

<div class="certTab">
<s:if test="model.certificates.empty">
  No certificates defined for this user.
</s:if>
<s:else>
<table cellpadding="0" cellspacing="0">
  <tr>
    <th>Subject</th>
    <th>Issuer</th>
    <th>Added on</th>
    <th>Status</th>
  </tr>
  <s:iterator var="cert" value="%{model.certificates}">
    <tr>
      <td class="dn">
        <voms:formatDN dn="${cert.subjectString}" fields="CN"/>
      </td>
      <td class="dn ca">
          <voms:formatDN dn="${cert.ca.subjectString}" fields="CN"/>
      </td>
      <td>
        <s:property value="creationTime"/>
      </td>
      <td>
        <s:if test="suspended">
          Suspended! Reason:
          <div class="suspensionReason">
            <s:property value="suspensionReason"/>
          </div>
        </s:if>
        <s:else>
          ok
        </s:else>
      </td>
    </tr>
  </s:iterator>
</table>
</s:else>
</div>

<h3>Membership information</h3>

<s:set var="userId" scope="page" value="%{model.id}"/>

<voms:unsubscribedGroups var="unsubscribedGroups" userId="${userId}"/>
<voms:unassignedRoleMap var="unassignedRoleMap" userId="${userId}"/>

<s:if test="not #attr.unsubscribedGroups.empty">
  <div class="subscribeGroups">
    <s:form action="add-to-group" namespace="/user">
      <s:token/>
      <s:hidden name="userId" value="%{model.id}"/>
      <s:select list="#attr.unsubscribedGroups" listKey="id" listValue="name" name="groupId"/>
      <s:submit value="%{'Add to group'}"/>
    </s:form>
  </div>
</s:if>


<div class="membershipTab">
<table cellpadding="0" cellspacing="0">
  <tr>
    <th>Group name</th>
    <th>Roles</th>
  </tr>
  <s:iterator var="mapping" value="model.mappingsMap">
    <tr>
      <td>
        <s:property value="key"/>   
      </td>
      <td>
        <s:iterator var="role" value="value">
          <div class="userRoleName">
            <s:property value="name"/>
          </div>
          <div class="dismissRoleBox">
            
            <s:form action="dismiss-role" namespace="/user">
              <s:token/>
              <s:hidden name="userId" value="%{model.id}"/>
              <s:hidden name="groupId" value="%{#mapping.key.id}"/>
              <s:hidden name="roleId" value="%{#role.id}"/>
              <s:submit value="%{'Dismiss role'}"/>
            </s:form>
            
          </div>
        </s:iterator>
        
        <s:if test="%{not #attr.unassignedRoleMap[#mapping.key.id].isEmpty}">
          
          <div class="roleAssign">
            <s:form action="assign-role" namespace="/user">
              <s:token/>
              <s:hidden name="userId" value="%{model.id}"/>
              <s:hidden name="groupId" value="%{#mapping.key.id}"/>
              <s:select list="#attr.unassignedRoleMap[#mapping.key.id]" listKey="id" listValue="name" name="roleId"/>
              <s:submit value="%{'Assign role'}"/> 
            </s:form>
          </div>
        </s:if>
      </td>
      <td>
        <s:if test="%{not key.rootGroup}">
          <s:form action="remove-from-group" namespace="/user">
            <s:hidden name="userId" value="%{model.id}"/>
            <s:hidden name="groupId" value="%{#mapping.key.id}"/>
            <s:submit value="%{'Remove from group'}"/>
          </s:form>
        </s:if>
      </td>
    </tr>
  </s:iterator>
</table>
</div>

