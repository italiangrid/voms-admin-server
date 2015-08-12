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
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class TestContentTag extends TagSupport {

  String context;

  public int doStartTag() throws JspException {

    TestTag parent = (TestTag) getParent();
    if (parent == null) {

      throw new JspTagException(
        "TestContentTag may be used only inside TestTag!");
    }

    parent.contextList.add(context);

    return SKIP_BODY;

  }

  public String getContext() {

    return context;
  }

  public void setContext(String context) {

    this.context = context;
  }

}
