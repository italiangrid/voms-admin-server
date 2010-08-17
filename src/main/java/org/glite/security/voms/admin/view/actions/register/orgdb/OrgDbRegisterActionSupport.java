package org.glite.security.voms.admin.view.actions.register.orgdb;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.glite.security.voms.admin.error.IllegalStateException;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBDAOFactory;
import org.glite.security.voms.admin.integration.orgdb.model.VOMSPerson;
import org.glite.security.voms.admin.view.actions.register.RegisterActionSupport;

public class OrgDbRegisterActionSupport extends RegisterActionSupport implements SessionAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String ORGDB_RECORD_SESSION_KEY = "___voms.user-orgdb-record";
	
	Long vomsPersonId;
	
	Map<String, Object> session;
	
	protected void populateRequesterFromOrgdb(){
		
		if (requester == null)
			requester = requesterInfoFromCurrentAdmin();
		
		if (session == null)
			throw new IllegalStateException("Session is null for this user!");
		
		VOMSPerson orgDbPerson = (VOMSPerson) session.get(ORGDB_RECORD_SESSION_KEY);
		
		if (orgDbPerson == null){
			
			if (vomsPersonId == null){
				throw new IllegalArgumentException("No id provided for the VOMS person orgdb record and no record found in session!");
			
			}else{
	
				orgDbPerson = OrgDBDAOFactory.instance().getVOMSPersonDAO().findById(vomsPersonId, false);
				if (orgDbPerson == null){
					throw new IllegalArgumentException("No orgb record found for id '"+vomsPersonId+"'.");
				}
				
				session.put(ORGDB_RECORD_SESSION_KEY, orgDbPerson);
			}
			
		}
		
		requester.setName(orgDbPerson.getFirstName());
		requester.setSurname(orgDbPerson.getName());
		requester.setEmailAddress(orgDbPerson.getPhysicalEmail());
		
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		populateRequesterFromOrgdb();
	}
	
	public Long getVomsPersonId() {
		return vomsPersonId;
	}
	
	public void setVomsPersonId(Long vomsPersonId) {
		this.vomsPersonId = vomsPersonId;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
		
	}

}
