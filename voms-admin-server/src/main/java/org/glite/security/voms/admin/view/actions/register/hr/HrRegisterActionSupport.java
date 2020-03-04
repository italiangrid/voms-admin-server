package org.glite.security.voms.admin.view.actions.register.hr;

import static java.util.Objects.isNull;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;
import org.glite.security.voms.admin.view.actions.register.RegisterActionSupport;

public class HrRegisterActionSupport extends RegisterActionSupport implements SessionAware {

  private static final long serialVersionUID = 1L;

  private Map<String, Object> session;

  @Override
  public void prepare() throws Exception {
    super.prepare();
    requester = (RequesterInfo) session.get(ResolveHrIdAction.REQUESTER_INFO_KEY);
  }

  @Override
  public void validate() {
    if (isNull(requester)) {
      addActionError("Application information not found in session!");
    }
  }
  @Override
  public void setSession(Map<String, Object> session) {
    this.session = session;
  }

}
