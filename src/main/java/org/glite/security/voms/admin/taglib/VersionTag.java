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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;

public class VersionTag extends TagSupport {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	public VersionTag() {

		super();
	}

	@Override
	public int doStartTag() throws JspException {

		VOMSConfiguration conf = VOMSConfiguration.instance();

		String version = conf
				.getString(VOMSConfiguration.VOMS_ADMIN_SERVER_VERSION);

		try {

			if (version == null)

				pageContext.getOut().print("undefined version");

			else
				pageContext.getOut().print(version);

		} catch (IOException e) {

			throw new JspTagException("Error writing version information!");
		}

		return SKIP_BODY;
	}

}
