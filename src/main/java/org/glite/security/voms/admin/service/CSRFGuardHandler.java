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
    
    
    
    public void invoke(MessageContext msgContext) throws AxisFault {
	
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
