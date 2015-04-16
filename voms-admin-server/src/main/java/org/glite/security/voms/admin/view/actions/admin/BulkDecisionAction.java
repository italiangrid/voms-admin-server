package org.glite.security.voms.admin.view.actions.admin;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.operations.VOMSOperation;
import org.glite.security.voms.admin.operations.requests.BaseHandleRequestOperation;
import org.glite.security.voms.admin.operations.requests.DECISION;
import org.glite.security.voms.admin.operations.requests.HandleCertificateRequestOperation;
import org.glite.security.voms.admin.operations.requests.HandleGroupRequestOperation;
import org.glite.security.voms.admin.operations.requests.HandleMembershipRemovalRequest;
import org.glite.security.voms.admin.operations.requests.HandleRoleMembershipRequestOperation;
import org.glite.security.voms.admin.operations.requests.HandleVOMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.CertificateRequest;
import org.glite.security.voms.admin.persistence.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.persistence.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Results({

  @Result(name = BaseAction.SUCCESS, location = "adminHome"),
  @Result(name = BaseAction.INPUT, location = "adminHome"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE,
  location = "adminHome")
  
})

@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute"})
public class BulkDecisionAction extends BulkRequestActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LoggerFactory.getLogger(BulkDecisionAction.class);

  String decision;
  
  @Override
  public void validate() {
 
    super.validate();
    if (decision == null){
      addActionError("Please provide a decision!");
    }
    
  }

  public BaseHandleRequestOperation<?> resolveRequestHandler(Request request,
    DECISION decision) {
    
    if (request instanceof NewVOMembershipRequest){
      return new HandleVOMembershipRequest((NewVOMembershipRequest)request, 
        decision, null);
    }

    if (request instanceof GroupMembershipRequest) {
      return new HandleGroupRequestOperation((GroupMembershipRequest) request,
        decision);
    }

    if (request instanceof RoleMembershipRequest) {
      return new HandleRoleMembershipRequestOperation(
        (RoleMembershipRequest) request, decision);
    }

    if (request instanceof MembershipRemovalRequest) {
      return new HandleMembershipRemovalRequest(
        (MembershipRemovalRequest) request, decision);
    }

    if (request instanceof CertificateRequest) {
      return new HandleCertificateRequestOperation(
        (CertificateRequest) request, decision);
    }

    return null;
  }
  
  @Override
  public String execute() throws Exception {

    DECISION theDecision = DECISION.valueOf(decision.toUpperCase());

    int numFailures = 0;
    
    for (Request r : selectedRequests) {
      VOMSOperation op = resolveRequestHandler(r, theDecision);
      if (op != null) {
        try{
          op.execute();
        }catch(Throwable t){
          numFailures++;
          String errorMessage = String.format("Error handling '%s (%d)': %s", 
            r.getTypeName(),
            r.getId(),
            t.getMessage());
          
          addActionError(errorMessage);
          LOGGER.error(t.getMessage(),t);
          
        }
      }
    }
    
    if (numFailures == 0){
      if (selectedRequests.size() > 1){
        addActionMessage(String.format("%d requests %s.", 
          selectedRequests.size(), decisionToVerb(theDecision)));
      }else {
        addActionMessage(String.format("Request %s.", 
          decisionToVerb(theDecision)));
      }
    } else {
      
      if (selectedRequests.size() - numFailures > 0){
        addActionMessage(String.format("%d (out of %d) requests %s succesfully.", 
          selectedRequests.size() - numFailures, selectedRequests.size(), 
          decisionToVerb(theDecision)));
      }
    }
    
    refreshManageableRequests();
    
    if (numFailures == 0){
      return SUCCESS;
    }
    
    return INPUT;
  }
  
  private String decisionToVerb(DECISION d){
    if (d.equals(DECISION.APPROVE)){
      return "approved";
    }
    return "rejected";
  }
  public String getDecision() {

    return decision;
  }

  public void setDecision(String decision) {

    this.decision = decision;
  }

}
