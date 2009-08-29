package org.glite.security.voms.admin.view.preparers.acl;

import java.util.List;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.glite.security.voms.admin.dao.VOMSCADAO;
import org.glite.security.voms.admin.model.VOMSCA;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;
import org.glite.security.voms.admin.operations.roles.ListRolesOperation;
import org.glite.security.voms.admin.operations.users.ListUsersOperation;

public class AddEntryPreparer extends ViewPreparerSupport {

	@Override
	public void execute(TilesRequestContext tilesContext,
			AttributeContext attributeContext) throws PreparerException {

		List<VOMSGroup> groups = (List<VOMSGroup>) ListGroupsOperation
				.instance().execute();
		List<VOMSRole> roles = (List<VOMSRole>) ListRolesOperation.instance()
				.execute();
		List<VOMSUser> users = (List<VOMSUser>) ListUsersOperation.instance()
				.execute();
		List<VOMSCA> cas = (List<VOMSCA>) VOMSCADAO.instance().getValid();

		tilesContext.getRequestScope().put("voUsers", users);
		tilesContext.getRequestScope().put("voGroups", groups);
		tilesContext.getRequestScope().put("voRoles", roles);
		tilesContext.getRequestScope().put("voCAs", cas);
	}

}
