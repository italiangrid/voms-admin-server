package org.glite.security.voms.admin.persistence.dao.generic;

import org.glite.security.voms.admin.persistence.model.GroupManager;


public interface GroupManagerDAO extends GenericDAO<GroupManager, Long>{
	
	public GroupManager findByName(String name);
	
}
