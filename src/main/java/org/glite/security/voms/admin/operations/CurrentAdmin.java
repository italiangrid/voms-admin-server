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

package org.glite.security.voms.admin.operations;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.SecurityContext;
import org.glite.security.voms.admin.common.DNUtil;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.model.ACL;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMSCA;
import org.glite.security.voms.admin.model.VOMSUser;


public class CurrentAdmin {

    private static final Log log = LogFactory.getLog( CurrentAdmin.class );

    private VOMSAdmin admin;

    public VOMSAdmin getAdmin() {

        return admin;
    }

    protected CurrentAdmin( VOMSAdmin a ) {

        this.admin = a;
    }

    public static CurrentAdmin instance() {

        SecurityContext theContext = SecurityContext.getCurrentContext();

        String adminDN = theContext.getClientName();
        String caDN = theContext.getIssuerName();

        VOMSAdmin admin = VOMSAdminDAO.instance().getByName( adminDN, caDN );

        if ( admin == null )
            admin = VOMSAdminDAO.instance().getAnyAuthenticatedUserAdmin();

        return new CurrentAdmin( admin );
    }

    public VOMSCA getCa() {

        return admin.getCa();
    }

    public String getDn() {

        return admin.getDn();
    }

    public boolean isAuthorizedAdmin(){
        
        return !getAdmin().equals( VOMSAdminDAO.instance().getAnyAuthenticatedUserAdmin()); 
    }
    
    public boolean isVoUser() {

        return ( getVoUser() != null );

    }

    public VOMSUser getVoUser() {

        if (!isAuthorizedAdmin()){
            
            return VOMSUserDAO.instance().getByDNandCA( getRealSubject(), getRealIssuer() );
        }
        
        return VOMSUserDAO.instance().getByDNandCA( admin.getDn(), admin.getCa() );
    }

    public void createVoUser(){
        
        VOMSUser usr = getVoUser();
                
        if (usr == null){
            
            VOMSUserDAO.instance().create(getRealSubject(),getRealIssuer(),getRealCN(),null,getRealEmailAddress());
        }
    }
    
    public boolean hasPermissions( VOMSContext c, VOMSPermission p ) {

        ACL acl = c.getACL();

        log.debug( "Checking if admin " + getAdmin() + " has permission " + p
                + " in context " + c );

        log.debug("ACL for this context: ");
        log.debug(acl);
        
        VOMSUser adminUser = getVoUser();

        log.debug("Admin user: "+adminUser);
        
        VOMSPermission personalPermissions = acl.getPermissions( admin );

        log.debug( "Permissions for admin: " + personalPermissions );
        
        VOMSPermission anyAuthenticatedUserPermissions = acl.getAnyAuthenticatedUserPermissions();
        
        log.debug( "Permissions for any authenticated user: "+ anyAuthenticatedUserPermissions );
 
        VOMSPermissionList adminPerms = VOMSPermissionList.instance();
        
        if ( personalPermissions == null && adminUser == null && anyAuthenticatedUserPermissions == null)
            return false;

        if ( personalPermissions != null )
            adminPerms.addPermission( personalPermissions );
        
        if (anyAuthenticatedUserPermissions != null)
            adminPerms.addPermission( anyAuthenticatedUserPermissions );
        
        if ( adminUser == null ) 
            return adminPerms.satifies( p );

        // AdminUser != null       
        Map groupPermissions = acl.getGroupPermissions();
        Map rolePermissions = acl.getRolePermissions();

        log.debug("Group permissions empty? "+groupPermissions.isEmpty());
        log.debug("Role permissions empty? "+rolePermissions.isEmpty());
        
        if ( !groupPermissions.isEmpty() ) {

            Iterator entries = groupPermissions.entrySet().iterator();

            while ( entries.hasNext() ) {

                Map.Entry entry = (Map.Entry) entries.next();
                String groupName = ( (VOMSAdmin) entry.getKey() ).getDn();

                if ( adminUser.isMember( groupName ) ) {
                    adminPerms
                            .addPermission( (VOMSPermission) entry.getValue() );
                    log
                            .debug( "Adding group permission "
                                    + entry.getValue()
                                    + " to admin's permission set. admin is a member of the group '"
                                    + groupName + "'." );
                }

            }
        }

        if ( !rolePermissions.isEmpty() ) {

            Iterator entries = rolePermissions.entrySet().iterator();

            while ( entries.hasNext() ) {
                Map.Entry entry = (Map.Entry) entries.next();
                String roleName = ( (VOMSAdmin) entry.getKey() ).getDn();

                log.debug("Checking if current admin has role: "+roleName);
                if ( adminUser.hasRole( roleName ) ){
                 
                    adminPerms
                            .addPermission( (VOMSPermission) entry.getValue() );
                    log
                    .debug( "Adding role permission "
                            + entry.getValue()
                            + " to admin's permission set. admin has role '"
                            + roleName + "'." );
                }
            }
        }

        log.debug("Admin permissions: "+adminPerms);
        
        return adminPerms.satifies( p );
    }

    public String getRealSubject(){
        
        SecurityContext theContext = SecurityContext.getCurrentContext();

        return theContext.getClientName();
        
        
    }
    
    public String getRealIssuer(){
        
        SecurityContext theContext = SecurityContext.getCurrentContext();

        return theContext.getIssuerName();
        
    }
    
    public String getRealCN(){
    
        SecurityContext theContext = SecurityContext.getCurrentContext();
        String name = DNUtil.getBCasX500( theContext.getClientCert().getSubjectX500Principal());
        
        Matcher m = Pattern.compile("/CN=([^/]*)").matcher(name);
        if(m.find())
            return m.group(1); // get the CN field
        else 
            return null; 
    }
    
    
    public String getRealEmailAddress(){
        SecurityContext theContext = SecurityContext.getCurrentContext();
        String name = DNUtil.getBCasX500( theContext.getClientCert().getSubjectX500Principal());
        
        String candidateEmail = DNUtil.getEmailAddressFromDN( DNUtil.normalizeEmailAddressInDN( name ) );
        
        if (candidateEmail == null)
            candidateEmail  = DNUtil.getEmailAddressFromExtensions( theContext.getClientCert() );
        
        return candidateEmail;
        
    }
    
    
    public String toString() {

        return admin.toString();
    }

}
