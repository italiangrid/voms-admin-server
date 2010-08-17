package org.glite.security.voms.admin.integration;

import java.util.List;


public class ValidationResult {
	
	
	private static final ValidationResult SUCCESS_RESULT = new ValidationResult(Outcome.SUCCESS, null, null);
	
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
	
	
	private ValidationResult(Outcome status, String message, Throwable exception) {
		
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

	
	public static ValidationResult success(){
		return SUCCESS_RESULT;
	}
	
	
	public static ValidationResult failure(String message){
		
		return new ValidationResult(Outcome.FAILURE, message, null);
	}
	
	public static ValidationResult error(String message, Throwable exception){
		
		return new ValidationResult(Outcome.ERROR, message, exception);
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
