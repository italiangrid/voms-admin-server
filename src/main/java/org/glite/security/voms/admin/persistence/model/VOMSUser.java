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
package org.glite.security.voms.admin.persistence.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.User;
import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
import org.glite.security.voms.admin.error.IllegalStateException;
import org.glite.security.voms.admin.error.NotFoundException;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.error.VOMSSyntaxException;
import org.glite.security.voms.admin.persistence.Auditable;
import org.glite.security.voms.admin.persistence.error.AlreadyExistsException;
import org.glite.security.voms.admin.persistence.error.NoSuchAttributeException;
import org.glite.security.voms.admin.persistence.error.NoSuchMappingException;
import org.glite.security.voms.admin.persistence.error.VOMSInconsistentDatabaseException;
import org.glite.security.voms.admin.persistence.model.personal_info.PersonalInformationRecord;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;
import org.glite.security.voms.admin.persistence.model.task.Task;
import org.glite.security.voms.admin.persistence.model.task.Task.TaskStatus;
import org.glite.security.voms.admin.util.PathNamingScheme;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

/**
 * 
 * 
 * 
 * @author andrea
 * 
 */

@Entity
@Table(name = "usr")
public class VOMSUser implements Serializable, Auditable, Comparable {

	private static final long serialVersionUID = -3815869585264835046L;

	public static final Logger log = LoggerFactory.getLogger(VOMSUser.class);

	public enum SuspensionReason {

		FAILED_TO_SIGN_AUP("User failed to sign the AUP in time."), MEMBERSHIP_EXPIRATION(
				"User membership has expired."), SECURITY_INCIDENT(
				"User membership has been suspended after a security incident."), OTHER(
				"User membership has been suspended for another unknown reason.");

		String message;

		private SuspensionReason(String msg) {
			this.message = msg;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

	public VOMSUser() {

	}

	@Id
	@Column(name = "userid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	// Base membership information (JSPG requirements)

	String name;

	String surname;

	String institution;

	String address;

	@Column(name = "phone_number")
	String phoneNumber;

	@Column(nullable = false, name = "email_address")
	String emailAddress;

	// Compatibility fields
	String dn;

	/** This field is here for compatibility reasons **/
	@ManyToOne(targetEntity = VOMSCA.class, optional = true)
	@JoinColumn(name = "ca")
	VOMSCA ca;

	// Creation time and validity info
	@Column(name = "creation_time", nullable = false)
	Date creationTime;

	@Column(name = "end_time", nullable = false)
	Date endTime;

	@Column(name = "suspended")
	Boolean suspended = false;

	@Enumerated(EnumType.STRING)
	@Column(name = "suspension_reason_code")
	SuspensionReason suspensionReasonCode;

	@Column(name = "suspension_reason")
	String suspensionReason;

	/** Generic attributes mapping **/
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "user")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	Set<VOMSUserAttribute> attributes = new HashSet<VOMSUserAttribute>();

	/** Membership mappings **/
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "user")
	@Sort(type = SortType.NATURAL)
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	Set<VOMSMapping> mappings = new TreeSet<VOMSMapping>();

	/** User certificates **/
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "user")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	Set<Certificate> certificates = new HashSet<Certificate>();

	/** AUP acceptance records **/
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "user")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	Set<AUPAcceptanceRecord> aupAcceptanceRecords = new HashSet<AUPAcceptanceRecord>();

	/** Assigned tasks **/
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "user", fetch = FetchType.EAGER)
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	Set<Task> tasks = new HashSet<Task>();

	/** Personal information set **/
	// FIXME: currently ignored by configuration
	@Transient
	Set<PersonalInformationRecord> personalInformations = new HashSet<PersonalInformationRecord>();

	/**
	 * @return Returns the emailAddress.
	 */
	public String getEmailAddress() {

		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            The emailAddress to set.
	 */
	public void setEmailAddress(String emailAddress) {

		this.emailAddress = emailAddress;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public Set getAttributes() {

		return attributes;
	}

	public void setAttributes(Set attributes) {

		this.attributes = attributes;
	}

	public Set getMappings() {

		return mappings;
	}

	public void setMappings(Set mappings) {

		this.mappings = mappings;
	}

	public VOMSUserAttribute getAttributeByName(String name) {

		Iterator i = attributes.iterator();

		while (i.hasNext()) {
			VOMSUserAttribute tmp = (VOMSUserAttribute) i.next();

			if (tmp.getName().equals(name))
				return tmp;
		}

		return null;

	}

	public void deleteAttributeByName(String attrName) {

		deleteAttribute(getAttributeByName(attrName));

	}

	public void deleteAttribute(VOMSUserAttribute val) {

		if (!attributes.contains(val))
			throw new NoSuchAttributeException("Attribute \"" + val.getName()
					+ "\" undefined for user " + this);

		attributes.remove(val);

	}

	public void setAttribute(String name, String value) {

		VOMSUserAttribute val = getAttributeByName(name);

		if (val == null)
			throw new NoSuchAttributeException("Attribute \"" + name
					+ "\" undefined for user \"" + this + "\".");

		val.setValue(value);

	}

	public void addAttribute(VOMSUserAttribute val) {

		val.setUser(this);

		if (attributes.contains(val)) {
			attributes.remove(val);
		}

		attributes.add(val);
	}

	public boolean isMember(String groupName) {

		if (groupName == null)
			throw new NullArgumentException(
					"Cannot org.glite.security.voms.admin.test membership in a null group!");

		if (!PathNamingScheme.isGroup(groupName))
			throw new VOMSSyntaxException(
					"Group name passed as argument does not respect the VOMS FQAN syntax. ["
							+ groupName + "]");

		Iterator i = getMappings().iterator();

		while (i.hasNext()) {

			VOMSMapping m = (VOMSMapping) i.next();
			if (m.getGroup().getName().equals(groupName) && m.isGroupMapping())
				return true;
		}

		return false;

	}

	public boolean isMember(VOMSGroup g) {

		if (g == null)
			throw new NullArgumentException(
					"Cannot org.glite.security.voms.admin.test membership in a null group!");

		Iterator i = getMappings().iterator();

		while (i.hasNext()) {

			VOMSMapping m = (VOMSMapping) i.next();
			if (m.getGroup().equals(g) && m.isGroupMapping())
				return true;
		}

		return false;

	}

	public void addToGroup(VOMSGroup g) {

		log.debug("Adding user \"" + this + "\" to group \"" + g + "\".");

		VOMSMapping m = new VOMSMapping(this, g, null);
		if (!getMappings().add(m))
			throw new AlreadyExistsException("User \"" + this
					+ "\" is already a member of group \"" + g + "\".");

		// Add this user to parent groups
		if (!g.isRootGroup()) {
			if (!isMember(g.parent))
				addToGroup(g.parent);
		}

	}

	public void removeFromGroup(VOMSGroup g) {

		log.debug("Removing user \"" + this + "\" from group \"" + g + "\".");

		dismissRolesInGroup(g);

		VOMSMapping m = new VOMSMapping(this, g, null);

		if (getMappings().contains(m)) {
			getMappings().remove(m);
			g.getMappings().remove(m);

		} else
			throw new NoSuchMappingException("User \"" + this
					+ "\" is not a member of group \"" + g + "\".");

	}

	public VOMSMapping assignRole(VOMSGroup g, VOMSRole r) {

		if (!isMember(g))
			throw new NoSuchMappingException("User \"" + this
					+ "\" is not a member of group \"" + g + "\".");

		VOMSMapping m = new VOMSMapping(this, g, r);
		if (getMappings().contains(m))
			throw new AlreadyExistsException("User \"" + this
					+ "\" already has role \"" + r + "\" in group \"" + g
					+ "\".");

		log.debug("Assigning role \"" + r + "\" to user \"" + this
				+ "\" in group \"" + g + "\".");

		getMappings().add(m);
		r.getMappings().add(m);

		return m;

	}

	public VOMSMapping dismissRole(VOMSGroup g, VOMSRole r) {

		if (!isMember(g))
			throw new NoSuchMappingException("User \"" + this
					+ "\" is not a member of group \"" + g + "\".");

		if (!hasRole(g, r))
			throw new NoSuchMappingException("User \"" + this
					+ "\" does not have role \"" + r + "\" in group \"" + g
					+ "\".");

		log.debug("Dismissing role \"" + r + "\" from user \"" + this
				+ "\" in group \"" + g + "\".");

		Iterator i = getMappings().iterator();
		boolean removed = false;

		VOMSMapping m = null;
		while (i.hasNext()) {
			m = (VOMSMapping) i.next();
			if (m.isRoleMapping()) {
				if (m.getGroup().equals(g) && m.getRole().equals(r)) {
					i.remove();
					r.getMappings().remove(m);
					removed = true;
				}
			}
		}

		if (!removed)
			throw new VOMSInconsistentDatabaseException(
					"Error removing exiting role mapping!");
		return m;

	}

	public void dismissRolesInGroup(VOMSGroup g) {

		if (!isMember(g))
			throw new NoSuchMappingException("User \"" + this
					+ "\" is not a member of group \"" + g + "\".");

		Iterator i = getMappings().iterator();

		while (i.hasNext()) {

			VOMSMapping m = (VOMSMapping) i.next();
			if (m.getGroup().equals(g) && m.isRoleMapping())
				i.remove();
		}

		return;
	}

	public boolean hasRole(VOMSGroup g, VOMSRole r) {

		if (!isMember(g))
			throw new NoSuchMappingException("User \"" + this
					+ "\" is not a member of group \"" + g + "\".");

		Iterator i = getMappings().iterator();

		while (i.hasNext()) {

			VOMSMapping m = (VOMSMapping) i.next();
			if (m.isRoleMapping()) {
				if (m.getGroup().equals(g) && m.getRole().equals(r))
					return true;
			}
		}

		return false;
	}

	public boolean hasRole(String fqan) {

		if (!PathNamingScheme.isQualifiedRole(fqan))
			throw new IllegalArgumentException(
					"Role name passed as argument is not a qualified role! ["
							+ fqan + "]");

		String groupName = PathNamingScheme.getGroupName(fqan);
		String roleName = PathNamingScheme.getRoleName(fqan);

		Iterator i = getMappings().iterator();

		while (i.hasNext()) {

			VOMSMapping m = (VOMSMapping) i.next();
			if (m.isRoleMapping()) {
				if (m.getGroup().getName().equals(groupName)
						&& m.getRole().getName().equals(roleName))
					return true;
			}
		}

		return false;

	}

	public Set getGroups() {

		SortedSet res = new TreeSet();
		Iterator mIter = getMappings().iterator();
		while (mIter.hasNext()) {

			VOMSMapping m = (VOMSMapping) mIter.next();
			if (m.isGroupMapping())
				res.add(m.getGroup());
		}

		return Collections.unmodifiableSortedSet(res);

	}

	public Set getRoles(VOMSGroup g) {

		SortedSet res = new TreeSet();

		Iterator mIter = getMappings().iterator();
		while (mIter.hasNext()) {

			VOMSMapping m = (VOMSMapping) mIter.next();
			if (m.isRoleMapping() && m.getGroup().equals(g))
				res.add(m.getRole());
		}

		return Collections.unmodifiableSortedSet(res);
	}

	public Set getRoleMappings() {

		SortedSet res = new TreeSet();

		Iterator mIter = getMappings().iterator();

		while (mIter.hasNext()) {

			VOMSMapping m = (VOMSMapping) mIter.next();
			if (m.isRoleMapping())
				res.add(m.getFQAN());
		}

		return res;
	}

	public Map getMappingsMap() {

		log.debug("mappings.size(): " + getMappings().size());
		if (getMappings().size() == 0)
			return null;

		Iterator i = getMappings().iterator();
		Map map = new TreeMap();

		while (i.hasNext()) {

			VOMSMapping m = (VOMSMapping) i.next();
			log.debug("mapping: " + m);

			if (m.isGroupMapping()) {

				log.debug("Added group mapping to map: " + m.getGroup());
				map.put(m.getGroup(), new TreeSet());

			} else if (m.isRoleMapping()) {

				if (!map.containsKey(m.getGroup())) {

					Set s = new TreeSet();
					s.add(m.getRole());
					map.put(m.getGroup(), s);

					log.debug("Added mapping to map: " + m);

				} else {

					Set s = (Set) map.get(m.getGroup());
					s.add(m.getRole());

					// is this necessary?
					map.put(m.getGroup(), s);
					log.debug("Added mapping to map:" + m.getRole());
				}
			}
		}

		return map;

	}

	public void fromUser(User u) {

		if (u == null)
			throw new NullArgumentException("User passed as argument is null!");

		
		setEmailAddress(u.getMail());
	}

	public User asUser() {

		User u = new User();

		u.setDN(getDefaultCertificate().getSubjectString());
		u.setCA(getDefaultCertificate().getCa().getSubjectString());
		u.setCN(null);
		u.setMail(getEmailAddress());
		u.setCertUri(null);

		return u;
	}

	public static User[] collectionAsUsers(Collection c) {

		if (c == null || c.isEmpty())
			return null;

		User[] users = new User[c.size()];

		int index = 0;
		Iterator i = c.iterator();

		while (i.hasNext())
			users[index++] = ((VOMSUser) i.next()).asUser();

		return users;

	}

	public String toString() {

		ToStringBuilder builder = new ToStringBuilder(this);
		
		builder.append("id", id).append("defaultCertficate", getDefaultCertificate()).append("name",name).append("surname", surname).append("emailAddress",emailAddress).append("suspended",suspended).append("endTime", endTime);
		
		return builder.toString();
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (other == null)
			return false;

		if (!(other instanceof VOMSUser))
			return false;

		VOMSUser that = (VOMSUser) other;

		// If name and surname are defined for both parties,
		// users are considered equal if they have the same:

		// 1. name
		// 2. surname
		// 3. emailAddress

		// If name or surname aren't defined for a user
		// the equality check is done on the first certificate.

		// If no certificate is available, the check is done on the
		// id

		if (getName() != null && getSurname() != null) {

			if (that.getName() != null && that.getSurname() != null) {

				if (getName().equals(that.getName()))
					if (getSurname().equals(that.getSurname()))
						return getEmailAddress().equals(that.getEmailAddress());

				return false;

			} else
				getDefaultCertificate().equals(that.getDefaultCertificate());

		}

		if (getDefaultCertificate() == null) {

			if (getId() == null)
				throw new IllegalStateException(
						"No information available to compare two users: this="
								+ this + " , that=" + that);

			return getId().equals(that.getId());

		}

		return getDefaultCertificate().equals(that.getDefaultCertificate());

	}

	public int hashCode() {

		HashCodeBuilder builder = new HashCodeBuilder(11, 59);

		if (getName() != null && getSurname() != null)
			builder.append(name).append(surname).append(emailAddress);
		else {

			if (getDefaultCertificate() == null) {

				if (dn == null)
					builder.append(id);
				else
					builder.append(dn);

			} else
				builder.append(getDefaultCertificate().getSubjectString());
		}

		return builder.toHashCode();
	}

	public int compareTo(Object o) {

		if (this.equals(o))
			return 0;

		if (o == null)
			return 1;

		VOMSUser that = (VOMSUser) o;

		if (getName() != null && getSurname() != null) {

			if (that.getName() != null && that.getSurname() != null) {

				// Both users have name and surname defined,
				// order by surname and then by name
				if (getSurname().equals(that.getSurname()))
					return getName().compareTo(that.getName());
				else
					return getSurname().compareTo(that.getSurname());

			} else
				// One user has name or surname undefined, compare certificates
				return getDefaultCertificate().compareTo(
						that.getDefaultCertificate());
		}

		if (getDefaultCertificate() != null)
			// Both users have name and surname undefined, compare certificates
			return getDefaultCertificate().compareTo(
					that.getDefaultCertificate());

		return -1;

	}

	public String getShortName() {
		
		if (name == null){
			if (getDefaultCertificate() == null)
				return getDn();
			else
				return getDefaultCertificate().subjectString + "("+getId()+")";
		}
		return String.format("%s %s (%d)", name, surname, id);
	}

	public Set<Certificate> getCertificates() {

		return certificates;
	}

	public void setCertificates(Set<Certificate> certificates) {

		this.certificates = certificates;
	}

	public void addCertificate(Certificate cert) {

		if (hasCertificate(cert))
			throw new AlreadyExistsException("Certificate '" + cert
					+ "' is already bound to user '" + this + "'.");

		getCertificates().add(cert);
		cert.setUser(this);

	}

	public boolean hasCertificate(Certificate cert) {

		for (Certificate c: certificates){
			
			if (c.equals(cert))
				return true;
		}
		
		return false;
	}

	public List<Certificate> getCertificatesBySubject(String subject){
		
		List<Certificate> result = new ArrayList<Certificate>();
		
		for (Certificate c: certificates){
			
			if (c.getSubjectString().equals(subject))
				result.add(c);
		}
		
		return result;
	}
	
	public void removeCertificate(Certificate cert) {

		if (!hasCertificate(cert))
			throw new NotFoundException("Certificate '" + cert
					+ "' is not bound to user '" + this + "'.");

		getCertificates().remove(cert);

	}

	public String getAddress() {

		return address;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public Date getCreationTime() {

		return creationTime;
	}

	public void setCreationTime(Date creationTime) {

		this.creationTime = creationTime;
	}

	
	public long getDaysBeforeEndTime(){
		
		Date now = new Date();
		
		if (now.after(getEndTime()))
			return -1;
		
		long timeDiff = getEndTime().getTime() - now.getTime();
		
		return TimeUnit.MILLISECONDS.toDays(timeDiff);
		
	}
	
	public Date getEndTime() {

		return endTime;
	}

	public void setEndTime(Date endTime) {

		this.endTime = endTime;
	}

	public String getInstitution() {

		return institution;
	}

	public void setInstitution(String institution) {

		this.institution = institution;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getPhoneNumber() {

		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {

		this.phoneNumber = phoneNumber;
	}

	public String getSurname() {

		return surname;
	}

	public void setSurname(String surname) {

		this.surname = surname;
	}

	public String getFullName() {

		return this.getName() + " " + this.getSurname();
	}

	public Certificate getDefaultCertificate() {

		Iterator<Certificate> iter = getCertificates().iterator();

		if (iter.hasNext())
			return iter.next();

		return null;

	}

	public String getEscapedDn() {

		Certificate cert = getDefaultCertificate();

		if (cert == null)
			return null;

		return cert.getSubjectString().replaceAll("'", "\\\\'");

	}

	/**
	 * @return the aupAcceptanceRecords
	 */
	public Set<AUPAcceptanceRecord> getAupAcceptanceRecords() {

		return aupAcceptanceRecords;
	}

	/**
	 * @param aupAcceptanceRecords
	 *            the aupAcceptanceRecords to set
	 */
	public void setAupAcceptanceRecords(
			Set<AUPAcceptanceRecord> aupAcceptanceRecords) {

		this.aupAcceptanceRecords = aupAcceptanceRecords;
	}

	public boolean hasSignedAUP(AUPVersion aupVersion) {

		for (AUPAcceptanceRecord r : aupAcceptanceRecords) {

			if (r.getAupVersion().equals(aupVersion))
				return true;

		}

		return false;
	}

	public AUPAcceptanceRecord getAUPAccceptanceRecord(AUPVersion aupVersion) {

		for (AUPAcceptanceRecord r : aupAcceptanceRecords) {

			if (r.getAupVersion().equals(aupVersion))
				return r;
		}

		return null;
	}

	/**
	 * @return the tasks
	 */
	public Set<Task> getTasks() {

		return tasks;
	}

	/**
	 * @param tasks
	 *            the tasks to set
	 */
	public void setTasks(Set<Task> tasks) {

		this.tasks = tasks;
	}

	@Deprecated
	public String getDn() {

		// If the default certificate exists for this user, take the dn from
		// there...
		if (getDefaultCertificate() != null) {
			return getDefaultCertificate().getSubjectString();
		}

		// Compatibility behaviour
		return dn;
	}

	@Deprecated
	public VOMSCA getCa() {

		if (getDefaultCertificate() != null) {
			return getDefaultCertificate().getCa();
		}

		return ca;
	}

	/**
	 * @return the personalInformations
	 */
	public Set<PersonalInformationRecord> getPersonalInformations() {

		return personalInformations;
	}

	/**
	 * @param personalInformations
	 *            the personalInformations to set
	 */
	public void setPersonalInformations(
			Set<PersonalInformationRecord> personalInformations) {

		this.personalInformations = personalInformations;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public void setCa(VOMSCA ca) {
		this.ca = ca;
	}

	public void assignTask(Task t) {

		if (!getTasks().contains(t)) {

			getTasks().add(t);
			t.setUser(this);

		}
	}

	public Task removeTask(Task t) {

		if (getTasks().contains(t)) {
			getTasks().remove(t);
			t.setUser(null);

			return t;
		}

		return null;

	}
	
	public boolean hasInvalidAUPAcceptanceRecordForAUP(AUP aup){
		
		if (getAupAcceptanceRecords().isEmpty())
			return false;
		
		for (AUPAcceptanceRecord r: getAupAcceptanceRecords()){
			if (r.getAupVersion().equals(aup.getActiveVersion()) && !r.getValid())
				return true;
			
		}
		return false;
	}
	
	public boolean hasInvalidAUPAcceptanceRecord(){
		
		if (getAupAcceptanceRecords().isEmpty())
			return false;
		
		for (AUPAcceptanceRecord r: getAupAcceptanceRecords())
			if (!r.getValid())
				return true;
		
		return false;
	}

	public boolean hasPendingSignAUPTasks() {
		if (getTasks().isEmpty())
			return false;

		for (Task t : getTasks())
			if (t instanceof SignAUPTask) {
				SignAUPTask aupTask = (SignAUPTask) t;

				if (!aupTask.getStatus().equals(TaskStatus.COMPLETED))
					return true;
			}

		return false;
	}

	public boolean hasPendingSignAUPTask(AUP aup) {

		if (getTasks().isEmpty())
			return false;

		for (Task t : getTasks())
			if (t instanceof SignAUPTask) {
				SignAUPTask aupTask = (SignAUPTask) t;
				log.debug("aupTask: " + aupTask);
				if (aupTask.getAup().equals(aup)
						&& (!aupTask.getStatus().equals(TaskStatus.COMPLETED))) {
					log.debug("Found pending aup task: " + aupTask);
					return true;
				}
			}
		return false;

	}
	
	public SignAUPTask getPendingSignAUPTask(AUP aup) {

		if (getTasks().isEmpty())
			return null;

		for (Task t : getTasks()) {

			if (t instanceof SignAUPTask) {

				SignAUPTask aupTask = (SignAUPTask) t;
				if (aupTask.getAup().equals(aup)
						&& !aupTask.getStatus().equals(TaskStatus.COMPLETED))
					return aupTask;
			}
		}

		return null;

	}

	public void suspend(SuspensionReason reason) {

		setSuspended(true);
		setSuspensionReasonCode(reason);
		setSuspensionReason(reason.getMessage());

		for (Certificate c : getCertificates()) {

			// Only suspend certificates that are not already suspended
			// possibly for another reason.
			if (!c.isSuspended())
				c.suspend(reason);
		}

	}

	/**
	 * Restores membership and certificates that where suspended for the reason
	 * passed as argument
	 * 
	 * @param reason
	 */
	public void restore(SuspensionReason reason) {
		setSuspended(false);
		setSuspensionReason(null);

		for (Certificate c : getCertificates())
			c.restore(reason);
	}

	public void restore() {
		setSuspended(false);
		
		setSuspensionReason(null);
		setSuspensionReasonCode(null);

		for (Certificate c : getCertificates())
			c.restore();

	}

	public boolean isSuspended() {

		return (suspended == null ? false : suspended);

	}

	public Boolean getSuspended() {
		return suspended;
	}

	public void setSuspended(Boolean suspended) {
		this.suspended = suspended;
	}

	public SuspensionReason getSuspensionReasonCode() {
		return suspensionReasonCode;
	}

	public void setSuspensionReasonCode(SuspensionReason suspensionReasonCode) {
		this.suspensionReasonCode = suspensionReasonCode;
	}

	public String getSuspensionReason() {
		return suspensionReason;
	}

	public void setSuspensionReason(String suspensionReason) {
		this.suspensionReason = suspensionReason;
	}

	public boolean hasSuspendedCertificates() {

		for (Certificate c : certificates)
			if (c.isSuspended())
				return true;

		return false;
	}
	
	public boolean hasExpired(){
		
		return endTime.before(new Date());
	}
	
	public long getDaysSinceExpiration(){
		
		if (hasExpired()){
			
			Date now = new Date();
			long timediff = now.getTime() - getEndTime().getTime();
			
			return TimeUnit.MILLISECONDS.toDays(timediff);
		}
		
		return -1L;
	}

	
	public static VOMSUser fromVOMSUserJSON(VOMSUserJSON user){
		
		VOMSUser u = new VOMSUser();
		
		u.setName(user.getName());
		u.setSurname(user.getSurname());
		u.setAddress(user.getAddress());
		u.setInstitution(user.getInstitution());
		u.setPhoneNumber(user.getPhoneNumber());
		u.setEmailAddress(user.getEmailAddress());
		
		return u;
		
	}
	
	public static VOMSUser fromRequesterInfo(RequesterInfo ri) {

		VOMSUser u = new VOMSUser();

		u.setDn(ri.getCertificateSubject());
		
		u.setName(ri.getName());
		u.setSurname(ri.getSurname());
		u.setAddress(ri.getAddress());
		u.setInstitution(ri.getInstitution());
		u.setPhoneNumber(ri.getPhoneNumber());
		u.setEmailAddress(ri.getEmailAddress());

		return u;
	}
}
