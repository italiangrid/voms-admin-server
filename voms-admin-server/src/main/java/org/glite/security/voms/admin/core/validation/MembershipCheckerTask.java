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

package org.glite.security.voms.admin.core.validation;

import java.util.List;

import org.glite.security.voms.admin.core.tasks.RegistrationServiceTask;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MembershipCheckerTask extends AbstractMembershipChecker implements Runnable, RegistrationServiceTask{
	
	public static final Logger log = LoggerFactory.getLogger(MembershipCheckerTask.class);

	public MembershipCheckerTask(MembershipValidationContext ctxt) {
		setValidationContext(ctxt);
	}
	
	public void checkMembershipStatus() {
		
		List<VOMSUser> expiringMembers = getExpiringMembersLookupStrategy().findExpiringMembers();
		
		log.debug("Expiring members: {}", expiringMembers);
		
		getHandleExpiringMembersStrategy().handleMembersAboutToExpire(expiringMembers);
		
		List<VOMSUser> expiredMembers = getExpiredMembersLookupStrategy().findExpiredMembers();
		
		log.debug("Expired members: {}", expiredMembers);
		
		getHandleExpiredMembersStrategy().handleExpiredMembers(expiredMembers);
		
		List<VOMSUser> aupFailingMembers = getAupFailingMembersLookupStrategy().findAUPFailingMembers();
		
		log.debug("AUP failing members: {}", aupFailingMembers);
		
		getHandleAUPFailingMembersStrategy().handleAUPFailingMembers(aupFailingMembers);

	}

	public void run() {
	    
	    checkMembershipStatus();
	    
	}

}
