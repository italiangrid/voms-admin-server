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

import java.util.Iterator;
import java.util.List;

import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.lookup.FindByCertificateDAO;
import org.glite.security.voms.admin.persistence.dao.lookup.LookupPolicyProvider;
import org.glite.security.voms.admin.persistence.error.NoSuchCAException;
import org.glite.security.voms.admin.persistence.error.VOMSDatabaseException;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSCA;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.util.DNUtil;
import org.glite.security.voms.admin.util.PathNamingScheme;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VOMSAdminDAO implements FindByCertificateDAO<VOMSAdmin>{

  public static final Logger log = LoggerFactory.getLogger(VOMSAdminDAO.class);

  protected VOMSAdminDAO() {

    HibernateFactory.beginTransaction();

  }

  public static VOMSAdminDAO instance() {

    return new VOMSAdminDAO();
  }

  public VOMSAdmin lookup(String certificateSubject,
    String certificateIssuer) {

    return LookupPolicyProvider.instance().lookupStrategy().lookup(this,
      certificateSubject, certificateIssuer);
  }

  public List getAll() {

    String query = "from VOMSAdmin";
    List result = HibernateFactory.getSession().createQuery(query).list();
    return result;
  }

  public List<VOMSAdmin> getNonInternalAdmins() {

    String caDN = "/O=VOMS/O=System%";
    String query = "from VOMSAdmin where ca.subjectString not like :caDN";

    Query q = HibernateFactory.getSession().createQuery(query);
    q.setString("caDN", caDN);

    return q.list();

  }

  public VOMSAdmin getAnyAuthenticatedUserAdmin() {

    String query = "from VOMSAdmin as a where a.dn = :dn and a.ca.subjectString = :caDN";
    Query q = HibernateFactory.getSession().createQuery(query);

    q.setString("dn", VOMSServiceConstants.ANYUSER_ADMIN);
    q.setString("caDN", VOMSServiceConstants.VIRTUAL_CA);

    VOMSAdmin a = (VOMSAdmin) q.uniqueResult();

    if (a == null)
      throw new VOMSDatabaseException(
        "Database corrupted! ANYUSER_ADMIN not found in admins table!");

    return a;

  }

  public VOMSAdmin createUnauthenticateClientAdmin() {

    return createFromSubjectAndIssuer(VOMSServiceConstants.UNAUTHENTICATED_CLIENT,
      VOMSServiceConstants.VIRTUAL_CA);
  }

  public VOMSAdmin getUnauthenticatedClientAdmin() {

    String query = "from VOMSAdmin as a where a.dn = :dn and a.ca.subjectString = :caDN";
    Query q = HibernateFactory.getSession().createQuery(query);

    q.setString("dn", VOMSServiceConstants.UNAUTHENTICATED_CLIENT);
    q.setString("caDN", VOMSServiceConstants.VIRTUAL_CA);

    VOMSAdmin a = (VOMSAdmin) q.uniqueResult();

    return a;

  }

  public VOMSAdmin getById(Long id) {

    if (id == null)
      throw new NullArgumentException("id must be non-null!");

    return (VOMSAdmin) HibernateFactory.getSession().load(VOMSAdmin.class, id);

  }

  public VOMSAdmin findBySubject(String subject) {

    if (subject == null)
      throw new NullArgumentException("subject must be non-null!");

    final String normalizedSubject = DNUtil.normalizeDN(subject);

    Criteria crit = HibernateFactory.getSession()
      .createCriteria(VOMSAdmin.class);

    return (VOMSAdmin) crit.add(Restrictions.eq("dn", normalizedSubject)).uniqueResult();

  }

  public VOMSAdmin findBySubjectAndIssuer(String dn, String caDN) {

    if (dn == null)
      throw new NullArgumentException("dn must be non-null!");

    if (caDN == null)
      throw new NullArgumentException("caDN must be non-null!");

    final String normalizedDn = DNUtil.normalizeDN(dn);
    final String normalizedCaDN = DNUtil.normalizeDN(caDN);
    

    String query = "from VOMSAdmin as a where a.dn = :dn and a.ca.subjectString = :caDN";
    Query q = HibernateFactory.getSession().createQuery(query);

    q.setString("dn", normalizedDn);
    q.setString("caDN", normalizedCaDN);

    return (VOMSAdmin) q.uniqueResult();

  }

  public VOMSAdmin getFromUser(VOMSUser u) {

    assert u != null : "Cannot look for an admin starting from a null user!";

    VOMSAdmin result = null;

    for (Certificate c : u.getCertificates()) {

      // Return the first certificate found...
      result = lookup(c.getSubjectString(),
        c.getCa().getSubjectString());

      if (result != null)
        break;
    }

    return result;
  }

  public VOMSAdmin createFromUser(VOMSUser u) {

    assert u != null : "Cannot create an admin starting from a null user!";

    VOMSAdmin admin = getFromUser(u);

    if (admin != null)
      return admin;

    admin = new VOMSAdmin();
    Certificate c = u.getDefaultCertificate();

    admin.setDn(c.getSubjectString());
    admin.setCa(c.getCa());
    admin.setEmailAddress(u.getEmailAddress());

    HibernateFactory.getSession().save(admin);

    return admin;
  }

  public List getRoleAdmins(VOMSRole r) {

    String searchString = "%Role=" + r.getName();
    String query = "from VOMSAdmin where dn like :searchString";

    return HibernateFactory.getSession().createQuery(query)
      .setString("searchString", searchString).list();

  }

  public void deleteRoleAdmins(VOMSRole r) {

    String searchString = "%Role=" + r.getName();
    String query = "from VOMSAdmin where dn like :searchString";
    Iterator i = HibernateFactory.getSession().createQuery(query)
      .setString("searchString", searchString).iterate();

    while (i.hasNext()) {

      VOMSAdmin a = (VOMSAdmin) i.next();
      delete(a);
    }

  }

  public VOMSAdmin getByFQAN(String fqan) {

    if (fqan == null)
      throw new NullArgumentException("fqan must be non-null!");

    if (PathNamingScheme.isGroup(fqan)){

      return findBySubjectAndIssuer(fqan, VOMSServiceConstants.GROUP_CA);

    }else if (PathNamingScheme.isQualifiedRole(fqan)){

      return findBySubjectAndIssuer(fqan, VOMSServiceConstants.ROLE_CA);
    }
    return null;
  }

  public VOMSAdmin createFromFqan(String fqan) {

    if (fqan == null)
      throw new NullArgumentException("fqan must be non-null!");

    PathNamingScheme.checkSyntax(fqan);

    VOMSAdmin admin = new VOMSAdmin();
    admin.setDn(fqan);

    if (PathNamingScheme.isGroup(fqan))

      admin.setCa(VOMSCADAO.instance().getGroupCA());

    else if (PathNamingScheme.isQualifiedRole(fqan))

      admin.setCa(VOMSCADAO.instance().getRoleCA());

    HibernateFactory.getSession().save(admin);

    return admin;

  }

  public VOMSAdmin createFromSubjectAndIssuer(String dn, String caDN) {

    return create(dn, caDN, null);
  }

  public VOMSAdmin create(String dn, String caDN, String emailAddress) {

    if (dn == null)
      throw new NullArgumentException("dn must be non-null!");

    if (caDN == null)
      throw new NullArgumentException("caDN must be non-null!");

    // Fix for https://savannah.cern.ch/bugs/?31068
    caDN = DNUtil.normalizeDN(caDN);

    VOMSAdmin admin = new VOMSAdmin();
    VOMSCA ca = VOMSCADAO.instance().getByName(caDN);

    if (ca == null)
      throw new IllegalArgumentException(
        "Unkown CA " + caDN + " passed as argument!");

    dn = DNUtil.normalizeDN(dn);
    admin.setDn(dn);
    admin.setCa(ca);
    admin.setEmailAddress(emailAddress);
    HibernateFactory.getSession().save(admin);
    return admin;

  }

  public VOMSAdmin create(VOMSAdmin admin) {

    if (admin == null)
      throw new NullArgumentException("admin must not be null!");

    admin.setDn(DNUtil.normalizeDN(admin.getDn()));
    HibernateFactory.getSession().save(admin);
    return admin;

  }

  public void delete(VOMSAdmin admin) {

    if (admin == null)
      throw new NullArgumentException("admin must not be null!");

    HibernateFactory.getSession().delete(admin);

  }

  public void delete(String dn, String caDN) {

    if (dn == null)
      throw new NullArgumentException("dn must be non-null!");

    if (caDN == null)
      throw new NullArgumentException("caDN must be non-null!");

    VOMSCA ca = VOMSCADAO.instance().getByName(caDN);

    if (ca == null)
      throw new NoSuchCAException("Unknown CA '" + caDN + "'.");

    // FIXME: do it without using an HQL update!
    Query q = HibernateFactory.getSession()
      .createQuery("delete from VOMSAdmin where dn = :dn and ca =:ca")
      .setString("dn", dn).setParameter("ca", ca);

    q.executeUpdate();
  }

  public void saveOrUpdate(VOMSAdmin a) {

    HibernateFactory.getSession().saveOrUpdate(a);
  }

  public void update(VOMSAdmin a) {

    HibernateFactory.getSession().update(a);
  }

}
