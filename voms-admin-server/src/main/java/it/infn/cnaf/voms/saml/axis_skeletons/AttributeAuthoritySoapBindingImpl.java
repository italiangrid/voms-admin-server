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

/**
 * AttributeAuthoritySoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.infn.cnaf.voms.saml.axis_skeletons;

import it.infn.cnaf.voms.saml.SAMLAssertionFactory;
import it.infn.cnaf.voms.saml.SAMLResponseFactory;
import it.infn.cnaf.voms.saml.VOMSSAMLService;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;

public class AttributeAuthoritySoapBindingImpl implements
  it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthorityPortType {

  /**
   * 
   */
  static private Logger logger = LoggerFactory
    .getLogger(AttributeAuthoritySoapBindingImpl.class);

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

    if (conf.getBoolean(
      VOMSConfigurationConstants.VOMS_AA_SAML_ACTIVATE_ENDPOINT, false)) {
      SAMLAssertionFactory sAMLAssertionFactory = new SAMLAssertionFactory(
        conf.getServiceCertificate(), conf.getServicePrivateKey());

      SAMLResponseFactory sAMLResponseFactory = new SAMLResponseFactory(conf
        .getServiceCertificate().getSubjectX500Principal());

      this.vOMSSAMLService = new VOMSSAMLService(sAMLAssertionFactory,
        sAMLResponseFactory, conf.getInt(
          VOMSConfigurationConstants.VOMS_SAML_MAX_ASSERTION_LIFETIME, 720));
    }

  }

  public org.opensaml.saml2.core.Response attributeQuery(
    org.opensaml.saml2.core.AttributeQuery attributeQuery)
    throws java.rmi.RemoteException {

    if (!VOMSConfiguration.instance().getBoolean(
      VOMSConfigurationConstants.VOMS_AA_SAML_ACTIVATE_ENDPOINT, false))
      throw new RemoteException(
        "SAML attribute authority is currently disabled on this VOMS Admin instance.");

    HttpServletRequest httpServletRequest = (HttpServletRequest) MessageContext
      .getCurrentContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);

    return vOMSSAMLService.attributeQuery(attributeQuery, httpServletRequest);
  }
}
