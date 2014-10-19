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
 * This class models a Generic Attribute (GA) value in the VOMS database.
 * 
 * 
 * @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti</a>
 */
public class AttributeValue implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	/**
	 * This GA value attribute class.
	 */
	AttributeClass attributeClass;
	
	/**
	 * The context qualifier for this GA value.
	 */
	String context;
	
	/**
	 * The GA value.
	 */
	String value;

	/**
	 * Returns the {@link AttributeClass} related to this GA value.
	 * 
	 * @return
	 * this GA value attribute class
	 */
	public AttributeClass getAttributeClass() {

		return attributeClass;
	}

	/**
	 * Sets the {@link AttributeClass} for this GA value object.
	 * 
	 * @param attributeClass
	 * the GA value attribute class
	 */
	public void setAttributeClass(AttributeClass attributeClass) {

		this.attributeClass = attributeClass;
	}

	/** 
	 * Returns the context qualifier for this GA value.
	 * 
	 * <ul>
	 * 	<li>If the attribute was defined at user scope, the context will be the VO name.</li>
	 * 	<li>If the attributes was defined at group scope, the context will be the group name.</li>
	 * 	<li>If the attribute was defined at role scope, the context will be the qualified role name.</li>
	 * </ul>
	 * 
	 * @return
	 * A string representing the context for this GA. 
	 */
	public String getContext() {

		return context;
	}

	/**
	 * Sets the context for this GA value. VOMS will set this for you, so you shouldn't worry. 
	 * 
	 * @param context
	 * A string representing the context for this GA.
	 */
	public void setContext(String context) {

		this.context = context;
	}

	/**
	 * Returns the GA value. 
	 * 
	 * @return
	 * A string representing the GA value.
	 */
	public String getValue() {

		return value;
	}
	
	/**
	 * Sets the GA value.
	 * 
	 * @param value
	 * A string representing the GA value.
	 */
	public void setValue(String value) {

		this.value = value;
	}

}
