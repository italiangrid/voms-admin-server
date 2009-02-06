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
package org.glite.security.voms.admin.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.Constants;
import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.model.VOMSCA;
import org.hibernate.Session;



public class VOMSCADAO {

    public static final Log log = LogFactory.getLog( VOMSCADAO.class );

    private VOMSCADAO() {

        HibernateFactory.beginTransaction();
    }

    public VOMSCA createCA( String caDN, String description ) {

        if ( caDN == null )
            throw new NullArgumentException( "caDN must be non-null!" );
        
        log.info( "Adding  '" + caDN + "' to trusted CA database." );

        VOMSCA ca = new VOMSCA( caDN, description );

        HibernateFactory.getSession().save( ca );

        return ca;
    }

    public void saveTrustedCA( String caDN ) {

        Session sess = HibernateFactory.getSession();
        VOMSCA ca = getByName( caDN );
        
        if (ca == null){
            
            log.debug( "Adding [ " + caDN + "] to trusted CA database." );
            createCA( caDN, null);

        } else
            log.debug( caDN + " is already in trusted CA database." ); 

    }

    public VOMSCA getByName( String caDN ) {

        if ( caDN == null )
            throw new NullArgumentException( "caDN must be non-null!" );

        String queryString = "from org.glite.security.voms.admin.model.VOMSCA as ca where ca.dn = :caDN";

        VOMSCA res = (VOMSCA) HibernateFactory.getSession().createQuery(
                queryString ).setString( "caDN", caDN ).uniqueResult();

        return res;

    }

    public VOMSCA getByID (Short caID){
        
        return (VOMSCA)HibernateFactory.getSession().get(VOMSCA.class,caID);
        
    }
    
    public VOMSCA getByID( short caID ) {

        return getByID(new Short(caID));

    }

    public List getAll() {

        String query = "from org.glite.security.voms.admin.model.VOMSCA";

        List res = HibernateFactory.getSession().createQuery( query ).list();

        return res;
    }

    public List getValid() {

        String query = "from org.glite.security.voms.admin.model.VOMSCA where dn not like '/O=VOMS%'";

        List res = HibernateFactory.getSession().createQuery( query ).list();

        return res;
    }
    
    

    public VOMSCA getGroupCA(){
        
        return getByName( Constants.GROUP_CA );
    }
    
    public VOMSCA getRoleCA(){
        
        return getByName( Constants.ROLE_CA);
    }
    public static VOMSCADAO instance() {

        return new VOMSCADAO();
    }
}
