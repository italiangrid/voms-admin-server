package org.glite.security.voms.admin.view.actions.group;

import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.RequestAware;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;

@Result(name = BaseAction.SUCCESS, location = "createGroup.jsp")
public class NewGroupAction extends BaseAction implements Preparable,
  RequestAware {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Map<String, Object> request;

  @Override
  public String execute() throws Exception {

    return BaseAction.SUCCESS;
  }

  @Override
  public void prepare() throws Exception {

    List<VOMSGroup> voGroups = VOMSGroupDAO.instance().findAll();
    request.put("voGroups", voGroups);
  }

  @Override
  public void setRequest(Map<String, Object> request) {

    this.request = request;

  }
}
