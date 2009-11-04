package org.glite.security.voms.admin.database;

import org.glite.security.voms.admin.common.VOMSException;

public class SuspendedUserException extends VOMSException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public SuspendedUserException(String message) {
		super(message);
	}

}
