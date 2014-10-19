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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Entity
@Table(name="group_attrs")
@MappedSuperclass
public class VOMSGroupAttribute extends VOMSBaseAttribute {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne
	@JoinColumn(name="g_id")
	VOMSGroup group;

	public VOMSGroupAttribute() {

		// TODO Auto-generated constructor stub
	}

	public static VOMSGroupAttribute instance(VOMSAttributeDescription desc,
			String value, VOMSGroup g) {

		return new VOMSGroupAttribute(desc, value, g);
	}

	protected VOMSGroupAttribute(VOMSAttributeDescription desc, String value,
			VOMSGroup g) {

		super(desc, value);
		this.group = g;

	}

	public String getContext() {

		return group.getName();
	}

	public VOMSGroup getGroup() {

		return group;
	}

	public void setGroup(VOMSGroup group) {

		this.group = group;
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof VOMSGroupAttribute))
			return false;

		if (other == null)
			return false;

		VOMSGroupAttribute that = (VOMSGroupAttribute) other;

		if (getGroup().equals(that.getGroup()))
			return getAttributeDescription().equals(
					that.getAttributeDescription());

		return false;

	}

	public int hashCode() {

		int result = 14;

		result = 29 * result + getAttributeDescription().hashCode();

		if (getGroup() != null)
			result = 29 * result + getGroup().hashCode();

		return result;

	}
}
