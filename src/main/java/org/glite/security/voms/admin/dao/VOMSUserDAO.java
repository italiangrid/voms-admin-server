/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/

package org.glite.security.voms.admin.dao;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.DNUtil;
import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.database.AlreadyExistsException;
import org.glite.security.voms.admin.database.AlreadyMemberException;
import org.glite.security.voms.admin.database.AttributeAlreadyExistsException;
import org.glite.security.voms.admin.database.AttributeValueAlreadyAssignedException;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.database.NoSuchAttributeException;
import org.glite.security.voms.admin.database.NoSuchCAException;
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.database.UserAlreadyExistsException;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.model.VOMSAttributeDescription;
import org.glite.security.voms.admin.model.VOMSCA;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSMapping;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.VOMSUserAttribute;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;



public class VOMSUserDAO {

	private static final Log log = LogFactory.getLog(VOMSUserDAO.class);

	private VOMSUserDAO() {

	}

	public List getAll() {

		Query q = HibernateFactory.getSession().createQuery("select u from VOMSUser u order by u.surname asc");

		List result = q.list();

		return result;
	}

	public List getUnsubscribedGroups(Long userId) {

		// Easy, but not performant (leverage HQL!) implementation

		VOMSUser u = findById(userId);
		List result = new ArrayList();

		Iterator groups = VOMSGroupDAO.instance().getAll().iterator();

		while (groups.hasNext()) {

			VOMSGroup g = (VOMSGroup) groups.next();

			if (!u.isMember(g))
				result.add(g);
		}

		return result;
	}

	public List getUnAssignedRoles(Long userId, Long groupId) {

		VOMSUser u = findById(userId);

		VOMSGroup g = VOMSGroupDAO.instance().findById(groupId);

		List result = new ArrayList();

		Iterator roles = VOMSRoleDAO.instance().getAll().iterator();

		while (roles.hasNext()) {

			VOMSRole r = (VOMSRole) roles.next();

			if (!u.hasRole(g, r))
				result.add(r);
		}

		return result;
	}

	public SearchResults getAll(int firstResults, int maxResults) {

		SearchResults res = SearchResults.instance();
		Query q = HibernateFactory.getSession().createQuery("from VOMSUser as u order by u.surname asc");

		q.setFirstResult(firstResults);
		q.setMaxResults(maxResults);

		res.setCount(countUsers());
		res.setResults(q.list());
		res.setFirstResult(firstResults);
		res.setResultsPerPage(maxResults);

		return res;

	}

	public int countMatches(String searchString) {

		String sString = "%" + searchString + "%";

		String countString = "select count(distinct u) from VOMSUser u where u.surname like :searchString "
				+ "or u.name like :searchString or u.emailAddress like :searchString";

		Query q = HibernateFactory.getSession().createQuery(countString);
		q.setString("searchString", sString);

		Long count = (Long) q.uniqueResult();

		return count.intValue();

	}

	public SearchResults search(
			String searchString,
			int firstResults,
			int maxResults) {

		log.debug("searchString:" + searchString + ",firstResults: "
				+ firstResults + ",maxResults: " + maxResults);

		if (searchString == null || searchString.equals("")
				|| searchString.length() == 0)
			return getAll(firstResults, maxResults);

		SearchResults res = SearchResults.instance();

		String sString = "%" + searchString + "%";

		String queryString = "select distinct u from VOMSUser u where u.surname like :searchString "
				+ "or u.name like :searchString or u.emailAddress like :searchString order by u.surname asc";

		Query q = HibernateFactory.getSession().createQuery(queryString);

		q.setString("searchString", sString);
		q.setFirstResult(firstResults);
		q.setMaxResults(maxResults);

		res.setCount(countMatches(searchString));
		res.setFirstResult(firstResults);
		res.setResultsPerPage(maxResults);
		res.setResults(q.list());
		res.setSearchString(searchString);

		return res;

	}

	public int countUsers() {

		Query q = HibernateFactory.getSession().createQuery(
				"select count(*) from VOMSUser");

        Long count = (Long) q.uniqueResult();
		
		return count.intValue();
	}

    public VOMSUser getByDNandCA(String userDN, VOMSCA ca){
        if (userDN == null)
            throw new NullArgumentException("Cannot find a user by name given a null userDN!");
        
        if (ca == null)
            throw new NullArgumentException("Cannot find a user by name and ca given a null ca!");
            
        return getByDNandCA( userDN, ca.getSubjectString() );
        
    }
	public VOMSUser getByDNandCA(String userDN, String caDN) {

        String queryString = "from Certificate where subjectString = :userDN and ca.subjectString = :caDN";
        
        Query q = HibernateFactory.getSession().createQuery(queryString);

		q.setString("userDN", userDN);
		q.setString("caDN", caDN);
        
        Certificate c = (Certificate) q.uniqueResult();
        
        if (c == null)
            return null;
        
		return c.getUser();

	}
    
	public List getAllNames() {

        
        // FIXME: implement with multiple certificate support
		String query = "select dn, ca.subjectString from org.glite.security.voms.admin.model.VOMSUser";

		return HibernateFactory.getSession().createQuery(query).list();

	}

    
    public VOMSUser findByCertificate(String subject, String issuer){
        
        if ( subject == null )
            throw new NullArgumentException( "subject cannot be null!" );
        
        if ( issuer == null )
            throw new NullArgumentException( "issuer cannot be null!" );
        
        String query = "select u from org.glite.security.voms.admin.model.VOMSUser u join u.certificates c where c.subjectString = :subjectString and c.ca.subjectString = :issuerSubjectString";
        
        Query q = HibernateFactory.getSession().createQuery( query );
        
        q.setString( "subjectString", subject);
        q.setString( "issuerSubjectString", issuer);
        
        VOMSUser u = (VOMSUser) q.uniqueResult(); 
        
        return u;
    }
    
    
	public VOMSUser findByDNandCA(String dn, String caDN) {

		if (dn == null)
			throw new NullArgumentException("dn must be non-null!");

		if (caDN == null)
			throw new NullArgumentException("ca must be non-null!");

        
        return findByCertificate( dn, caDN );
        
	}

	public VOMSUser findById(Long userId) {

		return (VOMSUser) HibernateFactory.getSession().get(VOMSUser.class,
				userId);
	}

	public VOMSUser findByEmail(String emailAddress) {

		if (emailAddress == null)
			throw new NullArgumentException("emailAddress must be non-null!");

		String query = "from org.glite.security.voms.admin.model.VOMSUser as u  where u.emailAddress = :emailAddress";
		Query q = HibernateFactory.getSession().createQuery(query);

		q.setString("emailAddress", emailAddress);

		VOMSUser result = (VOMSUser) q.uniqueResult();

		return result;

	}

//	public VOMSUser create(VOMSUser usr, String caDN) {
//
//		if (usr.getDn() == null || "".equals( usr.getDn() ))
//			throw new NullArgumentException("dn must be non-null!");
//
//		VOMSCA ca = VOMSCADAO.instance().getByName(caDN);
//
//		if (ca == null)
//			throw new NullArgumentException("CA " + caDN
//					+ " does not exist in org.glite.security.voms.admin.database!");
//
//        usr.setDn( DNUtil.normalizeEmailAddressInDN( usr.getDn() ) );
//		usr.setCa(ca);
//
//		return create(usr);
//
//	}

    private void checkNullFields(VOMSUser usr){
        
        if (usr.getName() == null)
            throw new NullArgumentException("Please specify a name for the user!");
        
        if (usr.getSurname() == null)
            throw new NullArgumentException("Please specify a surname for the user!");
        
        if (usr.getInstitution() == null)
            throw new NullArgumentException("Please specify an instituion for the user!");
        
        if (usr.getAddress() == null)
            throw new NullArgumentException("Please specify an address for the user!");
        
        if (usr.getPhoneNumber()==null)
            throw new NullArgumentException("Please specify a phone number for the user!");
        
        if (usr.getEmailAddress() == null )
            throw new NullArgumentException("Please specify an email address for the user!");
        
    }
	public VOMSUser create(VOMSUser usr) {

        checkNullFields( usr );
        
        // Check if CA exists in db

		// Add user to VO root group
		VOMSGroup voGroup = VOMSGroupDAO.instance().getVOGroup();
		HibernateFactory.getSession().save(usr);

		addToGroup(usr, voGroup);

		HibernateFactory.getSession().save(usr);
		HibernateFactory.getSession().save(voGroup);
		return usr;
	}

	public VOMSUser create(
			String dn,
			String caDN,
			String cn,
			String certURI,
			String emailAddress) {

		if (dn == null)
			throw new NullArgumentException("dn must be non-null!");

		if (caDN == null)
			throw new NullArgumentException("ca must be non-null!");

		if (emailAddress == null)
			throw new NullArgumentException("emailAddress must be non-null!");

		VOMSUser u = findByDNandCA(dn, caDN);

		if (u != null)
			throw new UserAlreadyExistsException("User " + u
					+ " already in org.glite.security.voms.admin.database!");
		/* 
         * Email uniqueness checked out since otherwise vomrs cannot create
         * multiple users with different certificates.
         * 
		if (findByEmail(emailAddress) != null)
			throw new EmailAddressAlreadyBoundException("A user with the "
					+ emailAddress + " email address is already in org.glite.security.voms.admin.database!");

		 */
		VOMSCA ca = VOMSCADAO.instance().getByName(caDN);

		if (ca == null)
			throw new NoSuchCAException("Unknown ca " + caDN
					+ ". Will not create user " + dn);

		u = new VOMSUser();

        dn = DNUtil.normalizeEmailAddressInDN( dn );
		u.setDn(dn);
		u.setCa(ca);
		u.setCn(cn);
		u.setCertURI(certURI);
		u.setEmailAddress(emailAddress);

		log.debug("Creating user \"" + u + "\".");

		// Add user to default VO group
		
		VOMSGroup voGroup = VOMSGroupDAO.instance().getVOGroup();
		HibernateFactory.getSession().save(u);
		
		addToGroup(u, voGroup);

		

		return u;
	}

	public void delete(Long userId) {

		VOMSUser u = findById(userId);

		if (u == null)
			throw new NoSuchUserException("User identified by \"" + userId
					+ "\" not found in org.glite.security.voms.admin.database!");
		try {

			delete(u);

		} catch (ObjectNotFoundException e) {
			// Still don't understand why sometimes findById fails in returnin null...
			throw new NoSuchUserException("User identified by \"" + userId
					+ "\" not found in org.glite.security.voms.admin.database!");
		}
	}

	public void delete(VOMSUser u) {

		log.debug("Deleting user \"" + u + "\".");
		// Remove user
//		if (getByDNandCA(u.getDn(), u.getCa().getDn()) == null)
//			throw new NoSuchUserException("User \"" + u
//					+ "\" is not defined in org.glite.security.voms.admin.database!");

		removeFromGroup(u, VOMSGroupDAO.instance().getVOGroup());

	}

	public VOMSUser update(VOMSUser u) {

		HibernateFactory.getSession().update(u);
		return u;

	}

	public void addToGroup(VOMSUser u, VOMSGroup g) {

		log.debug("Adding user \"" + u + "\" to group \"" + g + "\".");

		if (!HibernateFactory.getSession().contains(u)) {

			VOMSUser checkUser = findById(u.getId());

			if (checkUser == null)
				throw new NoSuchUserException("User \"" + u
						+ "\" not found in org.glite.security.voms.admin.database.");
		}

		// Check that the group exists
		if (VOMSGroupDAO.instance().findByName(g.getName()) == null)
			throw new NoSuchGroupException("Group \"" + g
					+ "\" is not defined in org.glite.security.voms.admin.database.");

		VOMSMapping m = new VOMSMapping(u, g, null);
		if (u.getMappings().contains(m))
			throw new AlreadyMemberException("User \"" + u
					+ "\" is already a member of group \"" + g + "\".");

		u.getMappings().add(m);

		HibernateFactory.getSession().save(u);

	}

	public void removeFromGroup(VOMSUser u, VOMSGroup g) {

		log.debug("Removing user \"" + u + "\" from group \"" + g + "\".");

//		if (getByDNandCA(u.getDn(), u.getCa().getDn()) == null)
//			throw new NoSuchUserException("User \"" + u
//					+ "\" is not defined in org.glite.security.voms.admin.database.");

		if (VOMSGroupDAO.instance().findByName(g.getName()) == null)
			throw new NoSuchGroupException("Group \"" + g
					+ "\" is not defined in database.");

		u.removeFromGroup(g);

		if (!g.isRootGroup()) {

			HibernateFactory.getSession().save(u);

		} else {

			// Root group membership removal, let's delete the user as well.
			log
					.debug("Deleting user from database, since it is being removed from the VO root group.");

            u.getAttributes().clear();
			HibernateFactory.getSession().delete(u);

			
		}
	}

	public VOMSUserAttribute setAttribute(
			VOMSUser u,
			String attrName,
			String attrValue){
		
		VOMSAttributeDescription desc = VOMSAttributeDAO.instance()
			.getAttributeDescriptionByName(attrName);
		
		if (desc == null)
			throw new NoSuchAttributeException("Attribute '"+attrName+"' is not defined in this vo.");
		
		
		
		if (! VOMSAttributeDAO.instance().isAttributeValueAlreadyAssigned(u, desc, attrValue)){
			
            
			VOMSUserAttribute val = u.getAttributeByName( desc.getName() );
            if (val== null){
                val = VOMSUserAttribute.instance(desc, attrValue,u);
                u.addAttribute(val);
            }else
                val.setValue( attrValue );
			
			return val;
		}
		
		
		throw new AttributeValueAlreadyAssignedException("Value '"+attrValue+"' for attribute '"+attrName+"' have been already assigned to another user in this vo! Choose a different value.");
		
	}
			
			
	public VOMSUserAttribute createAttribute(
			VOMSUser u,
			String attrName,
			String attrDesc,
			String value) {

		if (u.getAttributeByName(attrName) != null)
			throw new AttributeAlreadyExistsException("Attribute \"" + attrName
					+ "\" already defined for user \"" + u + "\".");

		VOMSAttributeDescription desc = VOMSAttributeDAO.instance()
				.getAttributeDescriptionByName(attrName);

		if (desc == null)
			desc = VOMSAttributeDAO.instance().createAttributeDescription(
					attrName, attrDesc);

		log.debug("Creating attribute \"(" + attrName + "," + value
				+ ")\" for user \"" + u + "\".");
		VOMSUserAttribute val = VOMSUserAttribute.instance(desc, value,u);
		u.addAttribute(val);
		return val;

	}

	public void deleteAttribute(VOMSUser u, String attrName) {

		if (u.getAttributeByName(attrName) == null)
			throw new NoSuchAttributeException("Attribute \"" + attrName
					+ "\" not defined for user \"" + u + "\".");

		log.debug("Deleting attribute \"" + attrName + "\" for user \"" + u
				+ "\".");

		u.deleteAttributeByName(attrName);
		HibernateFactory.getSession().update(u);

	}

	public void assignRole(VOMSUser u, VOMSGroup g, VOMSRole r) {

		u.assignRole(g, r);
		HibernateFactory.getSession().update(u);

	}

	public void dismissRole(VOMSUser u, VOMSGroup g, VOMSRole r) {

		u.dismissRole(g, r);
		HibernateFactory.getSession().update(u);

	}

	
    public void deleteCertificate(VOMSUser u, String subject, String issuer){
        
        assert u != null: "User must be non-null!";
        assert subject != null: "Subject must be non-null!";
        assert issuer !=null: "Issuer must be non-null!";
        
        Certificate cert = CertificateDAO.instance().findByDNCA(subject,issuer);
        deleteCertificate( u, cert );    
    }
    
    
    public void deleteCertificate(Certificate cert){
        
        assert cert != null: "Certificate must be non-null!";
        
        VOMSUser u = cert.getUser();
        u.getCertificates().remove( cert );
        HibernateFactory.getSession().saveOrUpdate(u);
        
    }
	public void deleteCertificate(VOMSUser u, Certificate cert){
		
		assert u != null: "User must be non-null!";
		assert cert != null: "Certificate must be non-null!";
		
		u.getCertificates().remove(cert);
		HibernateFactory.getSession().saveOrUpdate(u);
		
	}
	public void addCertificate(VOMSUser u, X509Certificate x509Cert){
		
		assert u != null: "User must be non-null!";
		assert x509Cert != null: "Certificate must be non-null!";
		
		// Assume the certificate have been already validated
		// at this stage.
		
		String caDN = DNUtil.getBCasX500(x509Cert.getIssuerX500Principal());
		VOMSCA ca = VOMSCADAO.instance().getByName(caDN);
		
		if (ca == null)
			throw new NoSuchCAException("CA '"+caDN+"' not recognized!");
		
		Certificate cert = CertificateDAO.instance().find(x509Cert); 
		
		if (cert != null)
			throw new AlreadyExistsException("Certificate already bound!");
		
		cert = new Certificate();
		
		String subjectString = DNUtil.getBCasX500(x509Cert.getSubjectX500Principal());
		cert.setSubjectString(subjectString);
		cert.setSubjectDER(x509Cert.getSubjectX500Principal());
		cert.setCreationTime(new Date());
		cert.setNotAfter(x509Cert.getNotAfter());
		cert.setSuspended(false);
		cert.setCa(ca);
		
		cert.setUser(u);
		u.addCertificate(cert);
		
		// FIXME: Find a way to extract email address from certificate in a meaningful
		// way.
		HibernateFactory.getSession().saveOrUpdate(cert);
		HibernateFactory.getSession().saveOrUpdate(u);
		
		
	}
	public void addCertificate(VOMSUser u, String dn, String caDn){
		assert u != null: "User must be non-null!";
		assert dn != null: "DN must be non-null!";
		assert caDn != null: "CA must be non-null!";
		
		
		VOMSCA ca = VOMSCADAO.instance().getByName(caDn);
		if (ca == null)
			throw new NoSuchCAException("CA '"+caDn+"' not found in database.");
		
		
		Certificate cert = CertificateDAO.instance().findByDNCA(dn, caDn);
		
		if (cert != null)
			throw new AlreadyExistsException("Certificate already bound!");
		
		cert = new Certificate();
		
		cert.setSubjectString(dn);
		cert.setCa(ca);
		cert.setCreationTime(new Date());
		
		cert.setUser(u);
		u.addCertificate(cert);
		
		HibernateFactory.getSession().saveOrUpdate(cert);
		HibernateFactory.getSession().saveOrUpdate(u);
		
	}
	public void deleteAll() {

		Iterator users = getAll().iterator();

		while (users.hasNext())
			delete((VOMSUser) users.next());

	}

	public static VOMSUserDAO instance() {

		HibernateFactory.beginTransaction();
		return new VOMSUserDAO();
	}
}