/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
 */
package org.glite.security.voms.admin.persistence.dao;

import static org.glite.security.voms.admin.persistence.dao.SearchUtils.paginatedFind;

import java.util.Iterator;
import java.util.List;

import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.error.AlreadyExistsException;
import org.glite.security.voms.admin.persistence.error.NoSuchAttributeException;
import org.glite.security.voms.admin.persistence.error.NoSuchRoleException;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.attribute.VOMSAttributeDescription;
import org.glite.security.voms.admin.persistence.model.attribute.VOMSRoleAttribute;
import org.hibernate.Query;

public class VOMSRoleDAO {

  protected VOMSRoleDAO() {

    HibernateFactory.beginTransaction();
  }

  public List<VOMSRole> findAll() {

    return getAll();
  }

  @SuppressWarnings("unchecked")
  public List<VOMSRole> getAll() {

    String query = "from org.glite.security.voms.admin.persistence.model.VOMSRole";

    return HibernateFactory.getSession().createQuery(query).list();

  }

  public int countRoles() {

    String query = "select count(*) from org.glite.security.voms.admin.persistence.model.VOMSRole";

    Long count = (Long) HibernateFactory.getSession().createQuery(query).uniqueResult();

    return count.intValue();
  }

  public int countMatches(String searchString) {

    String sString = "%" + searchString + "%";
    String query =
        "select count(*) from org.glite.security.voms.admin.persistence.model.VOMSRole where name like :searchString";

    Long count = (Long) HibernateFactory.getSession()
      .createQuery(query)
      .setString("searchString", sString)
      .uniqueResult();

    return count.intValue();
  }

  public SearchResults getAll(int firstResult, int maxResults) {

    SearchResults results = SearchResults.instance();

    String query = "from org.glite.security.voms.admin.persistence.model.VOMSRole";
    Query q = HibernateFactory.getSession().createQuery(query);

    q.setFirstResult(firstResult);
    q.setMaxResults(maxResults);

    List res = q.list();

    results.setCount(countRoles());
    results.setFirstResult(firstResult);
    results.setResultsPerPage(maxResults);
    results.setResults(res);
    return results;

  }

  public SearchResults search(String searchString, int firstResult, int maxResults) {

    if (searchString == null || searchString.equals("") || searchString.length() == 0)
      return getAll(firstResult, maxResults);

    SearchResults results = SearchResults.instance();

    String sString = "%" + searchString + "%";
    String query =
        "from org.glite.security.voms.admin.persistence.model.VOMSRole where name like :searchString";

    Query q = HibernateFactory.getSession().createQuery(query).setString("searchString", sString);

    q.setFirstResult(firstResult);
    q.setMaxResults(maxResults);

    List res = q.list();

    results.setCount(countMatches(searchString));
    results.setFirstResult(firstResult);
    results.setResultsPerPage(maxResults);
    results.setSearchString(searchString);
    results.setResults(res);

    return results;
  }

  public SearchResults searchMemberBySubject(VOMSGroup g, VOMSRole r, String searchString,
      int firstResult, int maxResults) {

    if (g == null)
      throw new NullArgumentException("Cannot search members in a null group!");

    if (r == null)
      throw new NullArgumentException("Cannot search members in a null role!");

    if (searchString == null || searchString.equals("") || searchString.length() == 0)
      return getMembers(g, r, firstResult, maxResults);

    String sString = "%" + searchString + "%";

    String commonString =
        "from VOMSUser u join u.certificates c join u.mappings m where m.group =  :group and m.role = :role and"
            + "(c.subjectString like :searchString or c.ca.subjectString like :searchString)";

    Query q = HibernateFactory.getSession()
      .createQuery(String.format("select distinct u %s", commonString))
      .setString("searchString", sString);

    Query count = HibernateFactory.getSession()
      .createQuery(String.format("select count(distinct u) %s", commonString))
      .setString("searchString", sString);

    q.setEntity("group", g);
    q.setEntity("role", r);


    count.setEntity("group", g);
    count.setEntity("role", r);

    return paginatedFind(q, count, searchString, firstResult, maxResults);
  }

  public SearchResults searchMembers(VOMSGroup g, VOMSRole r, String searchString, int firstResult,
      int maxResults) {

    if (g == null)
      throw new NullArgumentException("Cannot search members in a null group!");

    if (r == null)
      throw new NullArgumentException("Cannot search members in a null role!");

    if (searchString == null || searchString.equals("") || searchString.length() == 0)
      return getMembers(g, r, firstResult, maxResults);

    String sString = "%" + searchString + "%";

    String commonString = "from VOMSMapping m join m.user.certificates as cert"
        + " where m.group = :group and m.role is :role and"
        + " (cert.subjectString like :searchString "
        + "or cert.ca.subjectString like :searchString " + "or m.user.name like :searchString "
        + "or m.user.surname like :searchString " + "or m.user.emailAddress like :searchString "
        + "or m.user.institution like :searchString)";
    
    Query q = HibernateFactory.getSession()
      .createQuery(String.format("select distinct m.user as user %s", commonString))
      .setString("searchString", sString);

    Query count = HibernateFactory.getSession()
      .createQuery(String.format("select count(distinct m.user) %s", commonString))
      .setString("searchString", sString);

    q.setEntity("group", g);
    q.setEntity("role", r);

    count.setEntity("group", g);
    count.setEntity("role", r);

    return paginatedFind(q, count, searchString, firstResult, maxResults);

  }

  private SearchResults getMembers(VOMSGroup g, VOMSRole r, int firstResult, int maxResults) {

    if (g == null)
      throw new NullArgumentException("Cannot search members in a null group!");

    if (r == null)
      throw new NullArgumentException("Cannot search members in a null role!");

    String commonQuery = "from VOMSMapping m where m.group = :group and m.role is :role ";

    Query q = HibernateFactory.getSession()
      .createQuery(String.format("select m.user as user %s", commonQuery));
    Query count = HibernateFactory.getSession()
      .createQuery(String.format("select count(distinct m.user ) %s", commonQuery));

    q.setEntity("group", g);
    q.setEntity("role", r);
    count.setEntity("group", g);
    count.setEntity("role", r);

    return SearchUtils.paginatedFind(q, count, null, firstResult, maxResults);

  }

  public List getAllNames() {

    String query = "select name from org.glite.security.voms.admin.persistence.model.VOMSRole";
    return HibernateFactory.getSession().createQuery(query).list();

  }

  public VOMSRole findByName(String roleName) {

    String query =
        "from org.glite.security.voms.admin.persistence.model.VOMSRole where name = :name";

    return (VOMSRole) HibernateFactory.getSession()
      .createQuery(query)
      .setString("name", roleName)
      .uniqueResult();
  }

  public VOMSRole findById(Long id) {

    return (VOMSRole) HibernateFactory.getSession().load(VOMSRole.class, id);
  }

  public VOMSRole create(String roleName) {

    if (findByName(roleName) != null)
      throw new AlreadyExistsException("Role \"" + roleName + "\" already defined in database!");

    VOMSRole r = new VOMSRole(roleName);

    HibernateFactory.getSession().save(r);

    return r;

  }

  public void deleteAll() {

    HibernateFactory.getSession()
      .createQuery("delete from org.glite.security.voms.admin.persistence.model.VOMSRole")
      .executeUpdate();

  }

  public VOMSRole delete(Long id) {

    VOMSRole r = findById(id);

    if (r == null)
      throw new NoSuchRoleException("Role with id \"" + id + "\" is not defined in database!");
    delete(r);

    return r;
  }

  public VOMSRole delete(String roleName) {

    VOMSRole r = findByName(roleName);
    if (r == null)
      throw new NoSuchRoleException("Role '" + roleName + "' is not defined in database!");

    delete(r);

    return r;

  }

  public VOMSRole delete(VOMSRole r) {

    if (findByName(r.getName()) == null)
      throw new NoSuchRoleException("Role \"" + r + "\" is not defined in database!");

    r.getMappings().clear();

    Iterator aclIter = r.getAcls().iterator();

    while (aclIter.hasNext()) {

      ACL acl = (ACL) aclIter.next();
      VOMSGroup g = acl.getGroup();
      g.getAcls().remove(acl);
      aclIter.remove();
    }

    // Delete permissions from ACLs that may be related with this role
    ACLDAO.instance().deletePermissionsForRole(r);

    // Delete role admins
    VOMSAdminDAO.instance().deleteRoleAdmins(r);

    HibernateFactory.getSession().delete(r);

    return r;

  }

  public void removeRoleAttributesForGroup(VOMSGroup g) {

    String deleteString = "delete from VOMSRoleAttribute where group = :group";

    HibernateFactory.getSession().createQuery(deleteString).setEntity("group", g).executeUpdate();

  }

  public VOMSRoleAttribute setAttribute(VOMSRole r, VOMSGroup g, String attrName,
      String attrValue) {

    VOMSAttributeDescription desc =
        VOMSAttributeDAO.instance().getAttributeDescriptionByName(attrName);

    if (desc == null)
      throw new NoSuchAttributeException("Attribute '" + attrName + "' is not defined in this vo.");

    VOMSRoleAttribute val = r.getAttributeByName(g, attrName);

    if (val != null)
      val.setValue(attrValue);
    else {
      val = VOMSRoleAttribute.instance(desc, attrValue, g, r);
      r.addAttribute(val);
    }

    HibernateFactory.getSession().update(r);
    return val;

  }

  public VOMSRoleAttribute createAttribute(VOMSRole r, VOMSGroup g, String attrName,
      String attrDesc, String attrValue) {

    if (r.getAttributeByName(g, attrName) != null)
      throw new AlreadyExistsException("Attribute \"" + attrName + "\" already defined for role \""
          + r + "\" in group \"" + g + "\".");

    VOMSAttributeDescription desc =
        VOMSAttributeDAO.instance().getAttributeDescriptionByName(attrName);

    if (desc == null)
      desc = VOMSAttributeDAO.instance().createAttributeDescription(attrName, attrDesc);

    VOMSRoleAttribute val = VOMSRoleAttribute.instance(desc, attrValue, g, r);

    r.addAttribute(val);

    return val;

  }

  public VOMSRoleAttribute deleteAttributeByName(VOMSRole r, VOMSGroup g, String attrName) {

    VOMSRoleAttribute attr = r.getAttributeByName(g, attrName);

    if (attr == null)
      throw new NoSuchAttributeException("Attribute '" + attrName + "' not defined for role '"
          + r.getName() + "' in group '" + g + "'.");

    deleteAttribute(r, attr);

    return attr;

  }

  public void deleteAttribute(VOMSRole r, VOMSRoleAttribute val) {

    r.deleteAttribute(val);
    HibernateFactory.getSession().update(r);

  }

  public List getMembers(VOMSGroup g, VOMSRole r) {

    if (g == null)
      throw new IllegalArgumentException("group parameter must be non-null!");

    if (r == null)
      throw new IllegalArgumentException("role parameter must be non-null!");

    String query =
        "select m.user from org.glite.security.voms.admin.persistence.model.VOMSMapping m where m.group = :group and m.role = :role";
    return HibernateFactory.getSession()
      .createQuery(query)
      .setEntity("group", g)
      .setEntity("role", r)
      .list();

  }

  public static VOMSRoleDAO instance() {

    return new VOMSRoleDAO();
  }

  public Object getMemberSubjectStrings(VOMSGroup g, VOMSRole r) {

    if (g == null)
      throw new IllegalArgumentException("group parameter must be non-null!");

    if (r == null)
      throw new IllegalArgumentException("role parameter must be non-null!");

    String query =
        "select distinct c.subjectString from VOMSUser u join u.certificates c join u.mappings m where u.suspended is false and c.suspended is false and m.group =  :group and m.role = :role";

    return HibernateFactory.getSession()
      .createQuery(query)
      .setEntity("group", g)
      .setEntity("role", r)
      .list();
  }
}
