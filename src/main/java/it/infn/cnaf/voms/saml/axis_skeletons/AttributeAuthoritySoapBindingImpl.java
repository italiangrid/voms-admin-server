/**
 *
 * Copyright 2006-2007 Istituto Nazionale di Fisica Nucleare (INFN)
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
 * File : AttributeAuthoritySoapBindingImpl.java
 *
 * Authors: Valerio Venturi <valerio.venturi@cnaf.infn.it>
 * 
 */

/**
 * AttributeAuthoritySoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.infn.cnaf.voms.saml.axis_skeletons;

import java.rmi.RemoteException;

import it.infn.cnaf.voms.saml.SAMLAssertionFactory;
import it.infn.cnaf.voms.saml.SAMLResponseFactory;
import it.infn.cnaf.voms.saml.VOMSSAMLService;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.glite.security.voms.admin.common.VOMSConfiguration;

public class AttributeAuthoritySoapBindingImpl implements
		it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthorityPortType {
	/**
   * 
   */
	static private Log logger = LogFactory
			.getLog(AttributeAuthoritySoapBindingImpl.class);

	/**
   * 
   */
	private VOMSSAMLService vOMSSAMLService;

	/**
   * 
   */
	public AttributeAuthoritySoapBindingImpl() {
		
		VOMSConfiguration conf = VOMSConfiguration.instance();

		/* instantiate the VomsSamlService object, if the service is active */

		if (conf.getBoolean(VOMSConfiguration.VOMS_AA_SAML_ACTIVATE_ENDPOINT,
				false)) {
			SAMLAssertionFactory sAMLAssertionFactory = new SAMLAssertionFactory(
					conf.getServiceCertificate(), conf.getServicePrivateKey());

			SAMLResponseFactory sAMLResponseFactory = new SAMLResponseFactory(
					conf.getServiceCertificate().getSubjectX500Principal());

			this.vOMSSAMLService = new VOMSSAMLService(sAMLAssertionFactory,
					sAMLResponseFactory, conf.getInt(
							VOMSConfiguration.VOMS_SAML_MAX_ASSERTION_LIFETIME,
							720));
		}

	}

	public org.opensaml.saml2.core.Response attributeQuery(
			org.opensaml.saml2.core.AttributeQuery attributeQuery)
			throws java.rmi.RemoteException {
		
		if (!VOMSConfiguration.instance().getBoolean(
				VOMSConfiguration.VOMS_AA_SAML_ACTIVATE_ENDPOINT, false))
			throw new RemoteException(
					"SAML attribute authority is currently disabled on this VOMS Admin instance.");

		HttpServletRequest httpServletRequest = (HttpServletRequest) MessageContext
				.getCurrentContext().getProperty(
						HTTPConstants.MC_HTTP_SERVLETREQUEST);

		return vOMSSAMLService.attributeQuery(attributeQuery,
				httpServletRequest);
	}
}
