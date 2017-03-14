/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.core.VOMSServiceConstants;

public class UnassignedRolesTag extends javax.servlet.jsp.tagext.TagSupport {

  public static final Logger log = LoggerFactory
    .getLogger(UnassignedRolesTag.class);

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  String id;

  String groupId;

  String var;

  public int doStartTag() throws JspException {

    List roles = null;

    Map unassignedRoles = (Map) pageContext
      .findAttribute(VOMSServiceConstants.UNASSIGNED_ROLES_KEY);

    if (unassignedRoles == null)
      throw new JspTagException(
        "Unassigned roles mappings not found in page context!");

    roles = (List) unassignedRoles.get(new Long(groupId));
    pageContext.setAttribute(var, roles);

    return SKIP_BODY;
  }

  public String getVar() {

    return var;
  }

  public void setVar(String var) {

    this.var = var;
  }

  public String getGroupId() {

    return groupId;
  }

  public void setGroupId(String groupId) {

    this.groupId = groupId;
  }

}
