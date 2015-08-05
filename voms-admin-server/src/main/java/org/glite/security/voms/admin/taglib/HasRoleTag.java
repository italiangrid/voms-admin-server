package org.glite.security.voms.admin.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.glite.security.voms.admin.error.NotFoundException;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.operations.VOMSContext;

public class HasRoleTag extends TagSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  String role;
  String var;

  @Override
  public int doStartTag() throws JspException {

    VOMSContext ctxt;
    try {

      ctxt = TagUtils.buildContext(role);

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

  public String getRole() {

    return role;
  }

  public void setRole(String role) {

    this.role = role;
  }

  public String getVar() {

    return var;
  }

  public void setVar(String var) {

    this.var = var;
  }

}
