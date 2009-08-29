package org.glite.security.voms.admin.view.preparers.role;

import java.util.List;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;

public class RoleDetailPreparer extends ViewPreparerSupport{

	@Override
	public void execute(TilesRequestContext tilesContext,
			AttributeContext attributeContext) throws PreparerException {
		
		List<VOMSGroup> groups = (List<VOMSGroup>) ListGroupsOperation.instance().execute();
		tilesContext.getRequestScope().put("voGroups", groups);
		
	}
}
