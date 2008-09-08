/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/
package org.glite.security.voms.admin.actions.acl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.ACLActionForm;
import org.glite.security.voms.admin.actionforms.AddACLEntryActionForm;
import org.glite.security.voms.admin.actions.BaseDispatchAction;
import org.glite.security.voms.admin.dao.ACLDAO;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.dao.VOMSCADAO;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.database.NoSuchACLException;
import org.glite.security.voms.admin.database.NoSuchCAException;
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.database.NoSuchRoleException;
import org.glite.security.voms.admin.model.ACL;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMSCA;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.acls.DeleteACLEntryOperation;
import org.glite.security.voms.admin.operations.acls.SaveACLEntryOperation;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;
import org.glite.security.voms.admin.operations.roles.ListRolesOperation;
import org.glite.security.voms.admin.operations.users.ListUsersOperation;


public class ACLActions extends BaseDispatchAction {

    private static final Log log = LogFactory.getLog( ACLActions.class );

    public ActionForward preAddEntry( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {

        AddACLEntryActionForm aForm = (AddACLEntryActionForm) form;

        log.debug( "aForm:" + aForm );

        ACL acl;

        VOMSGroup g = null;

        boolean newACL = false;

        if ( aForm.getAclId() == null ) {
            // Only default acls are created on request

            g = (VOMSGroup) FindGroupOperation.instance( aForm.getGroupId() )
                    .execute();
            acl = ACLDAO.instance().create( g, true );
            newACL = true;
        } else
            acl = ACLDAO.instance().getById( aForm.getAclId() ); //FIXME: do it with an operation

        List users = (List) ListUsersOperation.instance().execute();

        List cas = VOMSCADAO.instance().getValid();

        List groups = (List) ListGroupsOperation.instance().execute();

        List roles = (List) ListRolesOperation.instance().execute();
        
        // FIXME: implement me with an operation!!!
        List tags = (List) VOMSAdminDAO.instance().getTagAdmins();

        request.setAttribute( "acl", acl );

        if ( newACL )
            request.setAttribute( "context", g );
        else
            request.setAttribute( "context", acl.getContext() );

        request.setAttribute( "users", users );
        request.setAttribute( "cas", cas );
        request.setAttribute( "groups", groups );
        request.setAttribute( "roles", roles );
        request.setAttribute( "tags", tags);

        aForm.reset(mapping,request);
        return findSuccess( mapping );
    }

    public ActionForward addEntry( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {

        log.debug( "aForm:" + form );

        AddACLEntryActionForm aForm = (AddACLEntryActionForm) form;

        String entryKind = aForm.getEntryKind();

        VOMSAdmin admin = null;
        ACL acl = ACLDAO.instance().getById( aForm.getAclId() );

        if ( acl == null )
            throw new NoSuchACLException( "acl not found in database." );
        
        
        if (entryKind ==  null)
            throw new IllegalArgumentException("No acl entry type selected!");
            
                    
        if ( entryKind.equals( "voUser" ) ) {

            VOMSUser user = VOMSUserDAO.instance().findById( aForm.getUserId() );

            admin = VOMSAdminDAO.instance().getByName( user.getDn(),
                    user.getCa().getDn() );

            if ( admin == null )
                admin = VOMSAdminDAO.instance().create( user.getDn(),
                        user.getCa().getDn() );

        } else if ( entryKind.equals( "nonVoUser" ) ) {

            VOMSCA ca = VOMSCADAO.instance().getByID( aForm.getCaId() );

            if ( ca == null )
                throw new NoSuchCAException( "ca not found in database." );

            admin = VOMSAdminDAO.instance().getByName( aForm.getDn(),
                    ca.getDn() );

            if ( admin == null )
                admin = VOMSAdminDAO.instance().create( aForm.getDn(),
                        ca.getDn() );

        } else if ( entryKind.equals( "group" ) ) {

            VOMSGroup group = VOMSGroupDAO.instance().findById(
                    aForm.getGroupId() );
            if ( group == null )
                throw new NoSuchGroupException( "group not found in database." );

            admin = VOMSAdminDAO.instance().getByFQAN( group.getName() );
            if ( admin == null )
                admin = VOMSAdminDAO.instance().create( group.getName() );

        } else if ( entryKind.equals( "role" ) ) {

            VOMSGroup group = VOMSGroupDAO.instance().findById(
                    aForm.getRoleGroupId() );
            
            if ( group == null )
                throw new NoSuchGroupException( "group not found in database." );

            VOMSRole role = VOMSRoleDAO.instance().findById( aForm.getRoleId() );
            if ( role == null )
                throw new NoSuchRoleException( "role not found in database." );

            VOMSContext ctxt = VOMSContext.instance( group, role );
            admin = VOMSAdminDAO.instance().getByFQAN(ctxt.toString());
            if (admin == null)
                admin = VOMSAdminDAO.instance().create( ctxt.toString() );
        
        }else if (entryKind.equals( "anyAuthenticatedUser" )){
            
            admin = VOMSAdminDAO.instance().getAnyAuthenticatedUserAdmin();
            
        }else if (entryKind.equals( "tag" )){
            
            admin = VOMSAdminDAO.instance().getById( aForm.getTagId() );
        }

        String[] perms = aForm.getSelectedPermissions();

        String permString = StringUtils.join( perms, "|" ).toString();

        log.debug( "PermString: " + permString );
        
        SaveACLEntryOperation op = SaveACLEntryOperation.instance(acl, 
                                                                  admin, 
                                                                  VOMSPermission.fromString( permString ),
                                                                  aForm.isPropagated());                
        op.execute();
        
        aForm.reset( mapping, request );

        return findSuccess( mapping );
    }

    public ActionForward load( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {

        ACLActionForm aForm = (ACLActionForm) form;
        log.debug( aForm );

        VOMSAdmin admin = VOMSAdminDAO.instance().getById( aForm.getAdminId() );
        ACL acl = ACLDAO.instance().getById( aForm.getAclId() );

        VOMSPermission perm = acl.getPermissions( admin );
        aForm.setSelectedPermissions( perm.toStringArray() );

        request.setAttribute( "acl", acl );
        request.setAttribute( "admin", admin );

        return mapping.findForward( "editACL" );

    }

    public ActionForward set( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {

        ACLActionForm aForm = (ACLActionForm) form;
        log.debug( aForm );

        ACL acl = ACLDAO.instance().getById( aForm.getAclId() );
        VOMSAdmin admin = VOMSAdminDAO.instance().getById( aForm.getAdminId() );

        String[] perms = aForm.getSelectedPermissions();

        String permString = StringUtils.join( perms, "|" ).toString();

        log.debug( "PermString: " + permString );

        SaveACLEntryOperation op = SaveACLEntryOperation.instance(acl, admin, VOMSPermission.fromString( permString ),aForm.isPropagated());
        op.execute();
        
        return findSuccess( mapping );
    }

    public ActionForward preDelete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
    throws Exception {
    	
    	ACLActionForm aForm = (ACLActionForm) form;
        log.debug( aForm );
        
        ACLDAO dao = ACLDAO.instance();
        ACL acl = dao.getById( aForm.getAclId() );
        VOMSAdmin admin = VOMSAdminDAO.instance().getById( aForm.getAdminId() );
        VOMSPermission perm = acl.getPermissions(admin);
        
        request.setAttribute( "acl", acl );
        request.setAttribute( "admin", admin );
        request.setAttribute( "permission", perm );
        
        return mapping.findForward("deleteACL");
        
    }
    public ActionForward delete( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {

        ACLActionForm aForm = (ACLActionForm) form;
        log.debug( aForm );

        ACLDAO dao = ACLDAO.instance();
        ACL acl = dao.getById( aForm.getAclId() );
        VOMSAdmin admin = VOMSAdminDAO.instance().getById( aForm.getAdminId() );

        DeleteACLEntryOperation op = DeleteACLEntryOperation.instance( acl, admin, aForm.isPropagated());
        op.execute();
                
        // Delete admin if it doesn't have any active permissions
        if (!admin.isInternalAdmin()){
        	if (!dao.hasActivePermissions(admin))
        		VOMSAdminDAO.instance().delete( admin );	
        }
        
        storeGroup(request, acl.getGroup());
        return findSuccess( mapping );
    }

}
