package org.glite.security.voms.admin.operations.acls;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSAuthorizationException;
import org.glite.security.voms.admin.dao.ACLDAO;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.model.ACL;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSPermission;


public class SaveACLEntryOperation extends BaseVomsOperation {

    private static final Log log = LogFactory
            .getLog( SaveACLEntryOperation.class );
    
    private ACL acl;
    private VOMSAdmin admin;
    private VOMSPermission perms;
    
    private boolean recursive = false;
    
    protected Object doExecute() {

        ACLDAO.instance().saveACLEntry( acl, admin, perms );
        
        if (isRecursive() && acl.getContext().isGroupContext()){
            
            try{
                
                List childrenGroups = VOMSGroupDAO.instance().getChildren( acl.getGroup() );
                Iterator childIter = childrenGroups.iterator();
                
                while (childIter.hasNext()){
                    
                    VOMSGroup childGroup = (VOMSGroup)childIter.next();
                    SaveACLEntryOperation op = instance( childGroup.getACL(), admin, perms ,true);
                    op.execute();
                
                }
                
            }catch (VOMSAuthorizationException e) {
                    
                log.warn( "Authorization Error saving recursively ACL entry !", e );
                    
            }catch (RuntimeException e) {
                
                throw e;
            }
        }
        return acl;
        
    }

    protected void setupPermissions() {

        VOMSPermission requiredPerms = null;
        
        if (acl.isDefautlACL())
            requiredPerms = VOMSPermission.getEmptyPermissions().setACLDefaultPermission().setACLReadPermission().setACLWritePermission();
        else
            requiredPerms = VOMSPermission.getEmptyPermissions().setACLReadPermission().setACLWritePermission();
           
        addRequiredPermission( acl.getContext(), requiredPerms );
    }
    
    
    private SaveACLEntryOperation(ACL acl, 
            VOMSAdmin admin, VOMSPermission perms, boolean recursive) {

        this.acl = acl;
        this.admin = admin;
        this.perms = perms;
        this.recursive = recursive;
        
    }
    
    public static SaveACLEntryOperation instance(ACL acl, 
                VOMSAdmin admin, VOMSPermission perms) {

        return new SaveACLEntryOperation(acl,admin,perms, false);
    }

    public static SaveACLEntryOperation instance(ACL acl, 
            VOMSAdmin admin, VOMSPermission perms, boolean recursive) {

        return new SaveACLEntryOperation(acl,admin,perms, recursive);
    }
    
    public boolean isRecursive() {
    
        return recursive;
    }

    
    public void setRecursive( boolean recursive ) {
    
        this.recursive = recursive;
    }
    
    

}
