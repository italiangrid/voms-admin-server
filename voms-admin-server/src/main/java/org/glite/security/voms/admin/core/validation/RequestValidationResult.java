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

package org.glite.security.voms.admin.core.validation;

import java.util.List;


public class RequestValidationResult {
	
	
	private static final RequestValidationResult SUCCESS_RESULT = new RequestValidationResult(Outcome.SUCCESS, null, null);
	
	public enum Outcome{
		SUCCESS,
		FAILURE,
		ERROR
	}
	
	Outcome outcome;
	String message;
	Throwable exception;
		
	List<String> errorMessages;
	List<String> warningMessages;
	
	
	private RequestValidationResult(Outcome status, String message, Throwable exception) {
		
		setOutcome(status);
		setMessage(message);
		setException(exception);
	}
	
	/**
	 * @return the outcome
	 */
	public Outcome getOutcome() {
		return outcome;
	}

	/**
	 * @param outcome the outcome to set
	 */
	public void setOutcome(Outcome outcome) {
		this.outcome = outcome;
	}
	
	

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the exception
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(Throwable exception) {
		this.exception = exception;
	}

	
	public static RequestValidationResult success(){
		return SUCCESS_RESULT;
	}
	
	
	public static RequestValidationResult failure(String message){
		
		return new RequestValidationResult(Outcome.FAILURE, message, null);
	}
	
	public static RequestValidationResult error(String message, Throwable exception){
		
		return new RequestValidationResult(Outcome.ERROR, message, exception);
	}

	/**
	 * @return the errorMessages
	 */
	public List<String> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * @param errorMessages the errorMessages to set
	 */
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	/**
	 * @return the warningMessages
	 */
	public List<String> getWarningMessages() {
		return warningMessages;
	}

	/**
	 * @param warningMessages the warningMessages to set
	 */
	public void setWarningMessages(List<String> warningMessages) {
		this.warningMessages = warningMessages;
	}
	
	
}
