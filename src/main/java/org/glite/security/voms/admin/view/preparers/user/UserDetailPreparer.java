package org.glite.security.voms.admin.view.preparers.user;

import java.util.List;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.glite.security.voms.admin.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.model.VOMSAttributeDescription;

public class UserDetailPreparer extends ViewPreparerSupport {
	
	public void execute(TilesRequestContext context, AttributeContext attributeContext)
		throws PreparerException {
		
		List<VOMSAttributeDescription> attributeClasses  = VOMSAttributeDAO.instance().getAllAttributeDescriptions();
		
		context.getRequestScope().put("attributeClasses", attributeClasses);
		
	}

}
