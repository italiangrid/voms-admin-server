package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.persistence.dao.VOMSCADAO;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;

@Result(name = BaseAction.SUCCESS, location = "createUser.jsp")
public class NewUserAction extends BaseAction implements Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public void prepare() throws Exception {

    getRequest().put("trustedCas", VOMSCADAO.instance().getValid());

  }

}
