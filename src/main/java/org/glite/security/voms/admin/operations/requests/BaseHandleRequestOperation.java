package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.common.IllegalRequestStateException;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.database.NoSuchRoleException;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.request.Request.STATUS;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.users.FindUserOperation;

public abstract class BaseHandleRequestOperation<T extends Request> extends BaseVomsOperation {

	protected DECISION decision = DECISION.REJECT;
	T request;
	
	public BaseHandleRequestOperation(T request, DECISION decision){
		this.request = request;
		this.decision = decision;
	}

	protected abstract void approve();

	protected void checkRequestStatus(STATUS status){
		if (!request.getStatus().equals(status))
			throw new IllegalRequestStateException(
					"Illegal state for request: " + request.getStatus());
	}
	
	
	@Override
	protected final Object doExecute() {
		
		if (decision.equals(DECISION.APPROVE))
			approve();
		else
			reject();
		
		return request;
		
	}
	

	
	protected final VOMSGroup findGroupByName(String groupName){
		VOMSGroup g  = VOMSGroupDAO.instance().findByName(groupName);
		
		if (g == null)
			throw new NoSuchGroupException("Requested group '"+groupName+"' does not exist in this VO.");
		
		return g;
	}
	
	
	protected final VOMSRole findRoleByName(String roleName){
		
		VOMSRole r = VOMSRoleDAO.instance().findByName(roleName);
		
		if (r == null)
			throw new NoSuchRoleException("Requested role '"+roleName+"' does not exist in this VO.");
		
		return r;
		
	}
	
	public DECISION getDecision() {
		return decision;
	}
	
	public T getRequest(){
		
		return request;
	}
	protected final VOMSUser getRequesterAsVomsUser(){
		
		VOMSUser u = (VOMSUser) FindUserOperation.instance(
				request.getRequesterInfo().getCertificateSubject(),
				request.getRequesterInfo().getCertificateIssuer()).execute();

		if (u == null)
			throw new NoSuchUserException(String.format(
					"User '%s, %s' not found!", request.getRequesterInfo()
							.getCertificateSubject(), request
							.getRequesterInfo().getCertificateIssuer()));
		
		return u;
	}
	
	protected abstract void reject();
		
	protected void setDecision(DECISION decision) {
		this.decision = decision;
	}
}
