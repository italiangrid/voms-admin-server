/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009. See
 * http://www.eu-egee.org/partners/ for details on the copyright holders.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Authors: Andrea Ceccanti (INFN)
 */

package org.italiangrid.voms.aa.impl;

import java.util.Iterator;

import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.model.VOMSBaseAttribute;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSGroupAttribute;
import org.glite.security.voms.admin.persistence.model.VOMSMapping;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSRoleAttribute;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUserAttribute;
import org.glite.security.voms.admin.util.PathNamingScheme;
import org.italiangrid.voms.VOMSGenericAttribute;
import org.italiangrid.voms.aa.RequestContext;
import org.italiangrid.voms.aa.VOMSErrorMessage;
import org.italiangrid.voms.aa.VOMSRequest;
import org.italiangrid.voms.aa.VOMSResponse;
import org.italiangrid.voms.aa.VOMSResponse.Outcome;

public class DefaultVOMSAttributeResolver implements AttributeResolver {

	public DefaultVOMSAttributeResolver() {

	}

	protected void failResponse(VOMSResponse r, VOMSErrorMessage e) {

		r.setOutcome(Outcome.FAILURE);
		r.getErrorMessages().add(e);
	}

	protected String formatFQAN(String fqan) {

		return fqan;
	}

	protected void resolveRequestedFQANs(RequestContext context) {

		VOMSRequest request = context.getRequest();
		VOMSUser u = context.getVOMSUser();
		VOMSResponse response = context.getResponse();

		for (String fqan : request.getRequestedFQANs()) {
			if (PathNamingScheme.isQualifiedRole(fqan)) {
				if (u.hasRole(fqan)) {
					response.getIssuedFQANs().add(formatFQAN(fqan));
				} else {
					failResponse(response, VOMSErrorMessage.noSuchAttribute(fqan));
					context.setHandled(true);
					return;
				}

			} else if (PathNamingScheme.isGroup(fqan)) {
				if (u.isMember(fqan)) {
					response.getIssuedFQANs().add(formatFQAN(fqan));
				} else {
					failResponse(response, VOMSErrorMessage.noSuchAttribute(fqan));
					context.setHandled(true);
					return;
				}
			}
		}
	}

	protected void resolveCompulsoryGroupFQANs(RequestContext context) {

		VOMSUser u = context.getVOMSUser();
		VOMSResponse response = context.getResponse();

		Iterator<VOMSMapping> mappingIter = u.getMappings().iterator();
		while (mappingIter.hasNext()) {
			VOMSMapping mapping = mappingIter.next();

			if (mapping.isGroupMapping()) {
				String fqan = formatFQAN(mapping.getFQAN());

				if (!response.getIssuedFQANs().contains(fqan))
					response.getIssuedFQANs().add(fqan);
			}
		}
	}

	@Override
	public void resolveFQANs(RequestContext context) {

		resolveRequestedFQANs(context);
		resolveCompulsoryGroupFQANs(context);

	}
	
	protected String normalizeFQAN(String fqan){
		return fqan;
	}

	protected void addContainerGAs(RequestContext context) {

		for (String f : context.getResponse().getIssuedFQANs()) {

			String fqan=normalizeFQAN(f);
			
			if (PathNamingScheme.isGroup(fqan)) {
				VOMSGroup g = VOMSGroupDAO.instance().findByName(fqan);

				for (VOMSGroupAttribute gattr : g.getAttributes()) {
					context.getResponse().getIssuedGAs().add(newGenericAttribute(gattr));
				}

			} else {

				String roleName = PathNamingScheme.getRoleName(fqan);
				String groupName = PathNamingScheme.getGroupName(fqan);

				VOMSRole r = VOMSRoleDAO.instance().findByName(roleName);
				VOMSGroup g = VOMSGroupDAO.instance().findByName(groupName);

				for (VOMSRoleAttribute rattr : r.getAttributesInGroup(g)) {
					context.getResponse().getIssuedGAs().add(newGenericAttribute(rattr));
				}
			}
		}
	}

	protected void addUserGAs(RequestContext context) {

		VOMSUser user = context.getVOMSUser();
		VOMSResponse response = context.getResponse();

		for (VOMSUserAttribute attr : user.getAttributes()) {
			response.getIssuedGAs().add(newGenericAttribute(attr));
		}
	}

	@Override
	public void resolveGAs(RequestContext context) {

		addUserGAs(context);
		addContainerGAs(context);

	}

	protected VOMSGenericAttribute newGenericAttribute(VOMSBaseAttribute ua) {

		return new VOMSGAImpl(ua);
	}

}
