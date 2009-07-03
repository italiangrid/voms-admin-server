package org.glite.security.voms.admin.model.task;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.glite.security.voms.admin.model.task.Task.TaskStatus;

@Entity
@Table(name = "task_log_record")
public class LogRecord implements Serializable, Comparable<LogRecord> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	TaskStatus event;

	@Column(nullable = false)
	Date date;

	@ManyToOne
	@JoinColumn(name = "task_id", nullable = false)
	Task task;

	String adminDn;

	String userDn;

	public LogRecord() {

		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public Long getId() {

		return id;
	}

	/**
	 * @return the event
	 */
	public TaskStatus getEvent() {

		return event;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {

		return date;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * @param event
	 *            the event to set
	 */
	public void setEvent(TaskStatus event) {

		this.event = event;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {

		this.date = date;
	}

	public int compareTo(LogRecord o) {

		return new CompareToBuilder().
			append(task,o.task)
			.append(date, o.date).append(event, o.event).toComparison();
	}

	/**
	 * @return the task
	 */
	public Task getTask() {

		return task;
	}

	/**
	 * @param task
	 *            the task to set
	 */
	public void setTask(Task task) {

		this.task = task;
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;
		if (!(other instanceof LogRecord))
			return false;

		if (other == null)
			return false;

		LogRecord that = (LogRecord) other;

		EqualsBuilder builder = new EqualsBuilder();

		return builder.append(task, that.task).append(date, that.date).append(
				event, that.event).isEquals();

	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 35).append(task).append(date).append(
				event).toHashCode();

	}

	public String getAdminDn() {
		return adminDn;
	}

	public void setAdminDn(String adminDn) {
		this.adminDn = adminDn;
	}

	public String getUserDn() {
		return userDn;
	}

	public void setUserDn(String userDn) {
		this.userDn = userDn;
	}
	
	
}
