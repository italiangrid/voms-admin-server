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
package org.glite.security.voms.admin.operations.aup;

import java.net.MalformedURLException;
import java.net.URL;

import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.AUP;

public class AddVersionOperation extends BaseVomsOperation {

	AUP aup;
	
	String version;
	
	String url;
	
	public AddVersionOperation(AUP aup, String version, String url) {
		this.aup = aup;
		this.version = version;
		this.url = url;
	}
	@Override
	protected Object doExecute() {
		AUPDAO dao = DAOFactory.instance().getAUPDAO();

		try {
			
			dao.addVersion(aup, version, new URL(url));
		
		} catch (MalformedURLException e) {
			
			throw new VOMSException("Malformed URL passed as argument: "+url,e);
		}
		
		return aup;
	}

	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerRWPermissions().setMembershipRWPermission());
	}

}
