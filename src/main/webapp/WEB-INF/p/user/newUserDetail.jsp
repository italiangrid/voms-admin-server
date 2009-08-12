<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1 class="username">
  <s:property value="fullName"/> 
  <span class="institution">
    <s:property value="institution"/>  
  </span>
</h1>

<%-- 
<dl>
    <dt>Member since:</dt><dd><s:property value="creationTime"/></dd>
    <dt>Expiration time:</dt><dd><s:property value="endTime"/></dd>
</dl>
--%>

<div class="info-tab">
<h2><span>Personal information</span></h2>
<div class="content">
      <s:form>
        <s:textfield name="name" disabled="true" label="Name" size="40" cssClass="text"/>
        <s:textfield name="surname" disabled="true" label="Surname" size="40" cssClass="text"/>
        <s:textfield name="institution" disabled="true" label="Institution" size="40" cssClass="text"/>
        <s:textarea name="address" disabled="true" label="Address" rows="4" cols="30"   cssClass="text"/>
        <s:textfield name="phoneNumber" disabled="true" label="Phone" size="40"   cssClass="text"/>
        <s:textfield name="emailAddress" disabled="true" label="Email" size="40"   cssClass="text"/>
        <s:submit value="%{'Change personal information'}" disabled="true"/>
      </s:form>
 </div>
</div>

<div class="info-tab">
<h2><span>Certificates</span></h2>
<div class="content">

      
      <voms:authorized permission="CONTAINER_READ|CONTAINER_WRITE" context="vo">
        <div id="add-certificate-link">
          <s:url action="add-certificate" namespace="/user" var="addCertificateURL">
            <s:param name="userId" value="id"/>
          </s:url>
    
          <s:a href="%{#addCertificateURL}" cssClass="actionLink  ">Add a new certificate</s:a>
       </div>
      </voms:authorized>
  

      <s:if test="certificates.empty">
        No certificates defined for this user.
      </s:if>
      <s:else>
        <voms:hasPermissions var="canSuspend" 
                    context="/${voName}" 
                    permission="CONTAINER_READ|MEMBERSHIP_READ|SUSPEND"/>
                    
        <table cellpadding="0" cellspacing="0">
          
          <s:iterator var="cert" value="certificates">
            <tr class="tableRow">
              <td >
                <div class="userDN">
                  <s:property value="subjectString"/>
                </div>
                
                <div class="userCA">
                  <s:property value="ca.subjectString"/>
                </div>
                
                <div class="cert-date-info">
                  Added on: <span><s:property value="creationTime"/></span>
                </div>
                
                <div class="cert-status-info">
                  <s:if test="suspended">
                    <span>
                      Suspended:
                    </span>
              
                    <span class="suspensionReason">
                      <s:property value="suspensionReason"/>
                    </span>             
                  </s:if>
                </div>
                
                <div class="cert-operations">
                  <s:if test="#attr.canSuspend and not suspended">
                    <%--
                    <s:url action="suspend-certificate" method="input" namespace="/user" var="suspendCertURL">
                      <s:param name="userId" value="%{model.id}"/>
                      <s:param name="certificateId" value="%{#cert.id}"/>
                    </s:url>
              
                    <s:a href="%{suspendCertURL}" cssClass="actionLink">
                      suspend
                    </s:a>
                     --%>
                    <s:form action="suspend-certificate" namespace="/user" theme="simple" cssClass="cert-operation-forms">
                      <s:token/>
                      <s:hidden name="userId" value="%{model.id}"/>
                      <s:hidden name="certificateId" value="%{#cert.id}"/>
                      <s:submit value="%{'Suspend'}"/>
                    </s:form>
                  </s:if>
              
                  <s:if test="#attr.canSuspend and suspended">
                      
                     <s:if test="not user.suspended">
                        <s:form action="restore-certificate" namespace="/user" theme="simple" cssClass="cert-operation-forms">
                          <s:token/>  
                          <s:hidden name="userId" value="%{model.id}"/>
                          <s:hidden name="certificateId" value="%{#cert.id}"/>
                          <s:submit value="%{'Restore'}"/>
                        </s:form>
                     </s:if>
                     
                </s:if>
                
                <s:if test="model.certificates.size > 1">
                  <s:form action="delete-certificate" namespace="/user" theme="simple" cssClass="cert-operation-forms">
                    <s:token/>
                    <s:hidden name="userId" value="%{model.id}"/>
                    <s:hidden name="certificateId" value="%{#cert.id}"/>
                    <s:submit value="%{'Delete'}"/>
                  </s:form>
                </s:if>
                </div>
                
              </td>
           </tr>
           
           
           <%--
              
              
              <td>
                <s:property value="creationTime"/>
              </td>
              <td>
              
                    
                <s:if test="suspended">
                  Suspended:
                  <div class="suspensionReason">
                    <s:property value="suspensionReason"/>
                  </div>             
                </s:if>
                <s:else>
                  active
                  
                </s:else>
             </td>
             
             <td>
                  <s:if test="#attr.canSuspend and not suspended">
                    <s:url action="suspend-certificate" method="input" namespace="/user" var="suspendCertURL">
                      <s:param name="userId" value="%{model.id}"/>
                      <s:param name="certificateId" value="%{#cert.id}"/>
                    </s:url>
              
                    <s:a href="%{suspendCertURL}" cssClass="actionLink">
                      suspend
                    </s:a>
                  </s:if>
              
                  <s:if test="#attr.canSuspend and suspended">
                    <s:url action="restore-certificate" namespace="/user" var="restoreCertURL">
                      <s:param name="userId" value="%{model.id}"/>
                      <s:param name="certificateId" value="%{#cert.id}"/>
                    </s:url>
              
                    <s:a href="%{restoreCertURL}" cssClass="actionLink">
                      restore
                    </s:a>
                </s:if>
                
                <s:if test="model.certificates.size > 1">
                  <s:form action="delete-certificate" namespace="/user" theme="simple">
                    <s:token/>
                    <s:hidden name="userId" value="%{model.id}"/>
                    <s:hidden name="certificateId" value="%{#cert.id}"/>
                    <s:submit value="%{'Delete'}"/>
                  </s:form>
                </s:if>
              </td>
            </tr>
             --%>
          </s:iterator>
        </table>
      </s:else>
</div>
</div>

<div class="info-tab">
  <h2><span>Groups and Roles</span></h2>
  <div class="content">
      <s:set var="userId" scope="page" value="id"/>
      <voms:unsubscribedGroups var="unsubscribedGroups" userId="${userId}"/>
      <voms:unassignedRoleMap var="unassignedRoleMap" userId="${userId}"/>

      <s:if test="not #attr.unsubscribedGroups.empty">
        
        <div class="subscribeGroups">
          <s:form action="add-to-group" namespace="/user" theme="simple">
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
            <tr class="tableRow">
              
              <td>
                <table cellpadding="0" cellspacing="0" class="noborder">
                  <tr>
                    <td>
                      <div class="groupName">
                        <s:property value="key"/>
                      </div>
                    </td>
                  </tr>
                  <s:if test="not key.rootGroup">
                    <tr>
                      <td>
                        <s:form action="remove-from-group" namespace="/user" theme="simple">
                          <s:hidden name="userId" value="%{model.id}"/>
                          <s:hidden name="groupId" value="%{#mapping.key.id}"/>
                          <s:submit value="%{'Remove from this group'}"/>
                        </s:form>
                      </td>
                    </tr>
                  </s:if>  
                </table>
              </td>
              
              <td>
                <table class="noborder" cellpadding="0" cellspacing="0">
                <s:iterator var="role" value="value">
                  
                    <tr>
                      <td class="userRoleName">
                        <s:property value="name"/>
                      </td>
                      <td class="dismissRoleBox">
                        <s:form action="dismiss-role" namespace="/user" theme="simple">
                          <s:token/>
                          <s:hidden name="userId" value="%{model.id}"/>
                          <s:hidden name="groupId" value="%{#mapping.key.id}"/>
                          <s:hidden name="roleId" value="%{#role.id}"/>
                          <s:submit value="%{'Dismiss role'}"/>
                        </s:form>
                      </td>
                    </tr>
                </s:iterator>
               
                <s:if test="%{not #attr.unassignedRoleMap[#mapping.key.id].isEmpty}">
                  <tr>
                    <td/>
                    <td class="roleAssign">
                    
                      <s:form action="assign-role" namespace="/user" theme="simple">
                        <s:token/>
                        <s:hidden name="userId" value="%{model.id}"/>
                        <s:hidden name="groupId" value="%{#mapping.key.id}"/>
                        <s:select list="#attr.unassignedRoleMap[#mapping.key.id]" listKey="id" listValue="name" name="roleId"/>
                        <s:submit value="%{'Assign role'}" cssClass="assignRoleButton"/> 
                      </s:form>
                    
                  </td>
                  </tr>
                </s:if>
                </table>
              </td>
            </tr>
          </s:iterator>
        </table>
      </div>
    </div>
</div>


<div class="info-tab">
  <h2><span>Generic attributes</span></h2>
  <div class="content">

  <voms:authorized permission="ATTRIBUTES_WRITE" context="vo">
  
    <s:if test="not #request.attributeClasses.empty">
      <div class="attributeCreationTab">
        <s:form action="set-attribute" namespace="/user">
          <s:token/>
          <s:hidden name="userId" value="%{model.id}"/>
          
          <table cellpadding="" cellspacing="" class="noborder">
            <tr>
              <td>
                <s:select name="attributeName" 
                  list="#request.attributeClasses" 
                  listKey="name" 
                  listValue="name" 
                  label="Attribute name"/>
              </td>
              </tr>
              
              <tr>
                <td>
                  <s:textarea label="Attribute value" name="attributeValue" rows="4" cols="30" value=""/>
                </td>
              </tr>
              <tr>
                <td>
                  <s:submit value="%{'Set attribute'}"/>
                </td>
              </tr>
          </table>       
        </s:form>
      </div>
    </s:if>
    <s:else>
      No attribute classes defined for this vo.
    </s:else>
  </voms:authorized>
    
  
  <voms:authorized permission="ATTRIBUTES_READ" context="vo">
    <s:if test="attributes.isEmpty">
        <s:if test="not #request.attributeClasses.empty">
          No attributes defined for this user.
        </s:if>
    </s:if>
    <s:else>
      
      <table class="table" cellpadding="0" cellspacing="0">
            
            <tr>
              <th>Attribute name</th>
              <th>Attribute value</th>
              <th colspan="2"/>
            </tr>
            
            <s:iterator var="attribute" value="attributes">
              
              <tr class="tableRow">  
                <td><s:property value="name"/></td>
                <td><s:property value="value"/></td>
                <td>
                  <voms:authorized permission="ATTRIBUTES_WRITE" context="vo">
                    <s:form action="delete-attribute" namespace="/user">
                      <s:token/>
                      <s:hidden name="userId" value="%{model.id}"/>
                      <s:hidden name="attributeName" value="%{#attribute.name}"/>
                      <s:submit value="%{'delete'}"/>
                    </s:form>
                  </voms:authorized>
                </td>
              </tr>
            </s:iterator>
      </table>
    </s:else>
  </voms:authorized>
  </div>
</div>

