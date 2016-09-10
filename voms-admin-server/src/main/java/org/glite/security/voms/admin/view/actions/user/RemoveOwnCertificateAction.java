package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.users.RemoveOwnCertificateOperation;
import org.glite.security.voms.admin.persistence.dao.CertificateDAO;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "home",
    type = "redirectAction"),
  @Result(name = BaseAction.INPUT, location = "userHome") })
public class RemoveOwnCertificateAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Long certificateId;

  public Long getCertificateId() {

    return certificateId;
  }

  public void setCertificateId(Long certificateId) {

    this.certificateId = certificateId;
  }

  @Override
  public String execute() throws Exception {

    Certificate cert = CertificateDAO.instance()
      .findById(getCertificateId());

    RemoveOwnCertificateOperation op = new RemoveOwnCertificateOperation(cert);
    op.execute();

    return SUCCESS;

  }
}
