<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:form id="confirmCertificateSuspensionForm" action="suspend-certificate" namespace="/user"
  theme="bootstrap" cssClass="form-horizontal">
  
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal"
      aria-label="Close">
      <span aria-hidden="true">&times;</span>
    </button>
    <h4 class="modal-title" id="suspendUserLabel">Suspend user certificate?</h4>
  </div>
  
  <div class="modal-body">
    <s:token/>
    <s:hidden name="userId" value="%{id}"/>
    <p>
      You are about to suspend the following user certificate: <br/>
      <em><s:property value="subjectString"/></em>
      <br/>
      Once suspended, the user will not be able to obtain VOMS
      credentials for this VO authenticating with the above certificate.
    </p>
    <s:textfield name="suspensionReason" 
      label="Reason" size="40"
      value="%{suspensionReason}" 
      placeholder="Insert the reason why the certificate is being suspended here..." />
  </div>
  
  <div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
    <sj:submit cssClass="btn btn-warning" 
      formIds="confirmCertificateSuspensionForm" 
      value="%{'Suspend certificate'}" 
      validate="true" validateFunction="bootstrapValidation"/>
  </div>
</s:form>