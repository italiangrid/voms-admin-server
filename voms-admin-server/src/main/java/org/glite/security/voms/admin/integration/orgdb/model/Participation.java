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
package org.glite.security.voms.admin.integration.orgdb.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Immutable
@Table(name = "PERSON_PARTICIPATION")
public class Participation implements Serializable {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private Id id = new Id();

  @Temporal(TemporalType.DATE)
  @Column(name = "END_DATE")
  private Date endDate;

  @Column(name = "PRIMARY", length = 1)
  private String primary;

  @Column(name = "GREYBOOK", length = 1)
  private String greybook;

  @Column(name = "TELEPHONE_NUMBER", length = 30)
  String telephoneNumber;

  @Column(name = "FAX_NUMBER", length = 30)
  String faxNumber;

  @ManyToOne
  @JoinColumn(name = "PERSON_ID", insertable = false, updatable = false)
  VOMSOrgDBPerson vomsPerson;

  @ManyToOne
  @NotFound(action= NotFoundAction.IGNORE) // Should never be true for active participations
  @JoinColumn(name = "INSTITUTE", insertable = false, updatable = false)
  Institute institute;

  @ManyToOne
  @NotFound(action= NotFoundAction.IGNORE) // Should never be true for active participations
  @JoinColumn(name = "EXPERIMENT", insertable = false, updatable = false)
  Experiment experiment;

  // FIXME: This subexperiment is expressed as a name here since this column can
  // contain the value '-' which doesn't have
  // a corresponding entry in the EXPERIMENTS table. This seems broken
  // referential integrity in the ORGDB voms view, so it's
  // safer to just put here the subexperiment name (mostly null, btw) and let
  // interested people do the join by hand.
  @Column(name = "SUBEXPERIMENT")
  String subexperimentName;

  @Embeddable
  public static class Id implements Serializable {

    /**
		 * 
		 */
    private static final long serialVersionUID = 1L;

    @Column(name = "PERSON_ID")
    private Long personId;

    @Column(name = "INSTITUTE")
    private String instituteId;

    @Column(name = "EXPERIMENT")
    private String experimentId;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

    public Id() {

    }

    /**
     * @return the personId
     */
    public Long getPersonId() {

      return personId;
    }

    /**
     * @param personId
     *          the personId to set
     */
    public void setPersonId(Long personId) {

      this.personId = personId;
    }

    /**
     * @return the instituteId
     */
    public String getInstituteId() {

      return instituteId;
    }

    /**
     * @param instituteId
     *          the instituteId to set
     */
    public void setInstituteId(String instituteId) {

      this.instituteId = instituteId;
    }

    /**
     * @return the experimentId
     */
    public String getExperimentId() {

      return experimentId;
    }

    /**
     * @param experimentId
     *          the experimentId to set
     */
    public void setExperimentId(String experimentId) {

      this.experimentId = experimentId;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {

      return startDate;
    }

    /**
     * @param startDate
     *          the startDate to set
     */
    public void setStartDate(Date startDate) {

      this.startDate = startDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

      final int prime = 31;
      int result = 1;
      result = prime * result
        + ((experimentId == null) ? 0 : experimentId.hashCode());
      result = prime * result
        + ((instituteId == null) ? 0 : instituteId.hashCode());
      result = prime * result + ((personId == null) ? 0 : personId.hashCode());
      result = prime * result
        + ((startDate == null) ? 0 : startDate.hashCode());
      return result;
    }

    /*
     * (non-Javadoc)
     * 
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
      Id other = (Id) obj;
      if (experimentId == null) {
        if (other.experimentId != null)
          return false;
      } else if (!experimentId.equals(other.experimentId))
        return false;
      if (instituteId == null) {
        if (other.instituteId != null)
          return false;
      } else if (!instituteId.equals(other.instituteId))
        return false;
      if (personId == null) {
        if (other.personId != null)
          return false;
      } else if (!personId.equals(other.personId))
        return false;
      if (startDate == null) {
        if (other.startDate != null)
          return false;
      } else if (!startDate.equals(other.startDate))
        return false;
      return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

      StringBuilder builder = new StringBuilder();
      builder.append("Id [personId=").append(personId).append("]");
      return builder.toString();
    }

  }

  /**
   * @return the id
   */
  public Id getId() {

    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(Id id) {

    this.id = id;
  }

  /**
   * @return the endDate
   */
  public Date getEndDate() {

    return endDate;
  }

  /**
   * @param endDate
   *          the endDate to set
   */
  public void setEndDate(Date endDate) {

    this.endDate = endDate;
  }

  /**
   * @return the primary
   */
  public String getPrimary() {

    return primary;
  }

  /**
   * @param primary
   *          the primary to set
   */
  public void setPrimary(String primary) {

    this.primary = primary;
  }

  /**
   * @return the greybook
   */
  public String getGreybook() {

    return greybook;
  }

  /**
   * @param greybook
   *          the greybook to set
   */
  public void setGreybook(String greybook) {

    this.greybook = greybook;
  }

  /**
   * @return the telephoneNumber
   */
  public String getTelephoneNumber() {

    return telephoneNumber;
  }

  /**
   * @param telephoneNumber
   *          the telephoneNumber to set
   */
  public void setTelephoneNumber(String telephoneNumber) {

    this.telephoneNumber = telephoneNumber;
  }

  /**
   * @return the faxNumber
   */
  public String getFaxNumber() {

    return faxNumber;
  }

  /**
   * @param faxNumber
   *          the faxNumber to set
   */
  public void setFaxNumber(String faxNumber) {

    this.faxNumber = faxNumber;
  }

  /**
   * @return the vomsPerson
   */
  public VOMSOrgDBPerson getVomsPerson() {

    return vomsPerson;
  }

  /**
   * @param vomsPerson
   *          the vomsPerson to set
   */
  public void setVomsPerson(VOMSOrgDBPerson vomsPerson) {

    this.vomsPerson = vomsPerson;
  }

  /**
   * @return the institute
   */
  public Institute getInstitute() {

    return institute;
  }

  /**
   * @param institute
   *          the institute to set
   */
  public void setInstitute(Institute institute) {

    this.institute = institute;
  }

  /**
   * @return the experiment
   */
  public Experiment getExperiment() {

    return experiment;
  }

  /**
   * @param experiment
   *          the experiment to set
   */
  public void setExperiment(Experiment experiment) {

    this.experiment = experiment;
  }

  /**
   * @return
   * @see org.glite.security.voms.admin.integration.orgdb.model.Participation.Id#getStartDate()
   */
  public Date getStartDate() {

    return id.getStartDate();
  }

  public void setStartDate(Date startDate) {

    id.setStartDate(startDate);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
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
    Participation other = (Participation) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {

    StringBuilder builder = new StringBuilder();
    builder.append("Participation [endDate=").append(endDate)
      .append(", experiment=").append(experiment).append(", getStartDate()=")
      .append(getStartDate()).append("]");
    return builder.toString();
  }

}
