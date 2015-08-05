package org.glite.security.voms.admin.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.error.NotFoundException;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.operations.VOMSContext;

public class HasGroupManagerRoleTag extends TagSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  String group;
  String var;

  final String groupManagerRoleName;

  public HasGroupManagerRoleTag() {

    groupManagerRoleName = VOMSConfiguration.instance()
      .getGroupManagerRoleName();
  }

  @Override
  public int doStartTag() throws JspException {

    VOMSContext ctxt;

    try {

      ctxt = TagUtils.buildContext(group + "/Role=" + groupManagerRoleName);

    } catch (NotFoundException e) {
      pageContext.setAttribute(var, Boolean.FALSE);
      return SKIP_BODY;
    }

    boolean hasRole = CurrentAdmin.instance().hasRole(ctxt.getGroup(),
      ctxt.getRole());
    pageContext.setAttribute(var, new Boolean(hasRole));
    return SKIP_BODY;
  }

  @Override
  public int doEndTag() throws JspException {

    return EVAL_PAGE;
  }

  public String getGroup() {

    return group;
  }

  public void setGroup(String group) {

    this.group = group;
  }

  public String getVar() {

    return var;
  }

  public void setVar(String var) {

    this.var = var;
  }

}
