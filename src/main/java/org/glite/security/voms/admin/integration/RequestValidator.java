package org.glite.security.voms.admin.integration;

import java.util.List;

import org.glite.security.voms.admin.persistence.model.request.Request;

public interface RequestValidator<RequestType extends Request> {
	
	public void validateRequest(RequestType r) throws RequestValidationException;
	public void validateRequests(List<RequestType> requests) throws RequestValidationException;

}
