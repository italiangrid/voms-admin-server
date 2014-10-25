/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.taglib;

import java.util.Collections;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.glite.security.voms.admin.error.VOMSAuthorizationException;
import org.glite.security.voms.admin.operations.users.FindUnsubscribedGroupsOperation;

public class UnsubscribedGroupsTag extends javax.servlet.jsp.tagext.TagSupport {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  String userId;

  String var;

  public int doStartTag() throws JspException {

    // FIXME: add some exception handling here!
    List groups;

    try {

      groups = (List) FindUnsubscribedGroupsOperation
        .instance(new Long(userId)).execute();

    } catch (VOMSAuthorizationException e) {

      groups = Collections.EMPTY_LIST;
    }

    pageContext.setAttribute(var, groups);

    return SKIP_BODY;
  }

  public String getUserId() {

    return userId;
  }

  public void setUserId(String userId) {

    this.userId = userId;
  }

  public String getVar() {

    return var;
  }

  public void setVar(String var) {

    this.var = var;
  }

}
