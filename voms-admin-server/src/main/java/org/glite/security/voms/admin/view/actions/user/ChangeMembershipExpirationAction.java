package org.glite.security.voms.admin.view.actions.user;

import java.util.Date;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.core.validation.ValidationUtil;

@Results({ @Result(name = UserActionSupport.SUCCESS,
  location = "changeMembershipExpiration") })
public class ChangeMembershipExpirationAction extends UserActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public Date getProposedExpirationDate() {

    return ValidationUtil.membershipExpirationDateStartingFromNow();
  }

}
