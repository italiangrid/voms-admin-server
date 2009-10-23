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
package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.event.EventType;
import org.glite.security.voms.admin.event.GenericEvent;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;

public abstract class VOMembershipRequestEvent extends GenericEvent {

	NewVOMembershipRequest request;

	public VOMembershipRequestEvent(NewVOMembershipRequest r) {
		super(EventType.VOMembershipRequestEvent);
		request = r;

	}

	public NewVOMembershipRequest getRequest() {
		return request;
	}

	public void setRequest(NewVOMembershipRequest request) {
		this.request = request;
	}

}
