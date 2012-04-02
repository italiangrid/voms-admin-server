package org.glite.security.voms.admin.core.validation.strategies.impl;

import java.util.List;

import org.glite.security.voms.admin.core.validation.strategies.AUPFailingMembersLookupStrategy;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class DefaultAUPFailingMembersLookupStrategy implements AUPFailingMembersLookupStrategy{

	public List<VOMSUser> findAUPFailingMembers() {
		
		AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();
		VOMSUserDAO userDAO = VOMSUserDAO.instance();
		
		return userDAO.findAUPFailingUsers(aupDAO.getVOAUP());
		
	}

}
