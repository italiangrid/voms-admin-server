package org.glite.security.voms.admin.view.preparers.acl;

import java.util.Collections;
import java.util.List;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.glite.security.voms.admin.common.VOMSAuthorizationException;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;
import org.glite.security.voms.admin.operations.roles.ListRolesOperation;

public class ManagePreparer extends ViewPreparerSupport {

	@Override
	public void execute(TilesRequestContext tilesContext,
			AttributeContext attributeContext) throws PreparerException {

		List<VOMSGroup> groups = Collections.EMPTY_LIST;
		List<VOMSRole> roles = Collections.EMPTY_LIST;
		
		try{
			groups = (List<VOMSGroup>) ListGroupsOperation
					.instance().execute();
			
			roles = (List<VOMSRole>) ListRolesOperation.instance()
			.execute();
		
		
		}catch(VOMSAuthorizationException e){
			
			// swallow authorization exception
		}
			
			

		tilesContext.getRequestScope().put("voGroups", groups);
		tilesContext.getRequestScope().put("voRoles", roles);

	}
}
