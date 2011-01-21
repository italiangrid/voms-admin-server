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

package org.glite.security.voms.admin.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.transport.http.HTTPConstants;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.error.VOMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSRFGuardHandler extends BasicHandler {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public static final String CSRF_GUARD_HEADER_NAME = "X-VOMS-CSRF-GUARD";
    
    public static final Logger log = LoggerFactory.getLogger(CSRFGuardHandler.class);
    
    public static final String[] uncheckedServices = {"VOMSSaml", "VOMSCompatibility"};
    
    
    public void invoke(MessageContext msgContext) throws AxisFault {
	
	// Don't check readonly services
	for (String uncheckedService: uncheckedServices)
	    if (uncheckedService.equals(msgContext.getTargetService()))
		    return;    
	
	
	HttpServletRequest httpReq = (HttpServletRequest) MessageContext
		.getCurrentContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
	
	boolean csrfGuardLogOnly = VOMSConfiguration.instance().getBoolean(VOMSConfigurationConstants.VOMS_CSRF_GUARD_LOG_ONLY, false);
	
	if (httpReq.getHeader(CSRF_GUARD_HEADER_NAME) == null){
	    
	    log.warn("Incoming request from {}:{} is missing CSRF prevention HTTP header", httpReq.getRemoteAddr(), httpReq.getRemotePort());
	    
	    if (!csrfGuardLogOnly)
		throw new VOMSException("CSRF header guard missing from request!");
	    
	}
    }

}
