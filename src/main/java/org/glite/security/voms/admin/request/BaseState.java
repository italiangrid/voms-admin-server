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

public class BaseState implements State {

	String name;

	ActionSequence actions = new ActionSequence();

	public BaseState(String name) {

		this.name = name;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public boolean equals(Object other) {

		// TODO Auto-generated method stub
		if (this == other)
			return true;
		if (!(other instanceof BaseState))
			return false;

		BaseState that = (BaseState) other;
		return name.equals(that.name);

	}

	public int hashCode() {

		return name.hashCode();
	}

	public String toString() {

		return "s(" + name + ")";
	}

	public ActionSequence getActions() {

		return actions;
	}

	public Object executeActions() {

		return actions.execute();
	}

	public void addAction(Action a) {

		actions.addAction(a);
	}

	public void removeAction(Action a) {

		actions.removeAction(a);
	}

}
