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
package org.glite.security.voms.admin.notification;

import java.util.HashSet;
import java.util.Set;

import org.glite.security.voms.admin.common.PathNamingScheme;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;

public class NotificationUtil {

	public static Set getEmailAddressesForAdmin(VOMSAdmin a) {

		Set emails = new HashSet();

		if (!a.isInternalAdmin())
			emails.add(a.getEmailAddress());
		else {

			if (a.isGroupAdmin()) {

				VOMSGroup g = VOMSGroupDAO.instance().findByName(a.getDn());
				emails.addAll(g.getMembersEmailAddresses());

			} else if (a.isRoleAdmin()) {

				VOMSRole r = VOMSRoleDAO.instance().findByName(
						PathNamingScheme.getRoleName(a.getDn()));
				VOMSGroup g = VOMSGroupDAO.instance().findByName(
						PathNamingScheme.getGroupName(a.getDn()));
				emails.addAll(r.getMembersEmailAddresses(g));

			}

		}

		return emails;
	}

}
