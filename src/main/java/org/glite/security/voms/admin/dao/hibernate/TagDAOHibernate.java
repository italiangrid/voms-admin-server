package org.glite.security.voms.admin.dao.hibernate;

import java.util.List;

import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.dao.generic.TagDAO;
import org.glite.security.voms.admin.database.AlreadyExistsException;
import org.glite.security.voms.admin.model.Tag;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.hibernate.criterion.Restrictions;

public class TagDAOHibernate extends GenericHibernateDAO<Tag, Long> implements
		TagDAO {

	public Tag findByName(String name) {

		if (name == null)
			throw new NullArgumentException("name cannot be null!");

		List<Tag> retVal = findByCriteria(Restrictions.eq("name", name));

		if (retVal.isEmpty())
			return null;

		return retVal.get(0);

	}

	public Tag createTag(String name, VOMSPermission perm,
			VOMSPermission permOnPath, boolean implicit) {

		if (name == null)
			throw new NullArgumentException("name cannot be null!");

		if (perm == null)
			throw new NullArgumentException("perm cannot be null!");

		Tag t = findByName(name);

		if (t != null)
			throw new AlreadyExistsException("Tag named '" + name
					+ "' already exists.");

		t = new Tag(name, perm, permOnPath, implicit);

		getSession().save(t);
		return t;
	}

}
