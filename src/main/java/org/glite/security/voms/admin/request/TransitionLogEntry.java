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

package org.glite.security.voms.admin.request;

import java.util.Date;

public class TransitionLogEntry implements Comparable {

	private Date time;

	private State initialState;

	private State finalState;

	private Event cause;

	private StateMachineException exception;

	private TransitionLogEntry(Date time, State s, State t, Event c) {

		this.time = time;
		initialState = s;
		finalState = t;
		cause = c;
	}

	private TransitionLogEntry(Date time, State s, StateMachineException ex) {

		this.time = time;
		initialState = s;
		exception = ex;
	}

	public StateMachineException getException() {

		return exception;
	}

	public void setException(StateMachineException exception) {

		this.exception = exception;
	}

	public Event getCause() {

		return cause;
	}

	public void setCause(Event cause) {

		this.cause = cause;
	}

	public State getFinalState() {

		return finalState;
	}

	public void setFinalState(State finalState) {

		this.finalState = finalState;
	}

	public Date getTime() {

		return time;
	}

	public void setTime(Date time) {

		this.time = time;
	}

	public State getInitialState() {

		return initialState;
	}

	public void setInitialState(State initialState) {

		this.initialState = initialState;
	}

	public static TransitionLogEntry instance(State initialState,
			State finalState, Event cause) {

		TransitionLogEntry entry = new TransitionLogEntry(new Date(),
				initialState, finalState, cause);
		return entry;
	}

	public static TransitionLogEntry instance(State initialState,
			StateMachineException e) {

		TransitionLogEntry entry = new TransitionLogEntry(new Date(),
				initialState, e);
		return entry;
	}

	public int compareTo(Object o) {

		if (this.equals(o))
			return 0;
		TransitionLogEntry that = (TransitionLogEntry) o;
		return this.time.compareTo(that.time);
	}

	public String toString() {

		StringBuffer buf = new StringBuffer();
		buf.append("<");
		buf.append(time);
		buf.append("," + initialState);

		if (exception == null) {

			buf.append(":" + cause + "-->" + finalState);

		} else
			buf.append(": exception " + exception);

		buf.append(">");
		return buf.toString();
	}

}
