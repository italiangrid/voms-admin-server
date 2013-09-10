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

package org.italiangrid.voms.aa.impl;

import java.util.Calendar;
import java.util.Date;

import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.italiangrid.voms.aa.AttributeAuthority;
import org.italiangrid.voms.aa.RequestContext;
import org.italiangrid.voms.aa.VOMSErrorMessage;
import org.italiangrid.voms.aa.VOMSRequest;
import org.italiangrid.voms.aa.VOMSResponse.Outcome;
import org.italiangrid.voms.aa.VOMSWarningMessage;

public class AAImpl implements AttributeAuthority {

	private final AttributeResolver attributeResolver;
	private final long maxAttrValidityInSecs; 
	
	public AAImpl(AttributeResolver resolver, 
		long maxAttrValidityInSecs) {
		  this.attributeResolver = resolver;
		  this.maxAttrValidityInSecs = maxAttrValidityInSecs;
	}
		
	private void resolveFQANs(RequestContext context){
		attributeResolver.resolveFQANs(context);
	}
	
	private void resolveGAs(RequestContext context){
		attributeResolver.resolveGAs(context);
	}
	
	private void handleRequestedValidity(RequestContext context){
				
		long validity = maxAttrValidityInSecs;
		long requestedValidity = context.getRequest().getRequestedValidity();
		
		if (requestedValidity > 0 && requestedValidity < maxAttrValidityInSecs)
			validity = requestedValidity;
		
		if (requestedValidity > maxAttrValidityInSecs){
			context.getResponse().getWarnings().add(
				VOMSWarningMessage.ShortenedAttributeValidity);
		}
		
		Calendar cal = Calendar.getInstance();
		Date startDate = cal.getTime();		

		cal.add(Calendar.SECOND, (int)validity);

		Date endDate = cal.getTime();
		
		context.getResponse().setNotAfter(endDate);
		context.getResponse().setNotBefore(startDate);
	}
	
	private void handleTargets(RequestContext context){
		// TODO: Check that targets are actually valid hostnames
		// or IP addresses
		context.getResponse().setTargets(context.getRequest().getTargets());
	}
	
	protected void requestSanityChecks(VOMSRequest request){
		if (request == null)
			throw new NullPointerException("Cannot handle a null request!");
		
		if (request.getRequesterSubject() == null)
			throw new NullPointerException("Requester subject cannot be null!");
		
		if (request.getHolderSubject() == null)
			throw new NullPointerException("Holder subject cannot be null!");
	}
	
	@Override
	public boolean getAttributes(RequestContext context) {
		
		requestSanityChecks(context.getRequest());
		
		authorize(context);
		
		if (!context.isHandled())
			resolveUser(context);
		
		if (!context.isHandled())
			checkMembershipValidity(context);
		
		if (!context.isHandled())
			handleRequestedValidity(context);
		
		if (!context.isHandled())
			handleTargets(context);
		
		if (!context.isHandled())
			resolveFQANs(context);
		
		if (!context.isHandled())
			resolveGAs(context);
		
		context.setHandled(true);
		
		return (context.getResponse().getOutcome() == Outcome.SUCCESS);
	}
	
	
	protected void authorize(RequestContext context) {		
		// TODO: check if requester is authorized in requesting
		// attributes for holder
	}
	
	protected void checkMembershipValidity(RequestContext context) {

		VOMSUser u = context.getVOMSUser();
		VOMSRequest r = context.getRequest();
		
		if (u.isSuspended()){
			failResponse(context, VOMSErrorMessage.suspendedUser(r.getHolderSubject(), 
				r.getHolderIssuer(), u.getSuspensionReason()));
			context.setHandled(true);
			return;
		}
		
		Certificate cert = u.getCertificate(r.getHolderSubject(), 
			r.getHolderIssuer());
		
		if (cert.isSuspended()){
			failResponse(context, 
				VOMSErrorMessage.suspendedCertificate(cert.getSubjectString(), 
				cert.getCa().getSubjectString(),
				cert.getSuspensionReason()));
		}
		
	}
	
	protected void failResponse(RequestContext context, VOMSErrorMessage em){
		context.getResponse().setOutcome(Outcome.FAILURE);
		context.getResponse().getErrorMessages().add(em);
	}

	private void resolveUser(RequestContext context) {
		
		VOMSRequest request = context.getRequest();
		VOMSUser user = null;
		
		if (request.getHolderIssuer() == null)
			user = VOMSUserDAO.instance().findBySubject(request.getHolderSubject());
		else
			user = VOMSUserDAO.instance().findByDNandCA(request.getHolderSubject(),
				request.getHolderIssuer());
		
		if (user == null){
			
			 VOMSErrorMessage m = VOMSErrorMessage.noSuchUser(
				 request.getHolderSubject(), 
				 request.getHolderIssuer()); 
			 
			context.getResponse().setOutcome(Outcome.FAILURE);
			context.getResponse().getErrorMessages().add(m);
			context.setHandled(true);
		
		}else{
			context.setVOMSUser(user);
		}
	}

}
