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
package org.glite.security.voms.admin.common.tasks;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.Constants;
import org.glite.security.voms.admin.common.DNUtil;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.dao.VOMSCADAO;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.dao.VOMSVersionDAO;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.model.ACL;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMSCA;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSSeqNumber;
import org.glite.security.voms.admin.operations.VOMSPermission;



/**
 * 
 * @author andrea
 * 
 */
public class DatabaseSetupTask extends TimerTask {

    private static final Log log = LogFactory.getLog( DatabaseSetupTask.class );

    private Timer timer;

    private static DatabaseSetupTask instance = null;

    public static DatabaseSetupTask instance(){
        return instance(null);
    }
    
    public static DatabaseSetupTask instance( Timer t ) {

        if ( instance == null )
            instance = new DatabaseSetupTask( t );
        return instance;

    }

    private DatabaseSetupTask( Timer timer ) {

        this.timer = timer;
    }

    public void run() {

        List admins = VOMSAdminDAO.instance().getAll();
        List cas = VOMSCADAO.instance().getAll();

        if ( admins.isEmpty() || cas.isEmpty() ) {

            // The org.glite.security.voms.admin.database needs to be populated at first

            // Add internal CAs
            VOMSCADAO caDAO = VOMSCADAO.instance();

            caDAO.createCA( Constants.VIRTUAL_CA,
                    "A dummy CA for local org.glite.security.voms.admin.database mainteneance" );
            caDAO
                    .createCA( Constants.GROUP_CA,
                            "A virtual CA for VOMS groups." );
            
            caDAO.createCA( Constants.ROLE_CA, "A virtual CA for VOMS roles." );
            
            caDAO.createCA( Constants.AUTHZMANAGER_ATTRIBUTE_CA,
                    "A virtual CA for authz manager attributes" );
            
            caDAO.createCA( Constants.TAG_CA, "A virtual CA for VOMS Admin tags" );

            // Create vo root group
            VOMSGroup voGroup = VOMSGroupDAO.instance().createVOGroup();

            // Set correct db version
            VOMSVersionDAO.instance().setupVersion();

            // Add internal admins
            VOMSAdminDAO adminDAO = VOMSAdminDAO.instance();

            VOMSAdmin internalAdmin = adminDAO.create(
                    Constants.INTERNAL_ADMIN, Constants.VIRTUAL_CA );

            VOMSAdmin localAdmin = adminDAO.create( Constants.LOCAL_ADMIN,
                    Constants.VIRTUAL_CA );

            adminDAO.create( Constants.PUBLIC_ADMIN, Constants.VIRTUAL_CA );

            adminDAO
                    .create( Constants.ANYUSER_ADMIN, Constants.VIRTUAL_CA );

            VOMSPermission allPermissions = VOMSPermission.getAllPermissions();

            ACL voGroupACL = new ACL( voGroup, false );
            voGroup.getAcls().add( voGroupACL );

            voGroupACL.setPermissions( localAdmin, allPermissions );
            voGroupACL.setPermissions( internalAdmin, allPermissions );
            
            // Create VO-Admin role and admin
            
            VOMSRole voAdminRole = VOMSRoleDAO.instance().create( "VO-Admin" );            
            
            VOMSAdmin voAdmin = VOMSAdminDAO.instance().create( voGroup.getName()+"/Role=VO-Admin" );
            
            voGroupACL.setPermissions( voAdmin, allPermissions);
            
            VOMSSeqNumber seqNum = new VOMSSeqNumber();
            seqNum.setSeq( "0" );
            HibernateFactory.getSession().save( seqNum );

            // Trusted admin creation

            String trustedAdminDn = VOMSConfiguration.instance().getString(
                    "voms.trusted.admin.subject" );

            if ( trustedAdminDn == null ) {

                voAdminRole.importACL( voGroup );
                HibernateFactory.commitTransaction();
                return;
            }

            String trustedAdminCa = VOMSConfiguration.instance().getString(
                    "voms.trusted.admin.ca" );

            if ( trustedAdminCa == null ) {
                log
                        .error( "Missing \"voms.trusted.admin.ca\" configuration parameter. Skipping creation of the trusted admin..." );
                return;
            }

            VOMSCA ca = VOMSCADAO.instance().getByName( trustedAdminCa );

            if ( ca == null ) {

                log
                        .error( "Trusted admin ca \""
                                + trustedAdminCa
                                + "\" not found in org.glite.security.voms.admin.database. Skipping creation of the trusted admin..." );
                return;
            }

            
            VOMSAdmin trustedAdmin = VOMSAdminDAO.instance().getByName(
                    trustedAdminDn, ca.getSubjectString() );
            
            if ( trustedAdmin == null ) {

                String emailAddress = DNUtil.getEmailAddressFromDN( trustedAdminDn );
                trustedAdmin = VOMSAdminDAO.instance().create(
                        trustedAdminDn, ca.getSubjectString(), emailAddress );

            }

            voGroupACL.setPermissions( trustedAdmin, allPermissions );
            
            log.info( "Trusted admin created." );
            
            
            // Import ACL *after* trusted admin has been created!
            voAdminRole.importACL( voGroup );
           
            HibernateFactory.commitTransaction();

            
        }

    }

}
