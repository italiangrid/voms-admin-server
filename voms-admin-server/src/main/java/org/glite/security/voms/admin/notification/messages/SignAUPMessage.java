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

import java.util.Date;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;
import org.glite.security.voms.admin.util.URLBuilder;

public class SignAUPMessage extends AbstractVelocityNotification {

	VOMSUser user;
	AUP aup;

	public SignAUPMessage(VOMSUser user, AUP aup) {

		setUser(user);
		setAup(aup);

	}

	@Override
	protected void buildMessage() {

		VOMSConfiguration conf = VOMSConfiguration.instance();
		String voName = conf.getVOName();

		setSubject("Sign '" + aup.getName() + "' notification for VO '"
				+ conf.getVOName() + "'.");

		Date expirationDate = null;

		SignAUPTask t = user.getPendingSignAUPTask(aup);

		if (t != null)
			expirationDate = t.getExpiryDate();

		context.put("voName", voName);
		context.put("aup", aup);
		context.put("user", user);
		context.put("recipient", getRecipientList().get(0));
		context.put("signAUPURL", URLBuilder.baseVOMSURLFromConfiguration()
				+ "/aup/sign!input.action?aupId=" + aup.getId());
		context.put("expirationDate", expirationDate);

		super.buildMessage();
	}

	public VOMSUser getUser() {
		return user;
	}

	public void setUser(VOMSUser user) {
		this.user = user;
	}

	public AUP getAup() {
		return aup;
	}

	public void setAup(AUP aup) {
		this.aup = aup;
	}

}
