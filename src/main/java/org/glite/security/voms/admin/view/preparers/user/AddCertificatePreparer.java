package org.glite.security.voms.admin.view.preparers.user;

import java.util.List;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.glite.security.voms.admin.dao.VOMSCADAO;
import org.glite.security.voms.admin.model.VOMSCA;

public class AddCertificatePreparer extends ViewPreparerSupport {
	
	@Override
	public void execute(TilesRequestContext tilesContext,
			AttributeContext attributeContext) throws PreparerException {
		
		
		List<VOMSCA> trustedCas = VOMSCADAO.instance().getValid();
		tilesContext.getRequestScope().put("trustedCas", trustedCas);
		
	}

}
