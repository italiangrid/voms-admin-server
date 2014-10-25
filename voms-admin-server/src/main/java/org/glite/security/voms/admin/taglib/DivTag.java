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

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.glite.security.voms.admin.core.VOMSServiceConstants;

public class DivTag extends TagSupport {

  String id;

  String cssClass;

  public String getId() {

    return id;
  }

  public void setId(String id) {

    this.id = id;
  }

  protected boolean isVisible() {

    Map statusMap = (Map) pageContext
      .findAttribute(VOMSServiceConstants.STATUS_MAP_KEY);

    if (statusMap == null)
      return true;

    Boolean status = (Boolean) statusMap.get(getId());

    if (status == null)
      return true;

    return status;
  }

  public String getCssClass() {

    return cssClass;
  }

  public void setCssClass(String cssClass) {

    this.cssClass = cssClass;
  }

  @Override
  public int doStartTag() throws JspException {

    String styleContent = "";

    if (!isVisible())
      styleContent = "display:none";

    String startTag = String.format(
      "<div id=\"%s\" class=\"%s\" style=\"%s\">", getId(), getCssClass(),
      styleContent);

    try {
      pageContext.getOut().write(startTag);

    } catch (IOException e) {
      throw new JspTagException("Error writing to jsp writer!", e);
    }

    return EVAL_BODY_INCLUDE;
  }

  @Override
  public int doEndTag() throws JspException {

    try {
      pageContext.getOut().write("\n</div><!-- " + getId() + "-->");

    } catch (IOException e) {
      throw new JspTagException("Error writing to jsp writer!", e);
    }

    return EVAL_PAGE;
  }

}
