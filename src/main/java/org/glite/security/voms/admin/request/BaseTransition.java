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

public class BaseTransition implements Transition {

	State targetState;

	Event event;

	public BaseTransition(Event e, State s) {

		this.event = e;
		this.targetState = s;
	}

	public Event getEvent() {

		return event;
	}

	public State getTargetState() {

		return targetState;
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;
		if (!(other instanceof BaseTransition))
			return false;

		BaseTransition that = (BaseTransition) other;
		if (this.event.equals(that.event))
			return true;

		return false;
	}

	public int hashCode() {

		int result = 14;

		result = 29 * result + event.hashCode();

		return result;
	}

	public String toString() {

		return event + "->" + targetState;
	}

}
