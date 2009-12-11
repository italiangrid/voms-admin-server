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
package org.glite.security.voms.admin.dao;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.database.AlreadyExistsException;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.database.NoSuchAttributeException;
import org.glite.security.voms.admin.model.VOMSAttributeDescription;
import org.glite.security.voms.admin.model.VOMSUser;
import org.hibernate.Query;

public class VOMSAttributeDAO {

	public static final Log log = LogFactory.getLog(VOMSAttributeDAO.class);

	protected VOMSAttributeDAO() {

		HibernateFactory.beginTransaction();
	}

	public static VOMSAttributeDAO instance() {

		return new VOMSAttributeDAO();
	}

	public List getAllAttributeDescriptions() {

		String query = "from org.glite.security.voms.admin.model.VOMSAttributeDescription order by name";
		return HibernateFactory.getSession().createQuery(query).list();
	}

	public VOMSAttributeDescription createAttributeDescription(String name,
			String desc, boolean unique) {

		VOMSAttributeDescription attrDesc = getAttributeDescriptionByName(name);
		if (attrDesc != null)
			throw new AlreadyExistsException("Attribute \"" + name
					+ "\" already defined in database.");

		attrDesc = new VOMSAttributeDescription(name, desc, unique);
		HibernateFactory.getSession().save(attrDesc);
		return attrDesc;
	}

	public VOMSAttributeDescription createAttributeDescription(String name,
			String desc) {

		return createAttributeDescription(name, desc, false);

	}

	public VOMSAttributeDescription deleteAttributeDescription(String name) {

		VOMSAttributeDescription attrDesc = getAttributeDescriptionByName(name);

		if (attrDesc == null)
			throw new NoSuchAttributeException("Attribute \"" + name
					+ "\" not found in database.");

		String attributeValueEntities[] = new String[] { "VOMSUserAttribute",
				"VOMSGroupAttribute", "VOMSRoleAttribute" };

		// Delete attribute value mappings!
		for (int i = 0; i < attributeValueEntities.length; i++) {
			String query = "delete from org.glite.security.voms.admin.model."
					+ attributeValueEntities[i]
					+ " where attributeDescription = :desc";
			HibernateFactory.getSession().createQuery(query).setEntity("desc",
					attrDesc).executeUpdate();

		}

		HibernateFactory.getSession().delete(attrDesc);

		return attrDesc;

	}

	public VOMSAttributeDescription getAttributeDescriptionByName(String name) {

		String query = "from org.glite.security.voms.admin.model.VOMSAttributeDescription where name = :name";

		VOMSAttributeDescription retVal = (VOMSAttributeDescription) HibernateFactory
				.getSession().createQuery(query).setString("name", name)
				.uniqueResult();

		return retVal;

	}

	public List getUserAttributes() {

		String query = "select a.attributeDescription.name, u, a.value from org.glite.security.voms.admin.model.VOMSUser u join u.attributes a";

		return HibernateFactory.getSession().createQuery(query).list();
	}

	public List getGroupAttributes() {
		String query = "select a.attributeDescription.name, g, a.value from org.glite.security.voms.admin.model.VOMSGroup g join g.attributes a";

		return HibernateFactory.getSession().createQuery(query).list();
	}

	public List getRoleAttributes() {
		String query = "select a.attributeDescription.name, r, a.value from org.glite.security.voms.admin.model.VOMSRole r join r.attributes a";

		return HibernateFactory.getSession().createQuery(query).list();
	}

	public List getUserAttributes(String attributeName) {

		VOMSAttributeDescription desc = getAttributeDescriptionByName(attributeName);

		if (desc == null)
			throw new NoSuchAttributeException("Attribute '" + attributeName
					+ "' not found in database!");

		return getUserAttributes(desc);

	}

	public List getGroupAttributes(String attributeName) {

		VOMSAttributeDescription desc = getAttributeDescriptionByName(attributeName);

		if (desc == null)
			throw new NoSuchAttributeException("Attribute '" + attributeName
					+ "' not found in database!");

		return getGroupAttributes(desc);

	}

	public List getRoleAttributes(String attributeName) {
		VOMSAttributeDescription desc = getAttributeDescriptionByName(attributeName);

		if (desc == null)
			throw new NoSuchAttributeException("Attribute '" + attributeName
					+ "' not found in database!");

		return getRoleAttributes(desc);

	}

	public List getGroupAttributes(VOMSAttributeDescription desc) {
		String query = "select g, a.value from org.glite.security.voms.admin.model.VOMSGroup g join g.attributes a"
				+ " where a.attributeDescription = :desc";

		return HibernateFactory.getSession().createQuery(query).setEntity(
				"desc", desc).list();
	}

	public List getRoleAttributes(VOMSAttributeDescription desc) {

		String query = "select r, a.group, a.value from org.glite.security.voms.admin.model.VOMSRole r join r.attributes a"
				+ " where a.attributeDescription = :desc";

		return HibernateFactory.getSession().createQuery(query).setEntity(
				"desc", desc).list();
	}

	public List getUserAttributes(VOMSAttributeDescription desc) {
		String query = "select u, a.value from org.glite.security.voms.admin.model.VOMSUser u join u.attributes a"
				+ " where a.attributeDescription = :desc";

		return HibernateFactory.getSession().createQuery(query).setEntity(
				"desc", desc).list();
	}

	public SearchResults getAllUserAttributes(int firstResult, int maxResults) {

		SearchResults results = SearchResults.instance();

		String queryString = "select a.attributeDescription.name, u, a.value from org.glite.security.voms.admin.model.VOMSUser u join u.attributes a "
				+ "order by a.attributeDescription.name,u.dn";
		Query q = HibernateFactory.getSession().createQuery(queryString);

		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);

		results.setFirstResult(firstResult);
		results.setResultsPerPage(maxResults);
		results.setCount(countUserAttributes());
		results.setResults(q.list());

		return results;
	}

	public int countUserAttributes() {

		String queryString = "select count(*) from org.glite.security.voms.admin.model.VOMSUser u join u.attributes a";

		Long count = (Long) HibernateFactory.getSession().createQuery(
				queryString).uniqueResult();

		return count.intValue();

	}

	public int countUserAttributesMatches(String searchString) {

		String queryString = "select count(*) from org.glite.security.voms.admin.model.VOMSUser u join u.attributes a "
				+ "where (a.attributeDescription.name like :searchString) or (u.dn like :searchString) or (u.ca.subjectString like :searchString) or "
				+ "(a.value like :searchString)";

		String sString = "%" + searchString + "%";
		Query q = HibernateFactory.getSession().createQuery(queryString)
				.setString("searchString", sString);

		Long count = (Long) q.uniqueResult();
		return count.intValue();

	}

	public SearchResults searchUserAttributes(String searchString,
			int firstResult, int maxResults) {

		if (searchString == null || searchString.equals("")
				|| searchString.length() == 0)
			return getAllUserAttributes(firstResult, maxResults);

		SearchResults results = SearchResults.instance();
		String sString = "%" + searchString + "%";
		String queryString = "select a.attributeDescription.name, u, a.value from org.glite.security.voms.admin.model.VOMSUser u join u.attributes a "
				+ "where (a.attributeDescription.name like :searchString) or (u.dn like :searchString) or (u.ca.subjectString like :searchString) or "
				+ "(a.value like :searchString) order by a.attributeDescription.name,u.dn";

		Query q = HibernateFactory.getSession().createQuery(queryString);

		q.setString("searchString", sString);
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);

		results.setCount(countUserAttributesMatches(searchString));
		results.setFirstResult(firstResult);
		results.setResultsPerPage(maxResults);
		results.setSearchString(searchString);
		results.setResults(q.list());

		return results;

	}

	public boolean isAttributeValueAlreadyAssigned(VOMSUser u, String attributeName, String attributeValue){
		
		return isAttributeValueAlreadyAssigned(u, getAttributeDescriptionByName(attributeName), attributeValue);
	}
	
	public boolean isAttributeValueAlreadyAssigned(VOMSUser u,
			VOMSAttributeDescription desc, String attrValue) {
		if (!desc.isUnique())
			return false;

		String queryString = "select a.value from org.glite.security.voms.admin.model.VOMSUser u join u.attributes a where a.attributeDescription = :desc "
				+ "and u != :user";

		Query q = HibernateFactory.getSession().createQuery(queryString);

		q.setEntity("desc", desc);
		q.setEntity("user", u);

		// Need to perform the check in memory since oracle has a bug (or I did
		// not understand how to manage clob equality tests).
		Iterator i = q.iterate();
		while (i.hasNext()) {
			String value = (String) i.next();

			// NULL attribute value can be assigned to multiple users, two NULLs
			// aren't equal as attribute values
			if (value == null)
				return false;

			if (value.equals(attrValue))
				return true;
		}

		return false;
	}

	public void update(VOMSAttributeDescription desc) {

		HibernateFactory.getSession().update(desc);
	}

}
