/**************************************************************************

 Copyright 2006-2007 Istituto Nazionale di Fisica Nucleare (INFN)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 File : ResponseFactory.java

 Authors: Valerio Venturi <valerio.venturi@cnaf.infn.it>
 
**************************************************************************/

package it.infn.cnaf.voms.saml;



import it.infn.cnaf.voms.saml.exceptions.IssuerPeerMismatchException;
import it.infn.cnaf.voms.saml.exceptions.UnauthorizedQueryException;
import it.infn.cnaf.voms.saml.exceptions.UnknownAttributeException;
import it.infn.cnaf.voms.saml.exceptions.UnknownPrincipalException;
import it.infn.cnaf.voms.saml.exceptions.VersionMismatchException;
import it.infn.cnaf.voms.saml.exceptions.X509SubjectWrongNameIDFormatException;
import it.infn.cnaf.voms.saml.exceptions.X509SubjectWrongNameIDValueException;

import java.util.HashMap;
import java.util.UUID;

import javax.security.auth.x500.X500Principal;

import org.apache.log4j.Logger;
import org.glite.security.voms.admin.database.SuspendedCertificateException;
import org.glite.security.voms.admin.database.SuspendedUserException;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.StatusMessage;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.impl.ResponseBuilder;
import org.opensaml.saml2.core.impl.StatusBuilder;
import org.opensaml.saml2.core.impl.StatusCodeBuilder;
import org.opensaml.saml2.core.impl.StatusMessageBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;


/**
 * @author Valerio Venturi <valerio.venturi@cnaf.infn.it>
 *
 */
public class SAMLResponseFactory
{
  private static Logger logger = Logger.getLogger(SAMLResponseFactory.class);
  
  private static X500Principal issuer;
  private static HashMap<Class, String[]> statusMapper;
  
  static 
  {
    statusMapper = new HashMap<Class, String[]>();
    statusMapper.put(VersionMismatchException.class, new String[] {StatusCode.VERSION_MISMATCH_URI});
    statusMapper.put(UnknownAttributeException.class, new String[] {StatusCode.REQUESTER_URI, StatusCode.UNKNOWN_ATTR_PROFILE_URI});
    statusMapper.put(UnknownPrincipalException.class, new String[] {StatusCode.REQUESTER_URI, StatusCode.UNKNOWN_PRINCIPAL_URI});
    statusMapper.put(UnauthorizedQueryException.class, new String[] {StatusCode.REQUESTER_URI, StatusCode.REQUEST_DENIED_URI});
    statusMapper.put(X509SubjectWrongNameIDValueException.class, new String[] {StatusCode.REQUESTER_URI});
    statusMapper.put(X509SubjectWrongNameIDFormatException.class, new String[] {StatusCode.REQUESTER_URI});
    statusMapper.put(IssuerPeerMismatchException.class, new String[] {StatusCode.REQUESTER_URI});
    statusMapper.put(SuspendedCertificateException.class, new String[]{StatusCode.REQUESTER_URI, StatusCode.REQUEST_DENIED_URI});
    statusMapper.put(SuspendedUserException.class, new String[]{StatusCode.REQUESTER_URI, StatusCode.REQUEST_DENIED_URI});
    statusMapper.put(Exception.class, new String[] {StatusCode.RESPONDER_URI});
  }
  
  public SAMLResponseFactory(X500Principal principal)
  {
    logger.info("Configuring ResponseFactory with issuer " + principal.getName());
    issuer = principal;    
  }
  
  private Response create(String queryId)
  {
    XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
    
    ResponseBuilder responseBuilder = 
      (ResponseBuilder)builderFactory.getBuilder(Response.DEFAULT_ELEMENT_NAME);
    Response response = responseBuilder.buildObject();

    /* set some attributes */
    
    response.setID("_" + UUID.randomUUID().toString());  
    response.setIssueInstant(new DateTime());
    response.setInResponseTo(queryId);
    
    /* set Issuer */
    
    IssuerBuilder issuerBuilder = (IssuerBuilder) builderFactory.getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
    Issuer responseIssuer = issuerBuilder.buildObject();
    
    responseIssuer.setFormat(Issuer.X509_SUBJECT);
    responseIssuer.setValue(issuer.getName());
    response.setIssuer(responseIssuer);
    
    return response;
  }
  
  public Response create(String queryId, Assertion assertion)
  {
    Response response = create(queryId);
    
    XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
        
    /* set Status */
    
    logger.debug("Setting Response status to " + StatusCode.SUCCESS_URI);
    
    StatusBuilder statusBuilder = (StatusBuilder) builderFactory.getBuilder(Status.DEFAULT_ELEMENT_NAME);
    Status status = statusBuilder.buildObject();

    StatusCodeBuilder statusCodeBuilder = (StatusCodeBuilder) builderFactory.getBuilder(StatusCode.DEFAULT_ELEMENT_NAME);
    StatusCode statusCode = statusCodeBuilder.buildObject();
    statusCode.setValue(StatusCode.SUCCESS_URI);

    status.setStatusCode(statusCode);

    response.setStatus(status);

    /* set assertion */
    
    response.getAssertions().add(assertion);
    
    return response;
  }  
  
  public Response create(String queryId, Throwable exception)
  {
    Response response = create(queryId);
    
    XMLObjectBuilderFactory builderFactory = org.opensaml.xml.Configuration.getBuilderFactory();
    
    /* set Status */ 
    
    logger.debug("Setting Response status");
    
    StatusBuilder statusBuilder =
      (StatusBuilder) builderFactory.getBuilder(Status.DEFAULT_ELEMENT_NAME);
    Status status = statusBuilder.buildObject();
   
    StatusCodeBuilder statusCodeBuilder = 
      (StatusCodeBuilder) builderFactory.getBuilder(StatusCode.DEFAULT_ELEMENT_NAME);  
    StatusCode statusCode = statusCodeBuilder.buildObject();
    StatusCode subStatusCode = statusCodeBuilder.buildObject();

    String[] mapped = statusMapper.get(exception.getClass());  
    
    if(mapped != null)
    {
      statusCode.setValue(mapped[0]);
      if(mapped.length == 2)
      {
        subStatusCode.setValue(mapped[1]);
        statusCode.setStatusCode(subStatusCode);
      }
    }
    else statusCode.setValue(StatusCode.RESPONDER_URI);
    
    if(exception.getMessage() != null)
    {
      StatusMessageBuilder statusMessageBuilder = 
        (StatusMessageBuilder) builderFactory.getBuilder(StatusMessage.DEFAULT_ELEMENT_NAME); 
      StatusMessage statusMessage = statusMessageBuilder.buildObject();
      statusMessage.setMessage(exception.getMessage());
      status.setStatusMessage(statusMessage);
    }
    
    status.setStatusCode(statusCode);
    
    response.setStatus(status);
    
    return response;
  }
  
}
