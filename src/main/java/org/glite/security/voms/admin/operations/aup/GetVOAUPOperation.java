package org.glite.security.voms.admin.operations.aup;

import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.operations.BaseVoReadOperation;

public class GetVOAUPOperation extends BaseVoReadOperation {
	
	
	@Override
	protected Object doExecute() {
		AUPDAO dao = DAOFactory.instance().getAUPDAO();
		
		
		return null;
	}

}
