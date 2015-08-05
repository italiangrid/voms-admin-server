package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBDAOFactory;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBVOMSPersonDAO;
import org.glite.security.voms.admin.operations.users.SetUserOrgDbIdOperation;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

@Results({
  @Result(name = UserActionSupport.INPUT, location = "userChangeOrgDbId"),
  @Result(name = UserActionSupport.SUCCESS, location = "load",
    type = "chain") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class SaveOrgdbIdAction extends UserActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  Long theOrgDbId;

  
  @Override
  public void validate() {
    
    if (theOrgDbId == null){
      // The error will be set by the field validator below 
      return;
    }
    
    OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance().getVOMSPersonDAO();
    
    if (dao.findById(theOrgDbId, false) == null){
      addFieldError("theOrgDbId", "No HR record found for the given ID: "
        +theOrgDbId);
    }
    
  }
  
  @RequiredFieldValidator(message="Please provide a value for the CERN HR ID!")
  public Long getTheOrgDbId() {

    return theOrgDbId;
  }

  public void setTheOrgDbId(Long theOrgDbId) {

    this.theOrgDbId = theOrgDbId;
  }

  @Override
  public String execute() throws Exception {
  
    SetUserOrgDbIdOperation op = new SetUserOrgDbIdOperation(getModel(), 
      theOrgDbId);
    
    op.execute();
    
    return SUCCESS;
  }
}
