package org.glite.security.voms.admin.operations.aup;

import java.net.MalformedURLException;
import java.net.URL;

import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class AddVersionOperation extends BaseVomsOperation {

	AUP aup;
	
	String version;
	
	String url;
	
	public AddVersionOperation(AUP aup, String version, String url) {
		this.aup = aup;
		this.version = version;
		this.url = url;
	}
	@Override
	protected Object doExecute() {
		AUPDAO dao = DAOFactory.instance().getAUPDAO();

		try {
			
			dao.addVersion(aup, version, new URL(url));
		
		} catch (MalformedURLException e) {
			
			throw new VOMSException("Malformed URL passed as argument: "+url,e);
		}
		
		return aup;
	}

	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerRWPermissions().setMembershipRWPermission());
	}

}
