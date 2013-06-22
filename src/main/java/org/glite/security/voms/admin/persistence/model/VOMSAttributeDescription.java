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
package org.glite.security.voms.admin.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.glite.security.voms.service.attributes.AttributeClass;

@Entity
@Table(name="attributes")
public class VOMSAttributeDescription implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "a_id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator="VOMS_ATTR_DESC_SEQ")
	@SequenceGenerator(name="VOMS_ATTR_DESC_SEQ", sequenceName="VOMS_ATTR_DESC_SEQ")
	Long id;

	@Column(name="a_name", nullable=false, unique=true)
	String name;

	@Column(name="a_desc", columnDefinition="text")
	String description;
	
	@Column(name="a_uniq", nullable=false)
	Boolean unique = new Boolean(false);

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public boolean isUnique() {

		return unique.booleanValue();
	}

	public Boolean getUnique() {
		return unique;
	}

	public void setUnique(Boolean unique) {
		this.unique = unique;
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof VOMSAttributeDescription))
			return false;

		if (other == null)
			return false;

		VOMSAttributeDescription that = (VOMSAttributeDescription) other;

		return (getName().equals(that.getName()));
	}

	public int hashCode() {

		return getName().hashCode();
	}

	public VOMSAttributeDescription() {

		super();

	}

	public VOMSAttributeDescription(String name, String desc) {

		this.name = name;
		this.description = desc;
	}

	public VOMSAttributeDescription(String name, String desc, boolean uniq) {

		this.name = name;
		this.description = desc;
		this.unique = new Boolean(uniq);
	}

	public AttributeClass asAttributeClass() {

		AttributeClass ac = new AttributeClass();

		ac.setName(getName());
		ac.setDescription(getDescription());
		ac.setUniquenessChecked(getUnique().booleanValue());

		return ac;

	}
}
