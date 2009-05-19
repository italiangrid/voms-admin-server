package org.glite.security.voms.admin.view.preparers.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparer;

public class ListUsersPreparer implements ViewPreparer {

	private static Log log = LogFactory.getLog(ListUsersPreparer.class);
	
	public void execute(TilesRequestContext context, AttributeContext attributeContext)
			throws PreparerException {
		
		log.debug("preparing view!");
		
	}

}
