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

package org.glite.security.voms.admin.integration.orgdb.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Immutable;


@Entity
@Immutable
@Table(name="VOMS_PERSONS")
public class VOMSOrgDBPerson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="PERSON_ID")
	Long id;
	
	@Column(name="IDENT", length=7)
	Long cernId;
	
	@Column(name="NAME", length=24, nullable=false)
	String name;
	
	@Column(name="FIRST_NAME", length=18, nullable=false)
	String firstName;
	
	@Column(name="DEPART", length=3)
	String department;
	
	@Column(name="GRP", length=3)
	String group;
	
	@Column(name="SECT", length=3)
	String sector;
	
	@Column(name="BUILDING", length=10)
	String building;
	
	@Column(name="FLOOR", length=2)
	String floor;
	
	@Column(name="ROOM", length=4)
	String room;
	
	@Column(name="TEL_1", length=5)
	String tel1;
	
	@Column(name="TEL_2", length=5)
	String tel2;
	
	@Column(name="TEL_3", length=5)
	String tel3;
	
	@Column(name="PORTABLE_PHONE", length=5)
	String portablePhone;
	
	@Column(name="BEEP", length=5)
	String beeper;
	
	@Column(name="EMAIL", length=60)
	String email;
	
	@Column(name="PHYSICAL_EMAIL", length=60)
	String physicalEmail;
	
	@Column(name="FIRM", length=14)
	String firm;
	
	@Column(name="AT_CERN", nullable=false, length=1)
	String atCern;
	
	@Column(name="PERSON_CLASS", length=6)
	String personClass;
	
	@Column(name="SUPERVISOR_OF_EXTERNAL_STAFF")
	Boolean supervisorOfExternalStuff;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="NATIONALITY1")
	Country nationality1;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="INSTITUTE")
	Institute institute;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_BIRTH")
	Date dateOfBirth;
	
	@Column(name="PROCESSING_START_DATE")
	Date processingStartDate;
	
	@Column(name="PROCESSING_END_DATE")
	Date processingEndDate;
		
	@OneToMany(mappedBy="vomsPerson", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="PERSON_ID")
	Set<Participation> participations = new HashSet<Participation>();
	
	/**
	 * @return the cernId
	 */
	public Long getCernId() {
		return cernId;
	}

	/**
	 * @param cernId the cernId to set
	 */
	public void setCernId(Long cernId) {
		this.cernId = cernId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the sector
	 */
	public String getSector() {
		return sector;
	}

	/**
	 * @param sector the sector to set
	 */
	public void setSector(String sector) {
		this.sector = sector;
	}

	/**
	 * @return the building
	 */
	public String getBuilding() {
		return building;
	}

	/**
	 * @param building the building to set
	 */
	public void setBuilding(String building) {
		this.building = building;
	}

	/**
	 * @return the floor
	 */
	public String getFloor() {
		return floor;
	}

	/**
	 * @param floor the floor to set
	 */
	public void setFloor(String floor) {
		this.floor = floor;
	}

	/**
	 * @return the room
	 */
	public String getRoom() {
		return room;
	}

	/**
	 * @param room the room to set
	 */
	public void setRoom(String room) {
		this.room = room;
	}

	/**
	 * @return the tel1
	 */
	public String getTel1() {
		return tel1;
	}

	/**
	 * @param tel1 the tel1 to set
	 */
	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}

	/**
	 * @return the tel2
	 */
	public String getTel2() {
		return tel2;
	}

	/**
	 * @param tel2 the tel2 to set
	 */
	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	/**
	 * @return the tel3
	 */
	public String getTel3() {
		return tel3;
	}

	/**
	 * @param tel3 the tel3 to set
	 */
	public void setTel3(String tel3) {
		this.tel3 = tel3;
	}

	/**
	 * @return the portablePhone
	 */
	public String getPortablePhone() {
		return portablePhone;
	}

	/**
	 * @param portablePhone the portablePhone to set
	 */
	public void setPortablePhone(String portablePhone) {
		this.portablePhone = portablePhone;
	}

	/**
	 * @return the beeper
	 */
	public String getBeeper() {
		return beeper;
	}

	/**
	 * @param beeper the beeper to set
	 */
	public void setBeeper(String beeper) {
		this.beeper = beeper;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the physicalEmail
	 */
	public String getPhysicalEmail() {
		return physicalEmail;
	}

	/**
	 * @param physicalEmail the physicalEmail to set
	 */
	public void setPhysicalEmail(String physicalEmail) {
		this.physicalEmail = physicalEmail;
	}

	/**
	 * @return the firm
	 */
	public String getFirm() {
		return firm;
	}

	/**
	 * @param firm the firm to set
	 */
	public void setFirm(String firm) {
		this.firm = firm;
	}

	/**
	 * @return the atCern
	 */
	public String getAtCern() {
		return atCern;
	}

	/**
	 * @param atCern the atCern to set
	 */
	public void setAtCern(String atCern) {
		this.atCern = atCern;
	}

	/**
	 * @return the personClass
	 */
	public String getPersonClass() {
		return personClass;
	}

	/**
	 * @param personClass the personClass to set
	 */
	public void setPersonClass(String personClass) {
		this.personClass = personClass;
	}

	/**
	 * @return the supervisorOfExternalStuff
	 */
	public Boolean getSupervisorOfExternalStuff() {
		return supervisorOfExternalStuff;
	}

	/**
	 * @param supervisorOfExternalStuff the supervisorOfExternalStuff to set
	 */
	public void setSupervisorOfExternalStuff(Boolean supervisorOfExternalStuff) {
		this.supervisorOfExternalStuff = supervisorOfExternalStuff;
	}

	/**
	 * @return the nationality1
	 */
	public Country getNationality1() {
		return nationality1;
	}

	/**
	 * @param nationality1 the nationality1 to set
	 */
	public void setNationality1(Country nationality1) {
		this.nationality1 = nationality1;
	}


	/**
	 * @return the institute
	 */
	public Institute getInstitute() {
		return institute;
	}

	/**
	 * @param institute the institute to set
	 */
	public void setInstitute(Institute institute) {
		this.institute = institute;
	}

	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the processingStartDate
	 */
	public Date getProcessingStartDate() {
		return processingStartDate;
	}

	/**
	 * @param processingStartDate the processingStartDate to set
	 */
	public void setProcessingStartDate(Date processingStartDate) {
		this.processingStartDate = processingStartDate;
	}

	/**
	 * @return the processingEndDate
	 */
	public Date getProcessingEndDate() {
		return processingEndDate;
	}

	/**
	 * @param processingEndDate the processingEndDate to set
	 */
	public void setProcessingEndDate(Date processingEndDate) {
		this.processingEndDate = processingEndDate;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	
	/**
	 * @return the participations
	 */
	public Set<Participation> getParticipations() {
		return participations;
	}

	/**
	 * @param participations the participations to set
	 */
	public void setParticipations(Set<Participation> participations) {
		this.participations = participations;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VOMSOrgDBPerson other = (VOMSOrgDBPerson) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("VOMSPerson [id=").append(id).append(", firstName=")
				.append(firstName).append(", name=").append(name).append(
						", email=").append(email).append(", physicalEmail=")
				.append(physicalEmail).append("]");
		return builder.toString();
	}
	
	
	public Participation getLastExpiredParticipationForExperiment(String experimentName){
		
		Participation lastExpiredParticipation = null;
		
		Date now = new Date();
		
		for (Participation p: participations){
			
			if (p.getExperiment().getName().equals(experimentName)){
				if (p.getEndDate() != null && p.getEndDate().before(now)){
					
					if ((lastExpiredParticipation == null) || ((lastExpiredParticipation != null) && p.getEndDate().after(lastExpiredParticipation.getEndDate())))
						lastExpiredParticipation = p;
				}
			}
		}
		
		return lastExpiredParticipation;
		
	}
	
	public Set<Participation> getValidParticipations(){
		Date now = new Date();
		
		HashSet<Participation> result = new HashSet<Participation>();
		
		for (Participation p: participations){
			
			if (p.getStartDate().before(now)){
				
				if (p.getEndDate() == null)
					result.add(p);
				else if (p.getEndDate().after(now))
					result.add(p);
			}
			
		}
		
		return result;
	}

	public boolean hasValidParticipationForExperiment(String experimentName){
		
		return getValidParticipationForExperiment(experimentName) != null;
	}
	
	public Participation getValidParticipationForExperiment(String experimentName){
		Date now = new Date();
		for (Participation p: participations){
			
			if (p.getExperiment().getName().equals(experimentName)){
				
				if (p.getStartDate().before(now)){
					
					if (p.getEndDate() == null)
						return p;
					
					if (p.getEndDate().after(now))
							return p;
				}
			}
		}
		
		return null;
	}
	
}
