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

import javax.servlet.ServletRequest;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.servlets.InitSecurityContext;

/**
 * This class is an AXIS handler that can be put in a
 * <i>org.glite.security.voms.admin.request flow</i> in front of an actual SOAP
 * endpoint that it initializes the SecurityContext.
 * <p>
 * Currently, only the case of SOAP over HTTPS with client authentication is
 * supported.
 * <p>
 * <b>Configuration (Tomcat)</b><br>
 * The handler is installed by first defining a <code>handler</code> in the
 * <code>.wsdd</code> file:
 * 
 * <pre>
 *   &lt;handler name=&quot;initSC&quot;
 *       type=&quot;java:org.glite.security.voms.service.SecurityContextHandler&quot;&gt;
 *   &lt;/handler&gt;
 * </pre>
 * 
 * A org.glite.security.voms.admin.request flow also needs to be defined for the
 * org.glite.security.voms.admin.servlets in question:
 * 
 * <pre>
 *   &lt;org.glite.security.voms.admin.service name=&quot;TestService&quot; ...&gt;
 *      &lt;requestFlow&gt;
 *          &lt;handler type=&quot;initSC&quot;/&gt;
 *      &lt;/requestFlow&gt;
 *      ...
 *   &lt;/org.glite.security.voms.admin.service&gt;
 * </pre>
 * 
 * @author mulmo
 */
public class SecurityContextHandler extends BasicHandler {

	/**
     * 
     */
	private static final long serialVersionUID = 5598719642627462697L;

	protected static Log log = LogFactory.getLog(SecurityContextHandler.class);

	/**
	 * Initializes the SecurityContext from a
	 * {@link org.apache.axis.MessageContext}.
	 * 
	 * @see org.apache.axis.Handler#invoke(MessageContext)
	 */
	public void invoke(final MessageContext mc) throws AxisFault {

		// Try to initialize the client's certificate chain from the
		// org.glite.security.voms.admin.servlets
		// container's (e.g. tomcat) context.

		ServletRequest req = (ServletRequest) MessageContext
				.getCurrentContext().getProperty(
						HTTPConstants.MC_HTTP_SERVLETREQUEST);
		if (req == null) {
			log.warn("SOAP Authorization: MC_HTTP_SERVLETREQUEST is null");
			InitSecurityContext.setClearContext();

		} else {
			InitSecurityContext.setContextFromRequest(req);
		}
	}

}

// Please do not change this line.
// arch-tag: b6c66c1b-a4ea-4315-b459-344ac99055f4
