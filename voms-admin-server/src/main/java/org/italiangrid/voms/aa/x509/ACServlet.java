/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
 */
package org.italiangrid.voms.aa.x509;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.italiangrid.utils.voms.CurrentSecurityContext;
import org.italiangrid.utils.voms.SecurityContextImpl;
import org.italiangrid.utils.voms.VOMSSecurityContext;
import org.italiangrid.voms.aa.AttributeAuthority;
import org.italiangrid.voms.aa.AttributeAuthorityFactory;
import org.italiangrid.voms.aa.RequestContext;
import org.italiangrid.voms.aa.RequestContextFactory;
import org.italiangrid.voms.aa.VOMSError;
import org.italiangrid.voms.aa.VOMSErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ACServlet extends HttpServlet {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  public static final String LEGACY_REQUEST_HEADER = "X-VOMS-Legacy";

  public static final Logger log = LoggerFactory.getLogger(ACServlet.class);

  private VOMSResponseBuilder responseBuilder;
  private ACGenerator acGenerator;
  private ACRequestLogger acRequestLogger;

  /**
   * 
   * @param response
   * @param context
   */
  protected void addDebugHeaders(HttpServletResponse response,
    RequestContext context) {

    response.addHeader("X-VOMS-FQANs",
      StringUtils.join(context.getResponse().getIssuedFQANs(), ","));

    response.addHeader("X-VOMS-GAs",
      StringUtils.join(context.getResponse().getIssuedGAs(), ","));
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    RequestContext context = newRequestContext(request);
    boolean isLegacyRequest = isLegacyRequest(request);

    prepareResponse(response);

    if (!isACEndpointEnabled()) {
      VOMSErrorMessage em = VOMSErrorMessage.endpointDisabled();

      logFailure(context, em.getMessage());

      writeErrorResponse(request, response, em, isLegacyRequest);

      return;
    }

    if (CurrentAdmin.instance().isUnauthenticated()) {
      logFailure(context, VOMSError.UnauthenticatedClient.getDefaultMessage());

      writeErrorResponse(request, response,
        VOMSErrorMessage.unauthenticatedClient(), isLegacyRequest);

      return;
    }

    populateRequest(request, context);

    AttributeAuthority aa = newAttributeAuthority();

    if (!aa.getAttributes(context)) {

      VOMSErrorMessage em = context.getResponse().getErrorMessages().get(0);

      logFailure(context, em.getMessage());

      writeErrorResponse(request, response, em, isLegacyRequest);

      return;

    } else {

      try {

        byte[] acBytes = acGenerator.generateVOMSAC(context);

        addDebugHeaders(response, context);
        writeResponse(response, acBytes, context);
        logSuccess(context);
        return;

      } catch (Throwable t) {

        log.error(
          "Error encoding user attribute certificate: " + t.getMessage(), t);

        logFailure(context, VOMSError.InternalError.getDefaultMessage());

        writeErrorResponse(request, response,
          VOMSErrorMessage.internalError(t.getMessage()), isLegacyRequest);
      }
    }
  }

  @Override
  public void init() throws ServletException {

    super.init();
    responseBuilder = ResponseBuilderFactory.newResponseBuilder();
    acGenerator = ACGeneratorFactory.newACGenerator();
    acRequestLogger = ACRequestLoggerFactory.newRequestLogger();
  }

  /**
   * 
   * @return
   */
  protected boolean isACEndpointEnabled() {

    return VOMSConfiguration.instance().getBoolean(
      VOMSConfigurationConstants.VOMS_AA_X509_ACTIVATE_ENDPOINT, true);
  }

  /**
   * 
   * @param request
   * @return
   */
  protected boolean isLegacyRequest(HttpServletRequest request) {

    return request.getHeader(LEGACY_REQUEST_HEADER) != null;
  }

  /**
   * 
   * @param context
   * @param errorMessage
   */
  public void logFailure(RequestContext context, String errorMessage) {

    acRequestLogger.logFailure(context, errorMessage);
  }

  /**
   * 
   * @param context
   */
  public void logSuccess(RequestContext context) {

    acRequestLogger.logSuccess(context);
  }

  /**
   * 
   * @return
   */
  protected AttributeAuthority newAttributeAuthority() {

    long maximumValidity = VOMSConfiguration.instance().getLong(
      VOMSConfigurationConstants.VOMS_AA_X509_MAX_AC_VALIDITY,
      TimeUnit.HOURS.toSeconds(12));

    boolean legacyFQANEncoding = VOMSConfiguration.instance().getBoolean(
      VOMSConfigurationConstants.VOMS_AA_X509_LEGACY_FQAN_ENCODING, true);

    return AttributeAuthorityFactory.newAttributeAuthority(maximumValidity,
      legacyFQANEncoding);
  }

  /**
   * 
   * @param request
   * @return
   */
  protected RequestContext newRequestContext(HttpServletRequest request) {

    RequestContext context = RequestContextFactory.newContext();

    String subject = CurrentAdmin.instance().getRealSubject();

    String issuer = CurrentAdmin.instance().getRealIssuer();

    context.getRequest().setRequesterSubject(subject);
    context.getRequest().setRequesterIssuer(issuer);
    context.getRequest().setHolderSubject(subject);
    context.getRequest().setHolderIssuer(issuer);

    context.setVOName(VOMSConfiguration.instance().getVOName());
    context.setHost(VOMSConfiguration.instance().getHostname());
    context.setPort(request.getServerPort());

    return context;
  }

  /**
   * 
   * @param request
   * @return
   */
  protected List<String> parseRequestedFQANs(HttpServletRequest request) {

    String fqansString = request.getParameter("fqans");

    if (fqansString == null)
      return Collections.emptyList();

    List<String> requestedFQANs = new ArrayList<String>();

    if (fqansString.contains(",")) {

      for (String s : StringUtils.split(fqansString, ","))
        requestedFQANs.add(s);
    } else
      requestedFQANs.add(fqansString);

    return requestedFQANs;

  }

  /**
   * 
   * @param request
   * @return
   */
  private List<String> parseTargetsFromRequest(HttpServletRequest request) {

    String targets = request.getParameter("targets");
    if (targets == null)
      return Collections.emptyList();

    List<String> targetList = new ArrayList<String>();

    if (targets.contains(",")) {

      String targetTokens[] = targets.split(",");

      for (String t : targetTokens)
        targetList.add(t);

    } else {

      targetList = Collections.singletonList(targets);

    }

    return targetList;

  }

  /**
   * Parse
   * 
   * @param request
   * @return
   */
  private long parseValidityFromRequest(HttpServletRequest request) {

    long lifetime = -1;

    try {
      String lifetimeString = request.getParameter("lifetime");

      if (lifetimeString != null)
        lifetime = Long.parseLong(request.getParameter("lifetime"));

    } catch (NumberFormatException e) {
      // Ignore strange things in lifetime parameter
    }
    return lifetime;
  }

  /**
   * 
   * @param request
   * @param context
   */
  private void populateRequest(HttpServletRequest request,
    RequestContext context) {

    VOMSSecurityContext ctxt = (VOMSSecurityContext) CurrentSecurityContext
      .get();

    context.getRequest().setHolderCert(ctxt.getClientCert());

    context.getRequest().setRequestedFQANs(parseRequestedFQANs(request));
    context.getRequest()
      .setRequestedValidity(parseValidityFromRequest(request));
    context.getRequest().setTargets(parseTargetsFromRequest(request));

    context.getRequest().setOwnedAttributes(ctxt.getVOMSAttributes());

  }

  /**
   * 
   * @param response
   */
  protected void prepareResponse(HttpServletResponse response) {

    response.setContentType("text/xml");
    response.setCharacterEncoding("UTF-8");
  }

  /**
   * 
   * @param request
   * @param response
   * @param errorMessage
   * @param isLegacyClient
   * @throws ServletException
   * @throws IOException
   */
  protected void writeErrorResponse(HttpServletRequest request,
    HttpServletResponse response, VOMSErrorMessage errorMessage,
    boolean isLegacyClient) throws ServletException, IOException {

    response.setStatus(errorMessage.getError().getHttpStatus());

    String responseString = null;

    if (isLegacyClient) {
      responseString = responseBuilder.createLegacyErrorResponse(errorMessage);
    } else
      responseString = responseBuilder.createErrorResponse(errorMessage);

    response.getOutputStream().write(responseString.getBytes());
  }

  /**
   * 
   * @param response
   * @param ac
   * @param context
   * @throws ServletException
   * @throws IOException
   */
  protected void writeResponse(HttpServletResponse response, byte[] ac,
    RequestContext context) throws ServletException, IOException {

    String responseString = responseBuilder.createResponse(ac, context
      .getResponse().getWarnings());

    response.getOutputStream().write(responseString.getBytes());
  }

}
