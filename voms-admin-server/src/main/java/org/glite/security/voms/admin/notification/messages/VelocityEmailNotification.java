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
package org.glite.security.voms.admin.notification.messages;

import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.glite.security.voms.admin.error.VOMSException;

public abstract class VelocityEmailNotification extends EmailNotification {

	private String templateFile;

	protected String subjectPrefix = "[VOMS Admin]";

	public String getTemplateFile() {

		return templateFile;
	}

	public void setTemplateFile(String templateFile) {

		this.templateFile = templateFile;
	}

	protected void buildMessageFromTemplate(VelocityContext context) {

		StringWriter w = new StringWriter();

		try {

			Velocity.mergeTemplate(templateFile, "UTF-8", context, w);

		} catch (Exception e) {
			throw new VOMSException(e);
		}

		setMessage(w.toString());
	}

}
