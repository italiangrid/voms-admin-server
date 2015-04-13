<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:form id="confirmUserSuspensionForm" action="suspend" namespace="/user"
  theme="bootstrap" cssClass="form-horizontal">
  
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal"
      aria-label="Close">
      <span aria-hidden="true">&times;</span>
    </button>
    <h4 class="modal-title" id="suspendUserLabel">Suspend user?</h4>
  </div>
  
  <div class="modal-body">
    <s:token/>
    <s:hidden name="userId" value="%{id}"/>
    <p>
      You are about to suspend the following user: <br/>
      <em><s:property value="shortName"/></em>
      <br/>
      Once suspended the user will not be able to obtain VOMS
      credentials for this VO.
    </p>
    <s:textfield name="suspensionReason" 
      label="Reason" size="40"
      value="%{suspensionReason}" 
      placeholder="Insert the reason why the user is being suspended here..." />
  </div>
  
  <div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
    <sj:submit cssClass="btn btn-warning" 
      formIds="confirmUserSuspensionForm" 
      value="%{'Suspend user'}" 
      validate="true" validateFunction="bootstrapValidation"/>
  </div>
</s:form>