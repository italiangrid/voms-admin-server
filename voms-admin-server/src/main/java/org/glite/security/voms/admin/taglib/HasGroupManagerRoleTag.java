/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
