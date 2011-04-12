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
package org.glite.security.voms.admin.api.attributes;

import java.io.Serializable;

/**
 * This class models a Generic Attribute (GA) in the VOMS database.
 * 
 * @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti</a>
 *
 */
public class AttributeClass implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	/**
	 * The GA name
	 */
	private String name;
	
	/**
	 * A textual description for the GA
	 */
	private String description;
	
	/**
	 * A boolean flag that states whether attribute value uniqueness across VOMS users should be enforced for this GA
	 */
	private boolean uniquenessChecked;

	/**
	 * Returns the description associated to this GA
	 * 
	 * @return
	 * the GA description
	 */
	public String getDescription() {

		return description;
	}

	/**
	 * Sets a description of this GA
	 * 
	 * @param description
	 * the GA description
	 */
	public void setDescription(String description) {

		this.description = description;
	}

	/**
	 * Returns the GA name
	 * 
	 * @return
	 * the GA name
	 */
	public String getName() {

		return name;
	}

	/**
	 * Sets a name for this GA
	 * 
	 * @param name
	 * the GA name
	 */
	public void setName(String name) {

		this.name = name;
	}

	/**
	 * Tests whether uniqueness of values across VOMS users is checked for this GA 
	 * 
	 * @return
	 * <code>true</code> if uniqueness is checked, <code>false</code> otherwise.
	 */
	public boolean isUniquenessChecked() {

		return uniquenessChecked;
	}
	

	/**
	 * Sets whether uniqueness of values should be checked across VOMS users for this GA
	 * 
	 * @param uniquenessChecked
	 * <code>true</code> to enable uniqueness value checking, <code>false</code> otherwise
	 */
	public void setUniquenessChecked(boolean uniquenessChecked) {

		this.uniquenessChecked = uniquenessChecked;
	}

}
