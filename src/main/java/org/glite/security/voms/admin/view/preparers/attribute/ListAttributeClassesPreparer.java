package org.glite.security.voms.admin.view.preparers.attribute;

import java.util.List;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.glite.security.voms.admin.model.VOMSAttributeDescription;
import org.glite.security.voms.admin.operations.attributes.ListAttributeDescriptionsOperation;

public class ListAttributeClassesPreparer extends ViewPreparerSupport {

	@Override
	public void execute(TilesRequestContext tilesContext,
			AttributeContext attributeContext) throws PreparerException {

		List<VOMSAttributeDescription> attributeClasses = (List<VOMSAttributeDescription>) ListAttributeDescriptionsOperation
				.instance().execute();

		tilesContext.getRequestScope()
				.put("attributeClasses", attributeClasses);
	}

}
