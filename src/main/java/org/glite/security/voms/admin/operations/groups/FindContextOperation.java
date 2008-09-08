package org.glite.security.voms.admin.operations.groups;

import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.common.PathNamingScheme;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;


public class FindContextOperation extends BaseVomsOperation {

    
    VOMSContext theContext = null;
    
    String contextString = null;
    
    
    private FindContextOperation(String contextString) {
        
        if ( contextString == null )
            throw new NullArgumentException( "contextString cannot be null!" );
            
        PathNamingScheme.checkSyntax( contextString );
        
        this.contextString = contextString; 
    }
    
    public static FindContextOperation instance(String contextString) {

        return new FindContextOperation(contextString);
    }
    
    
    @Override
    protected Object doExecute() {

        String groupName = PathNamingScheme.getGroupName( contextString );
        VOMSGroup g = VOMSGroupDAO.instance().findByName( groupName );
        
        if (PathNamingScheme.isQualifiedRole( contextString )){
            
            String roleName = PathNamingScheme.getRoleName( contextString );
            VOMSRole r = VOMSRoleDAO.instance().findByName( roleName );
            
            return VOMSContext.instance( g, r );
        }
        
        return VOMSContext.instance( g );
    }

    @Override
    protected void setupPermissions() {

        // A group part must be present in the contextString
        VOMSGroup g = VOMSGroupDAO.instance().findByName( PathNamingScheme.getGroupName( contextString ));
        addRequiredPermissionOnPath( g, VOMSPermission.getContainerReadPermission());
                
    }

}
