<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:form id="confirmUserDeleteForm" action="delete" namespace="/user"
  theme="bootstrap" cssClass="form-horizontal">
  
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal"
      aria-label="Close">
      <span aria-hidden="true">&times;</span>
    </button>
    <h4 class="modal-title" id="suspendUserLabel">Delete user?</h4>
  </div>
  
  <div class="modal-body">
    <s:token/>
    <s:hidden name="userId" value="%{id}"/>
    <p>
      Are you sure you want to delete following user: <br/>
      <em><s:property value="shortName"/></em>
      <br/>
    </p>
    <p>
      Once deleted, the user will not be recognized as a member of the VO
      anymore and thus will not be able to obtain VOMS credentials for this VO.
    </p>
  </div>
  
  <div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
    <sj:submit cssClass="btn btn-danger" 
      formIds="confirmUserDeleteForm" 
      value="%{'Delete user'}" 
      validate="true" validateFunction="bootstrapValidation"/>
  </div>
</s:form>