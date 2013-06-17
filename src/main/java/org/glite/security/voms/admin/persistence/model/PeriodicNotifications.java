package org.glite.security.voms.admin.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="periodic_notifications")
public class PeriodicNotifications {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="VOMS_PER_NOT_SEQ")
	@SequenceGenerator(name="VOMS_PER_NOT_SEQ", sequenceName="VOMS_PER_NOT_SEQ")
	private Long id;
	
	@Column(nullable=false, unique=true)
	private String notificationType;
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastNotificationTime;
	
	public PeriodicNotifications() {}	
	
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
	 * @return the notificationType
	 */
	public String getNotificationType() {
	
		return notificationType;
	}



	
	/**
	 * @param notificationType the notificationType to set
	 */
	public void setNotificationType(String notificationType) {
	
		this.notificationType = notificationType;
	}



	
	/**
	 * @return the lastNotificationTime
	 */
	public Date getLastNotificationTime() {
	
		return lastNotificationTime;
	}



	
	/**
	 * @param lastNotificationTime the lastNotificationTime to set
	 */
	public void setLastNotificationTime(Date lastNotificationTime) {
	
		this.lastNotificationTime = lastNotificationTime;
	}



	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result
			+ ((notificationType == null) ? 0 : notificationType.hashCode());
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeriodicNotifications other = (PeriodicNotifications) obj;
		if (notificationType == null) {
			if (other.notificationType != null)
				return false;
		} else if (!notificationType.equals(other.notificationType))
			return false;
		return true;
	}


	@Override
	public String toString() {

		return "PeriodicNotifications [id=" + id + ", notificationType="
			+ notificationType + ", lastNotificationTime=" + lastNotificationTime
			+ "]";
	}

}
