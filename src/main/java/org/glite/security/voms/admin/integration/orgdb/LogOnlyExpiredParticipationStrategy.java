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

package org.glite.security.voms.admin.integration.orgdb;

import org.glite.security.voms.admin.integration.orgdb.model.VOMSOrgDBPerson;
import org.glite.security.voms.admin.integration.orgdb.strategies.OrgDbExpiredParticipationStrategy;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogOnlyExpiredParticipationStrategy implements
	OrgDbExpiredParticipationStrategy {

    public static final Logger log = LoggerFactory.getLogger(LogOnlyExpiredParticipationStrategy.class);
    
    public void handleOrgDbExpiredParticipation(VOMSUser u,
	    VOMSOrgDBPerson orgDBPerson, String experimentName) {
	
	log.debug("User {} participation for experiment {} has expired.", u, experimentName);

    }

}
