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

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.configuration.VOMSConfigurationException;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.error.VOMSSyntaxException;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.error.AlreadyExistsException;
import org.glite.security.voms.admin.persistence.error.IllegalOperationException;
import org.glite.security.voms.admin.persistence.error.NoSuchAttributeException;
import org.glite.security.voms.admin.persistence.error.NoSuchGroupException;
import org.glite.security.voms.admin.persistence.error.VOMSDatabaseException;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.attribute.VOMSAttributeDescription;
import org.glite.security.voms.admin.persistence.model.attribute.VOMSGroupAttribute;
import org.glite.security.voms.admin.util.PathNamingScheme;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VOMSGroupDAO {

  public static final Logger log = LoggerFactory.getLogger(VOMSGroupDAO.class);

  private VOMSGroupDAO() {

    HibernateFactory.beginTransaction();
  }

  public List<VOMSGroup> getAll() {

    String query = "from VOMSGroup order by name asc";

    Query q = HibernateFactory.getSession().createQuery(query);
    @SuppressWarnings("unchecked")
    List<VOMSGroup> res = q.list();

    return DAOUtils.filterUnvisibleGroups(res);

  }

  public int countMatches(String searchString) {

    String sString = "%" + searchString + "%";

    String cQueryString =
        "select count(*) from org.glite.security.voms.admin.persistence.model.VOMSGroup where name like :searchString";

    Long totalMatches = (Long) HibernateFactory.getSession()
      .createQuery(cQueryString)
      .setString("searchString", sString)
      .uniqueResult();

    return totalMatches.intValue();

  }

  public int countGroups() {

    Long count = (Long) HibernateFactory.getSession()
      .createQuery("select count(*) from org.glite.security.voms.admin.persistence.model.VOMSGroup")
      .uniqueResult();

    return count.intValue();

  }

  public List<VOMSGroup> findAllWithoutRootGroup() {

    String query = "from VOMSGroup g where g != g.parent";
    Query q = HibernateFactory.getSession().createQuery(query);

    return q.list();
  }

  public List<VOMSGroup> findAll() {

    String query = "from org.glite.security.voms.admin.persistence.model.VOMSGroup";

    Query q = HibernateFactory.getSession().createQuery(query);

    return q.list();

  }

  public SearchResults getAll(int firstResult, int maxResults) {

    SearchResults results = SearchResults.instance();

    String query =
        "from org.glite.security.voms.admin.persistence.model.VOMSGroup order by name asc";

    Query q = HibernateFactory.getSession().createQuery(query);

    q.setFirstResult(firstResult);
    q.setMaxResults(maxResults);

    List<VOMSGroup> res = DAOUtils.filterUnvisibleGroups(q.list());

    results.setCount(countGroups());
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
    String queryString =
        "from org.glite.security.voms.admin.persistence.model.VOMSGroup where name like :searchString order by name asc";

    Query q =
        HibernateFactory.getSession().createQuery(queryString).setString("searchString", sString);

    q.setFirstResult(firstResult);
    q.setMaxResults(maxResults);

    List<VOMSGroup> res = DAOUtils.filterUnvisibleGroups(q.list());

    results.setSearchString(searchString);
    results.setResults(res);
    results.setCount(countMatches(searchString));
    results.setFirstResult(firstResult);
    results.setResultsPerPage(maxResults);

    return results;

  }

  public SearchResults getMembers(VOMSGroup g, int firstResult, int maxResults) {
    
    String commonQuery =
        "from VOMSMapping m where m.group = :group and m.role is null";

    Query q = HibernateFactory.getSession()
      .createQuery(String.format("select distinct m.user as user %s", commonQuery))
      .setFirstResult(firstResult)
      .setMaxResults(maxResults);
    
    Query count = HibernateFactory.getSession()
        .createQuery(String.format("select count(distinct m.user) %s", commonQuery));

    q.setEntity("group", g);
    count.setEntity("group", g);
    return paginatedFind(q, count, null, firstResult, maxResults);

  }

  public int countMatchingMembers(VOMSGroup g, String searchString) {

    if (g == null)
      throw new NullArgumentException("Cannot search members in a null group!");

    String sString = "%" + searchString + "%";

    String queryString =
        "select count(m.user) as u from VOMSMapping m join m.user.certificates as cert where "
            + "m.group = :group and m.role is null and " + "(cert.subjectString like :searchString "
            + "or cert.ca.subjectString like :searchString " + "or m.user.name like :searchString "
            + "or m.user.surname like :searchString " + "or m.user.emailAddress like :searchString "
            + "or m.user.institution like :searchString)";

    Query q =
        HibernateFactory.getSession().createQuery(queryString).setString("searchString", sString);

    q.setEntity("group", g);

    return ((Long) q.uniqueResult()).intValue();

  }
  
  public SearchResults searchMembersBySubject(VOMSGroup g, String searchString, int firstResult,
      int maxResults) {

    if (g == null)
      throw new NullArgumentException("Cannot search members in a null group!");

    if (searchString == null || searchString.equals("") || searchString.length() == 0) {
      return getMembers(g, firstResult, maxResults);
    }
    String sString = "%" + searchString + "%";

    String commonQuery = "from VOMSMapping m join m.user.certificates as cert where "
        + "m.group = :group and m.role is null and " + "(cert.subjectString like :searchString "
        + "or cert.ca.subjectString like :searchString )";

    String query = String.format("select distinct m.user as u %s", commonQuery);
    String countQuery = String.format("select count(distinct m.user ) %s", commonQuery);
    
    Query q =
        HibernateFactory.getSession().createQuery(query).setString("searchString", sString);
    
    Query count = 
        HibernateFactory.getSession().createQuery(countQuery).setString("searchString", sString);
    
    q.setEntity("group", g);
    count.setEntity("group", g);
    
    return paginatedFind(q, count, searchString, firstResult, maxResults);

  }

  public SearchResults searchMembers(VOMSGroup g, String searchString, int firstResult,
      int maxResults) {

    if (g == null)
      throw new NullArgumentException("Cannot search members in a null group!");

    if (searchString == null || searchString.equals("") || searchString.length() == 0) {
      return getMembers(g, firstResult, maxResults);
    }
    
    String sString = "%" + searchString + "%";
    
    String commonString = "from VOMSMapping m join m.user.certificates as cert"
        + " where m.group = :group and m.role is null and"
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
    
    count.setEntity("group", g);
    
    return paginatedFind(q, count, searchString, firstResult, maxResults);
  }

  public VOMSGroup findByName(String name) {

    if (!PathNamingScheme.isGroup(name))
      throw new VOMSSyntaxException("Syntax error in group name: " + name);

    String query =
        "from org.glite.security.voms.admin.persistence.model.VOMSGroup as g where g.name =:groupName";

    Query q = HibernateFactory.getSession().createQuery(query);

    q.setString("groupName", name);

    VOMSGroup g = (VOMSGroup) q.uniqueResult();
    return g;

  }

  public VOMSGroup findById(Long id) {

    VOMSGroup g = (VOMSGroup) HibernateFactory.getSession().get(VOMSGroup.class, id);

    return g;

  }

  public VOMSGroup getVOGroup() {

    VOMSConfiguration conf = VOMSConfiguration.instance();

    String voName = conf.getString(VOMSConfigurationConstants.VO_NAME);

    if (voName == null)
      throw new VOMSConfigurationException(
          VOMSConfigurationConstants.VO_NAME + "undefined in configuration!");

    VOMSGroup g = findByName("/" + voName);

    return g;

  }

  @SuppressWarnings("unchecked")
  public List<VOMSGroup> getChildren(VOMSGroup parentGroup) {

    String query =
        "from org.glite.security.voms.admin.persistence.model.VOMSGroup g where g.parent = :parentGroup and g != :parentGroup order by g.name";
    Query q = HibernateFactory.getSession().createQuery(query);

    q.setEntity("parentGroup", parentGroup);

    return q.list();

  }

  public VOMSGroup create(String groupName) {

    return create(groupName, null, null);
  }

  public VOMSGroup create(String groupName, String description, Boolean isRestricted) {

    if (!PathNamingScheme.isGroup(groupName))
      throw new VOMSSyntaxException("Syntax error in group name: " + groupName);

    if (findByName(groupName) != null)
      throw new AlreadyExistsException("Group \"" + groupName + "\" already defined!");

    String[] parentGroupChain = PathNamingScheme.getParentGroupChain(groupName);
    String rootGroupName = getVOGroup().getName();
    // The root group of the parent chain must be the root group defined for
    // the VO
    if (!parentGroupChain[0].equals(rootGroupName))
      throw new IllegalArgumentException("The root group for \"" + groupName
          + "\" must match the root group of the VO (i.e., " + rootGroupName + ")");

    String parentGroupName = PathNamingScheme.getParentGroupName(groupName);

    VOMSGroup parentGroup = findByName(parentGroupName);

    if (parentGroup == null)
      throw new NoSuchGroupException(
          "Parent group \"" + parentGroupName + "\" not defined in database!");

    VOMSGroup newGroup = new VOMSGroup();
    newGroup.setName(groupName);
    newGroup.setParent(parentGroup);

    if (description != null)
      newGroup.setDescription(description);

    if (isRestricted != null)
      newGroup.setRestricted(isRestricted);

    log.debug("Creating group \"" + newGroup + "\".");

    HibernateFactory.getSession().save(newGroup);
    return newGroup;

  }

  public void delete(Long id) {

    VOMSGroup g = (VOMSGroup) findById(id);
    if (g == null)
      throw new NoSuchGroupException("Group with id \"" + id + "\" not defined!");

    try {
      delete(g);
    } catch (ObjectNotFoundException e) {

      throw new NoSuchGroupException("Group with id \"" + id + "\" not defined!");
    }

  }

  private void uncheckedDelete(VOMSGroup g) {

    if (!g.isRootGroup()) {
      g.getMappings().clear();
      g.getAttributes().clear();
      HibernateFactory.getSession().delete(g);
    }

  }

  public void deleteAll() {

    Iterator groups = getAll().iterator();
    while (groups.hasNext())
      uncheckedDelete((VOMSGroup) groups.next());

  }

  public void delete(VOMSGroup g) {

    if (!PathNamingScheme.isGroup(g.getName()))
      throw new VOMSSyntaxException("Syntax error in group name for group: " + g);

    if (g.isRootGroup() && g.equals(getVOGroup()))
      throw new IllegalOperationException(
          "VO root group \"" + g.getName() + "\" cannot be deleted!");

    if (findByName(g.getName()) == null)
      throw new NoSuchGroupException("Group \"" + g + "\" not defined!");

    List children = getChildren(g);

    if (!children.isEmpty())
      throw new IllegalOperationException("The group \"" + g
          + "\" cannot be deleted since it contains subgroups. Delete subgroups first.");

    log.debug("Deleting group " + g + "\".");

    VOMSRoleDAO.instance().removeRoleAttributesForGroup(g);

    g.getMappings().clear();

    g.getAcls().clear();

    // Delete admins and ACL permissions that are related to this group
    VOMSAdminDAO adminDAO = VOMSAdminDAO.instance();
    VOMSAdmin groupAdmin = adminDAO.getByFQAN(g.getName());

    if (groupAdmin != null) {

      ACLDAO.instance().deletePermissionsForAdmin(groupAdmin);
      adminDAO.delete(groupAdmin);
    }

    HibernateFactory.getSession().delete(g);
    HibernateFactory.getSession().flush();

  }

  public VOMSGroup createVOGroup() {

    VOMSConfiguration conf = VOMSConfiguration.instance();

    String voName = conf.getVOName();

    if (voName == null)
      throw new VOMSConfigurationException(
          VOMSConfigurationConstants.VO_NAME + " undefined in configuration!");

    VOMSGroup g = findByName("/" + voName);

    if (g != null)
      throw new VOMSDatabaseException("Root group already defined for vo " + voName);

    g = new VOMSGroup();
    g.setName("/" + voName);
    g.setId(new Long(1));

    try {

      // The ugly transaction management code here is acceptable since
      // the creation of the VO GROUP is performed only once during the
      // lifetime of a VO.
      HibernateFactory.getSession().save(g);
      HibernateFactory.commitTransaction();

      HibernateFactory.beginTransaction();
      g.setParent(g);
      HibernateFactory.getSession().update(g);
      HibernateFactory.commitTransaction();

    } catch (HibernateException e) {

      throw new VOMSDatabaseException("Failed creation of VO root group!", e);
    } finally {

      HibernateFactory.commitTransaction();
    }

    return g;

  }

  public VOMSGroupAttribute setAttribute(VOMSGroup g, String attrName, String attrValue) {

    VOMSAttributeDescription desc =
        VOMSAttributeDAO.instance().getAttributeDescriptionByName(attrName);

    if (desc == null)
      throw new NoSuchAttributeException("Attribute '" + attrName + "' is not defined in this vo.");

    VOMSGroupAttribute val = g.getAttributeByName(attrName);

    if (val != null)
      val.setValue(attrValue);

    else {
      val = VOMSGroupAttribute.instance(desc, attrValue, g);
      g.addAttribute(val);
    }

    HibernateFactory.getSession().update(g);

    return val;

  }

  public VOMSGroupAttribute createAttribute(VOMSGroup g, String attrName, String attrDesc,
      String attrValue) {

    if (g.getAttributeByName(attrName) != null)
      throw new AlreadyExistsException(
          "Attribute \"" + attrName + "\" already defined for group \"" + g + "\".");

    VOMSAttributeDescription desc =
        VOMSAttributeDAO.instance().getAttributeDescriptionByName(attrName);

    if (desc == null)
      desc = VOMSAttributeDAO.instance().createAttributeDescription(attrName, attrDesc);
    log
      .debug("Creating attribute \"(" + attrName + "," + attrValue + ")\" for group \"" + g + "\"");
    VOMSGroupAttribute val = VOMSGroupAttribute.instance(desc, attrValue, g);
    g.addAttribute(val);

    return val;

  }

  public VOMSGroupAttribute deleteAttribute(VOMSGroup g, String attrName) {

    VOMSGroupAttribute ga = g.getAttributeByName(attrName);

    if (ga == null)
      throw new NoSuchAttributeException(
          "Attribute named '" + attrName + "' not defined for group '" + g.getName() + "'!");

    deleteAttribute(g, ga);

    return ga;

  }

  public void deleteAttribute(VOMSGroup g, VOMSGroupAttribute val) {

    log.debug("Deleting attribute \"" + val.getName() + "\" from group \"" + g + "\"");

    g.deleteAttribute(val);

  }

  public boolean isVOGroup(VOMSGroup g) {

    return (g.equals(getVOGroup()));
  }

  public static VOMSGroupDAO instance() {

    return new VOMSGroupDAO();
  }

  @SuppressWarnings("unchecked")
  public List<VOMSUser> getMembers(VOMSGroup g) {

    if (g == null)
      throw new NullArgumentException("Cannot get members of a null group!");

    String query = "select m.user from VOMSMapping m where m.group = :group and m.role is null";
    return HibernateFactory.getSession().createQuery(query).setEntity("group", g).list();

  }

  @SuppressWarnings("unchecked")
  public List<String> getMemberSubjectStrings(VOMSGroup g) {

    if (g == null)
      throw new NullArgumentException("Cannot get members of a null group!");

    String query =
        "select distinct c.subjectString from VOMSUser u join u.certificates c join u.mappings m where u.suspended is false and c.suspended is false and m.group =  :group and m.role is null";

    return HibernateFactory.getSession().createQuery(query).setEntity("group", g).list();
  }
}
