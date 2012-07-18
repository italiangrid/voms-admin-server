package org.glite.security.voms.admin.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.glite.security.SecurityContext;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.servlets.InitSecurityContext;

/**
 * A Jetty Handler that initializes a {@link SecurityContext}.
 * 
 * It should be included in front of other handlers to initialize the security context so that handlers
 * down the line can process the X.509 and leverage the information to take authorization decisions.
 * 
 * @author andreaceccanti
 *
 */
public class VOMSSecurityContextHandler extends AbstractHandler implements Handler {

	
	void setupVOMSRequestProperties(HttpServletRequest request){
		
		String voName = VOMSConfiguration.instance().getVOName();
		
		request.setAttribute(VOMSServiceConstants.VO_NAME_KEY, voName);
		request.setAttribute(VOMSServiceConstants.CURRENT_ADMIN_KEY, CurrentAdmin
				.instance());
		
	}
	
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		
		SecurityContext.clearCurrentContext();
		InitSecurityContext.setContextFromRequest(request);
		
		setupVOMSRequestProperties(request);
		
	}
}
