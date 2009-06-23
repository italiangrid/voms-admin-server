package org.glite.security.voms.admin.view.preparers.group;

import java.util.List;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;

public class ListGroupPreparer extends ViewPreparerSupport {

	public void execute(TilesRequestContext requestContext, AttributeContext attributeContext)
			throws PreparerException {
		
		
		List<VOMSGroup> groups = (List<VOMSGroup>) ListGroupsOperation.instance().execute();
		
		requestContext.getRequestScope().put("voGroups", groups);
		
		
	}

}
