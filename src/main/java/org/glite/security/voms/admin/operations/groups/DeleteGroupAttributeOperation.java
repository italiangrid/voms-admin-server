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
package org.glite.security.voms.admin.operations.groups;

import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.operations.BaseAttributeRWOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;

public class DeleteGroupAttributeOperation extends BaseAttributeRWOperation {

	String attributeName;

	private DeleteGroupAttributeOperation(VOMSGroup g, String aName) {
		super(VOMSContext.instance(g));
		attributeName = aName;
	}

	protected Object doExecute() {

		VOMSGroupDAO.instance().deleteAttribute(__context.getGroup(),
				attributeName);
		return null;
	}

	public static DeleteGroupAttributeOperation instance(VOMSGroup g,
			String aName) {

		return new DeleteGroupAttributeOperation(g, aName);
	}

	public static DeleteGroupAttributeOperation instance(String groupName,
			String aName) {

		VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(groupName)
				.execute();

		if (g == null)
			throw new NoSuchGroupException("Group '" + groupName
					+ "' does not exist in this vo.");

		return new DeleteGroupAttributeOperation(g, aName);
	}

}
