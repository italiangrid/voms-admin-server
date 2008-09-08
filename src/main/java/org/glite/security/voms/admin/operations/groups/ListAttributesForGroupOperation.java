package org.glite.security.voms.admin.operations.groups;

import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;


public class ListAttributesForGroupOperation extends BaseVomsOperation {

    
    VOMSGroup _vomsGroup;
    
    private ListAttributesForGroupOperation(VOMSGroup g) {

        _vomsGroup = g;
        
    }
    
    protected Object doExecute() {

        return _vomsGroup.getAttributes();
    }

    protected void setupPermissions() {

        addRequiredPermissionOnPath( _vomsGroup.getParent(), 
                VOMSPermission.getContainerReadPermission() );
        
        addRequiredPermission( VOMSContext.instance( _vomsGroup ), 
                VOMSPermission.getEmptyPermissions().setAttributesReadPermission() );
       
    }

    public static ListAttributesForGroupOperation instance(VOMSGroup g) {

        return new ListAttributesForGroupOperation(g);
    }
}
