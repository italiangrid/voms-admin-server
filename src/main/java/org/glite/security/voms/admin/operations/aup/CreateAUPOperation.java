package org.glite.security.voms.admin.operations.aup;

import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;


public class CreateAUPOperation extends BaseVomsOperation {
    
    AUP aup;
    
    
    protected CreateAUPOperation(AUP a) {

        aup = a;
    }
    
    public static CreateAUPOperation instance(AUP a) {

        return new CreateAUPOperation(a);
    }
    
    @Override
    protected Object doExecute() {

        AUPDAO dao  = DAOFactory.instance().getAUPDAO();
        
        dao.makePersistent( aup );
        
        return null;
    }

    @Override
    protected void setupPermissions() {

        addRequiredPermission( VOMSContext.getVoContext(), VOMSPermission.getAllPermissions() );

    }

}
