package org.glite.security.voms.admin.view.preparers.user;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparer;
import org.glite.security.voms.admin.common.VOMSAuthorizationException;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;
import org.glite.security.voms.admin.operations.roles.ListRolesOperation;

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
