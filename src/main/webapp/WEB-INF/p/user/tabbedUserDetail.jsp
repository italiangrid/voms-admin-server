<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1 class="username">
  <s:property value="fullName"/> 
  <span class="institution">
    <s:property value="institution"/>  
  </span>
</h1>

<div id="tabs" style="clear: both">
  <ul>
    <li><a href="#pers-info">Personal information</a></li>
    <li><a href="#cert-info">Certificates</a></li>
  </ul>
  <div id="pers-info">
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
  <div id="cert-info">
    
      
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
           
           
          </s:iterator>
        </table>
      </s:else>
  </div>
</div>
