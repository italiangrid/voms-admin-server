/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/
package org.glite.security.voms.admin.notification.messages;

import org.apache.velocity.VelocityContext;
import org.glite.security.voms.admin.common.VOMSConfiguration;

public class ExpiredRequestNotification extends VelocityEmailNotification {

	static String templateFilename = "RequestExpired.vm";

	public ExpiredRequestNotification(String recipient) {

		setTemplateFile(templateFilename);
		addRecipient(recipient);

	}

	protected void buildMessage() {

		setSubject("You vo membership request has expired!");

		VelocityContext context = new VelocityContext();
		context.put("recipient", getRecipientList().get(0));
		context.put("voName", VOMSConfiguration.instance().getVOName());

		buildMessageFromTemplate(context);

	}

}
