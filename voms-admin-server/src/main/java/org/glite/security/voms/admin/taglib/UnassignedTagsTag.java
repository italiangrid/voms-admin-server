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

public class UnassignedTagsTag extends TagSupport {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  String adminId;
  String var;

  public String getAdminId() {

    return adminId;
  }

  public void setAdminId(String adminId) {

    this.adminId = adminId;
  }

  public String getVar() {

    return var;
  }

  public void setVar(String var) {

    this.var = var;
  }

  @Override
  public int doStartTag() throws JspException {

    // FIXME: DO this with an operation
    // List<VOMSAdmin> unassignedTags =
    // VOMSAdminDAO.instance().getUnassignedTagsForAdmin( new Long(adminId)
    // );
    // pageContext.setAttribute( var, unassignedTags);
    //
    return SKIP_BODY;
  }

}
