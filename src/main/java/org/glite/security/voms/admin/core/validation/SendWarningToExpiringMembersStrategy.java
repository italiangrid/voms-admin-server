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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.core.validation.strategies.ExpiringMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleExpiringMembersStrategy;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendWarningToExpiringMembersStrategy implements
	HandleExpiringMembersStrategy, ExpiringMembersLookupStrategy {
    
    public static final Logger log = LoggerFactory.getLogger(SendWarningToExpiringMembersStrategy.class);
        
    Set<Integer> getWarningTimes(){
	
	String times = VOMSConfiguration.instance().getString(VOMSConfigurationConstants.MEMBERSHIP_EXPIRATION_WARNING_PERIOD, "15,1");
	
	String[] warningTimesStrings = times.split(",");
	Set<Integer> result = new HashSet<Integer>();
	
	for (String s: warningTimesStrings){
	    
	    Integer i = null;
	    
	    try{
		 i = Integer.parseInt(s);
		 
		 if (i > 0)
		     result.add(i);
		 else
		     log.warn("Ignoring negative value passed as argument {}. The {} property should contain a comma-separated list of postive integers!", s, VOMSConfigurationConstants.MEMBERSHIP_EXPIRATION_WARNING_PERIOD);
		 
	    }catch (NumberFormatException e) {
		log.error("Error converting {} to an integer. The {} property should contain a comma-separated list of postive integers!", i, VOMSConfigurationConstants.MEMBERSHIP_EXPIRATION_WARNING_PERIOD);
	    }
	    
	}
	
	return result;
	
    }
    
    public void handleMembersAboutToExpire(List<VOMSUser> expiringMembers) {
	
	if (expiringMembers ==  null | expiringMembers.isEmpty())
	    return;
	

    }

    
    public List<VOMSUser> findExpiringMembers() {
	
	Set<Integer> warningTimes = getWarningTimes();
	
	if (warningTimes == null || warningTimes.isEmpty()){
	    log.debug("No warning times set, returning the empty list");
	    return Collections.EMPTY_LIST;
	}
	return VOMSUserDAO.instance().findExpiringUsers(warningTimes.toArray(new Integer[0]));
    }

}
