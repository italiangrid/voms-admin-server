package org.glite.security.voms.admin.model.task;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.glite.security.voms.admin.model.request.Request;

@Entity
@Table(name = "user_request_task")
public class ApproveUserRequestTask extends Task implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "req_id", updatable = false, nullable = false)
	Request request;

	public ApproveUserRequestTask() {

	}

	/**
	 * @return the request
	 */
	public Request getRequest() {

		return request;
	}

	/**
	 * @param request
	 *            the request to set
	 */
	public void setRequest(Request request) {

		this.request = request;
	}

}
