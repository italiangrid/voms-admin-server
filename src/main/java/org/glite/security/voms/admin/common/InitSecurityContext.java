/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 * 
 *     Karoly Lorentey - karoly.lorentey.@cern.ch
 * 
 *******************************************************************************/
package org.glite.security.voms.admin.common;

import java.math.BigInteger;
import java.security.cert.X509Certificate;

import javax.servlet.ServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.SecurityContext;

/**
 * The InitSecurityContext is and AXIS handler that can be put in a <i>org.glite.security.voms.admin.request
 * flow</i> in front of an actual SOAP endpoint that it initializes the
 * SecurityContext.
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
 * For the org.glite.security.voms.admin.servlets in question, a org.glite.security.voms.admin.request flow is the defined:
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

    protected static Log log = LogFactory.getLog( InitSecurityContext.class );

    /**
     * Sets up the client's credentials. This method sets the current
     * <code>SecurityContext</code> to a new instance and initializes it from
     * the client's certificate. It also sets the {@linkplain
     * Constants#SECURITY_CONTEXT_REMOTE_ADDRESS remote IP address property}.
     * 
     * <p>
     * If the certificate is invalid, or there is some other problem with the
     * client's credentials, then the distinguished name and CA will be set to
     * <code>null</code>, unless the client is from localhost and the
     * configuration option <code>voms.localhost.defaults.to.local.admin</code>
     * is true. In this latter case, the credentials will be set to that of the
     * {@linkplain Constants#LOCAL_ADMIN Local Database Administrator}.
     * 
     * @see org.glite.security.SecurityContext
     */
    public static void setContextFromRequest( final ServletRequest req ) {

        log.debug( "Creating a new security context" );
        SecurityContext sc = new SecurityContext();
        SecurityContext.setCurrentContext( sc );

        // Store remote address.
        String remote = req.getRemoteAddr();
        sc.setProperty( Constants.SECURITY_CONTEXT_REMOTE_ADDRESS, remote );

        X509Certificate[] cert = null;
        try {
            // Interpret the client's certificate.
            cert = (X509Certificate[]) req
                    .getAttribute( "javax.servlet.request.X509Certificate" );
        } catch ( Exception e ) {
            log.warn( "Exception during certificate chain retrieval: " + e );
            // We swallow the exception and continue processing.
        }

        if ( cert == null ) {
            // No certificate.
            log.info( "Unauthenticated connection from \"" + remote + "\"" );

            if ( VOMSConfiguration.instance().getBoolean(
                    VOMSConfiguration.LOCALHOST_DEFAULTS_TO_LOCAL_ADMIN, false )
                    && remote != null
                    && ( "127.0.0.1".equals( remote ) || "0:0:0:0:0:0:0:1"
                            .equals( remote ) ) ) {

                log
                        .warn( "*** Overriding null credentials from localhost with Local Database Admin ***" );
                sc.setClientName( Constants.LOCAL_ADMIN );
                sc.setIssuerName( Constants.VIRTUAL_CA );
            } else {
                sc.setClientName( Constants.UNAUTHENTICATED_CLIENT );
                sc.setIssuerName( Constants.VIRTUAL_CA );
            }

        } else {
            // Client certificate found.
            sc.setClientCertChain( cert );

            // Convert the DNs to the old format that we use in the org.glite.security.voms.admin.database.
            if ( sc.getClientName() != null )
                sc.setClientName( DNUtil.getBCasX500( sc.getClientCert()
                        .getSubjectX500Principal() ) );

            if ( sc.getIssuerName() != null )
                sc.setIssuerName( DNUtil.getBCasX500( sc.getClientCert()
                        .getIssuerX500Principal() ) );

            String clientName = sc.getClientName();
            String issuerName = sc.getIssuerName();
            BigInteger sn = sc.getClientCert().getSerialNumber();

            String serialNumber = ( sn == null ) ? "NULL" : sn.toString();

            log.info( "Connection from \"" + remote + "\" by " + clientName
                    + " (issued by \"" + issuerName + "\", " + "serial "
                    + serialNumber + ")" );

            // Do not allow internal credentials coming from an external source.
            if ( sc.getClientName() != null
                    && sc.getClientName().startsWith(
                            Constants.INTERNAL_DN_PREFIX ) ) {
                log
                        .error( "Client name starts with internal prefix, discarding credentials: "
                                + sc.getClientName() );
                sc.setClientName( Constants.UNAUTHENTICATED_CLIENT );
                sc.setIssuerName( Constants.VIRTUAL_CA );
            } else if ( sc.getIssuerName() != null
                    && sc.getIssuerName().startsWith(
                            Constants.INTERNAL_DN_PREFIX ) ) {
                log
                        .error( "Client issuer starts with internal prefix, discarding credentials: "
                                + sc.getClientName() );
                sc.setClientName( Constants.UNAUTHENTICATED_CLIENT );
                sc.setIssuerName( Constants.VIRTUAL_CA );
            }
        }
    }

    /**
     * Initialize and set delegated admin's security context. This method should
     * only be used in restricted cases, because it effectively overrides the
     * credentials with user supplied values! <br>
     * To make it as safe as possible one has to configure the
     * <code>voms.fully.trusted.client.for.delegation.dn</code> and
     * <code>voms.fully.trusted.client.for.delegation.ca</code> values to
     * enable this functionality for one trusted org.glite.security.voms.admin.service. <br>
     * <b>Enabling this feature is the equivalent of giving the org.glite.security.voms.admin.database
     * password and link to the remote org.glite.security.voms.admin.service. Use with care, and only if you
     * really know what you are doing!</b>
     */
    public static void setDelegatedContext( final String delegatedDN,
            final String delegatedCA ) throws VOMSSecurityException {

        log.info( "Initializing the delegated security context" );
        SecurityContext sc = SecurityContext.getCurrentContext();

        if ( sc == null ) {
            throw new VOMSSecurityException(
                    "No security context for delegation?" );
        }

        // check the DN: configured, not virtual and matching the current
        String validDN = VOMSConfiguration.instance().getString(
                "voms.fully.trusted.client.for.delegation.dn" );
        if ( validDN == null || validDN.equals( Constants.LOCAL_ADMIN ) ) {
            throw new VOMSSecurityException(
                    "No valid trusted DN is configured" );
        }
        if ( !validDN.equals( sc.getClientName() ) ) {
            throw new VOMSSecurityException(
                    "Client is not trusted for delegation: "
                            + sc.getClientName() );
        }

        // check the CA: configured, not virtual and matching the current
        String validCA = VOMSConfiguration.instance().getString(
                "voms.fully.trusted.client.for.delegation.ca" );
        if ( validCA == null || validCA.equals( Constants.VIRTUAL_CA ) ) {
            throw new VOMSSecurityException(
                    "No valid trusted CA is configured" );
        }
        if ( !validCA.equals( sc.getIssuerName() ) ) {
            throw new VOMSSecurityException(
                    "CA is not trusted for delegation: " + sc.getIssuerName() );
        }

        // OK, the client may replace the credentials
        log.info( "Trusted client (" + sc.getClientName() + ", "
                + sc.getIssuerName() + ") sets delegated credentials ("
                + delegatedDN + ", " + delegatedCA + ")" );
        sc.setClientName( delegatedDN );
        sc.setIssuerName( delegatedCA );
    }

    /**
     * Initialize and set local admin's security context.
     */
    public static void setLocalContext( final String host ) {

        log.debug( "Initializing the local admin's security context" );
        SecurityContext sc = new SecurityContext();
        SecurityContext.setCurrentContext( sc );
        // sc.setAuthorizedAttributes(Arrays.asList(new String[] { }));
        sc.setClientName( Constants.LOCAL_ADMIN );
        sc.setIssuerName( Constants.VIRTUAL_CA );
        if ( host != null )
            sc.setProperty( Constants.SECURITY_CONTEXT_REMOTE_ADDRESS, host );
    }

    /**
     * Initialize and set internal admin's security context.
     */
    public static void setInternalContext() {

        log.debug( "Initializing the internal admin's security context" );
        SecurityContext sc = new SecurityContext();
        SecurityContext.setCurrentContext( sc );
        // sc.setAuthorizedAttributes(Arrays.asList(new String[] { }));
        sc.setClientName( Constants.INTERNAL_ADMIN );
        sc.setIssuerName( Constants.VIRTUAL_CA );
    }

    /**
     * Initialize a clear security context, which will fail on all security
     * checks. It is intended for non-authenticated requests.
     */
    public static void setClearContext() {

        log.info( "Clearing the security context" );
        SecurityContext sc = new SecurityContext();
        SecurityContext.setCurrentContext( sc );
        sc.setClientName( Constants.UNAUTHENTICATED_CLIENT );
        sc.setIssuerName( Constants.VIRTUAL_CA );
    }
}

// Please do not change this line.
// arch-tag: 00968f90-6182-41c0-ab43-993f992e4d25

