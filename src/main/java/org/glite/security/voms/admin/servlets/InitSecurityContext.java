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
package org.glite.security.voms.admin.servlets;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.axis.transport.http.HTTPSender;
import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.error.VOMSException;
import org.italiangrid.utils.voms.SecurityContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The InitSecurityContext is and AXIS handler that can be put in a
 * <i>org.glite.security.voms.admin.request flow</i> in front of an actual SOAP
 * endpoint that it initializes the SecurityContext.
 * <p>
 * <p>
 * Currently, only the case of SOAP over HTTPS with client authentication is
 * supported.
 * <p>
 * <b>Configuration (Tomcat)</b><br>
 * The handler is invoked by first defining a <code>handler</code> in the
 * <code>.wsdd</code> file:
 * 
 * <pre>
 *   &lt;handler name=&quot;initSC&quot;
 *       type=&quot;java:org.glite.security.voms.service.InitSecurityContext&quot;&gt;
 *   &lt;/handler&gt;
 * </pre>
 * 
 * For the org.glite.security.voms.admin.servlets in question, a
 * org.glite.security.voms.admin.request flow is the defined:
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
 * @author Karoly Lorentey <Karoly.Lorentey@cern.ch>
 */
public class InitSecurityContext {

	protected static Logger log = LoggerFactory
			.getLogger(InitSecurityContext.class);

	/**
	 * Sets up the client's credentials. This method sets the current
	 * <code>SecurityContext</code> to a new instance and initializes it from
	 * the client's certificate. It also sets the
	 * {@linkplain VOMSServiceConstants#SECURITY_CONTEXT_REMOTE_ADDRESS remote
	 * IP address property}.
	 * 
	 * <p>
	 * If the certificate is invalid, or there is some other problem with the
	 * client's credentials, then the distinguished name and CA will be set to
	 * <code>null</code>.
	 * 
	 * @see org.glite.security.SecurityContext
	 */
	public static void setContextFromRequest(final ServletRequest req) {

		log.debug("Creating a new security context");
		SecurityContextImpl sc = new SecurityContextImpl();
		// Store remote address.
		String remote = req.getRemoteAddr();
		sc.setRemoteAddr(remote);

		X509Certificate[] cert = null;
		try {
			// Interpret the client's certificate.
			cert = (X509Certificate[]) req
					.getAttribute("javax.servlet.request.X509Certificate");
		} catch (Throwable t) {
			throw new VOMSException(t.getMessage(), t);
		}

		if (cert == null) {

			sc.setClientName(VOMSServiceConstants.UNAUTHENTICATED_CLIENT);
			sc.setIssuerName(VOMSServiceConstants.VIRTUAL_CA);

		} else {
			// Client certificate found.
			sc.setClientCertChain(cert);

			// Do not allow internal credentials coming from an external source.
			if (sc.getClientName() != null
					&& sc.getClientName().startsWith(
							VOMSServiceConstants.INTERNAL_DN_PREFIX)) {
				log.error("Client name starts with internal prefix, discarding credentials: "
						+ sc.getClientName());
				sc.setClientName(VOMSServiceConstants.UNAUTHENTICATED_CLIENT);
				sc.setIssuerName(VOMSServiceConstants.VIRTUAL_CA);
			} else if (sc.getIssuerName() != null
					&& sc.getIssuerName().startsWith(
							VOMSServiceConstants.INTERNAL_DN_PREFIX)) {
				log.error("Client issuer starts with internal prefix, discarding credentials: "
						+ sc.getClientName());
				sc.setClientName(VOMSServiceConstants.UNAUTHENTICATED_CLIENT);
				sc.setIssuerName(VOMSServiceConstants.VIRTUAL_CA);
			}
		}

		SecurityContextImpl.setCurrentContext(sc);

	}

	public static void logConnection() {

		SecurityContextImpl sc = SecurityContextImpl.getCurrentContext();

		if (sc.getClientCert() == null) {

			log.info("Unauthenticated connection from \"{}\"",
					sc.getRemoteAddr());

		} else {

			String clientName = sc.getClientName();
			String issuerName = sc.getIssuerName();

			BigInteger sn = sc.getClientCert().getSerialNumber();

			String serialNumber = (sn == null) ? "NULL" : sn.toString();

			log.info("Connection from \"" + sc.getRemoteAddr() + "\" by "
					+ clientName + " (issued by \"" + issuerName + "\", "
					+ "serial " + serialNumber + ")");
		}
	}

	/**
	 * Initialize a clear security context, which will fail on all security
	 * checks. It is intended for non-authenticated requests.
	 */
	public static void setClearContext() {

		log.info("Clearing the security context");
		SecurityContextImpl sc = new SecurityContextImpl();
		SecurityContextImpl.setCurrentContext(sc);

		sc.setClientName(VOMSServiceConstants.UNAUTHENTICATED_CLIENT);
		sc.setIssuerName(VOMSServiceConstants.VIRTUAL_CA);
	}
}
