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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;

public class PageCustomizationTag extends TagSupport {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  String pageName;
  String styleClass;

  public int doStartTag() throws JspException {

    assert pageName != null : "pageName cannot be null!";

    if (styleClass == null)
      styleClass = "";

    VOMSConfiguration conf = VOMSConfiguration.instance();

    if (conf.pageHasCustomization(pageName)) {

      String customizationFilePath = conf
        .getCustomizationPageAbsolutePath(pageName);

      try {

        String header = "<div class='"
          + styleClass
          + "'><div class='customized-content-title'>VO customized content</div>\n";
        pageContext.getOut().write(header);

        BufferedReader reader = new BufferedReader(new FileReader(
          customizationFilePath));
        String line = null;
        while ((line = reader.readLine()) != null) {

          pageContext.getOut().write(line + "\n");

        }

        pageContext.getOut().write("</div>");

      } catch (FileNotFoundException e) {
        throw new JspException("Customization file not found: "
          + e.getMessage());

      } catch (IOException e) {
        throw new JspException("Error writing customization file content!");
      }

      return SKIP_BODY;

    }

    return SKIP_BODY;
  }

  public String getPageName() {

    return pageName;
  }

  public void setPageName(String pageName) {

    this.pageName = pageName;
  }

  public String getStyleClass() {

    return styleClass;
  }

  public void setStyleClass(String styleClass) {

    this.styleClass = styleClass;
  }

}
