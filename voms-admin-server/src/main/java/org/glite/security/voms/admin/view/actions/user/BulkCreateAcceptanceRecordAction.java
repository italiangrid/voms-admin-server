package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.operations.SingleArgumentOperationCollection;
import org.glite.security.voms.admin.operations.aup.CreateAcceptanceRecordOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({

@Result(name = BaseAction.SUCCESS, location = "search", type = "chain"),
  @Result(name = BaseAction.INPUT, location = "users"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE, location = "users")

})
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class BulkCreateAcceptanceRecordAction extends UserBulkActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  @Override
  public String execute() throws Exception {
  
    SingleArgumentOperationCollection<Long>
      op = new SingleArgumentOperationCollection<Long>(userIds, 
        CreateAcceptanceRecordOperation.class);
    
    op.execute();
    
    return SUCCESS;
  }

}
