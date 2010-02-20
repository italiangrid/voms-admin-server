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
package org.glite.security.voms.admin.view.preparers.user;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparer;
import org.glite.security.voms.admin.error.VOMSAuthorizationException;
import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;
import org.glite.security.voms.admin.operations.roles.ListRolesOperation;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;

public class ListUsersPreparer implements ViewPreparer {

	private static Log log = LogFactory.getLog(ListUsersPreparer.class);

	public void execute(TilesRequestContext context,
			AttributeContext attributeContext) throws PreparerException {

		List<VOMSGroup> groups;
		List<VOMSRole> roles; 
		
		try{
			
			groups = (List<VOMSGroup>) ListGroupsOperation.instance().execute();
			roles = (List<VOMSRole>)ListRolesOperation.instance().execute();
			
		}catch(VOMSAuthorizationException e){
			
			log.warn(e.getMessage());
			if (log.isDebugEnabled())
				log.warn(e.getMessage(),e);
			
			groups = Collections.EMPTY_LIST;
			roles = Collections.EMPTY_LIST;
		}
		
		context.getRequestScope().put("voGroups", groups);
		context.getRequestScope().put("voRoles", roles);
		
	}

}
