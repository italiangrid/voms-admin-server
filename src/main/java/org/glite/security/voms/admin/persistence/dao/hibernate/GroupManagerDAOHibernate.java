package org.glite.security.voms.admin.persistence.dao.hibernate;

import org.glite.security.voms.admin.persistence.dao.generic.GroupManagerDAO;
import org.glite.security.voms.admin.persistence.model.GroupManager;
import org.hibernate.criterion.Restrictions;


public class GroupManagerDAOHibernate 
	extends GenericHibernateDAO<GroupManager, Long> implements GroupManagerDAO {

	@Override
	public GroupManager findByName(String name) {
	
		return findByCriteriaUniqueResult(Restrictions.eq("name", name));
	}
}
