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

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;

public class FormatDNTag extends TagSupport {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  String fields;

  String dn;

  /**
   * FIXME: Should allow for more characters inside the fields, to be standard
   * compliant
   **/
  private static final String regexTemplate = "=((?:(?:\\/(?!DN|DC|STREET|O|OU|CN|C|L|E|Email|emailAddress|UID|uid))?\\w?:?;?'?\"?`?\\s?\\.?@?-?\\p{L}?\\(?\\)?,?)*)";

  private void write(String s) throws JspTagException {

    JspWriter out = pageContext.getOut();
    try {
      out.print(s);
    } catch (IOException e) {

      throw new JspTagException("Error writing to jsp writer!");
    }

  }

  public String getFields() {

    return fields;
  }

  public void setFields(String fields) {

    this.fields = fields;
  }

  public String formatDN() {

    StringBuffer repr = new StringBuffer();

    if (fields == null || "".equals(fields.trim()))
      return dn;

    String[] fieldsArray = fields.trim().split(",");

    for (int i = 0; i < fieldsArray.length; i++) {
      String regex = fieldsArray[i].trim() + regexTemplate;
      Pattern p = Pattern.compile(regex);
      Matcher m = p.matcher(dn);

      while (m.find()) {
        if (i > 0 || (repr.length() > 0))
          repr.append(",");
        repr.append(fieldsArray[i].trim() + "=");

        String match = m.group().trim();

        repr.append(match.substring(match.indexOf('=') + 1));

      }

    }

    if (repr.length() == 0)
      return dn;

    return repr.toString();

  }

  public int doStartTag() throws JspException {

    if (fields == null
      || VOMSConfiguration.instance().getBoolean(
        "voms.webapp.always-show-full-dns", false))
      write(dn);
    else {

      StringBuilder element = new StringBuilder();

      element
        .append("<div class=\"clickable formattedDN \" style=\"display:inline;\"><span title=\""
          + dn + "\">" + formatDN() + "</span>");
      element.append("<span style=\"display:none\">" + dn + "</span></div>");
      write(element.toString());

    }

    return SKIP_BODY;
  }

  public String getDn() {

    return dn;
  }

  public void setDn(String dn) {

    this.dn = dn;
  }
}
