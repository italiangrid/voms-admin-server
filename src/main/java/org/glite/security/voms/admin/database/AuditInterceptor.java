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

package org.glite.security.voms.admin.database;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.common.VOMSFatalException;
import org.glite.security.voms.admin.model.History;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.type.Type;


public class AuditInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 1L;

    static final Log log = LogFactory.getLog( AuditInterceptor.class );

    private Session session;

    private VOMSAdmin admin = null;

    private Map inserts = new HashMap();

    private Set updates = new HashSet();

    private Set deletions = new HashSet();

    private boolean auditUpdates;

    private boolean auditInserts;

    private boolean auditDeletions;

    public AuditInterceptor() {

        auditUpdates = VOMSConfiguration.instance().getBoolean(
                "voms.auditing.updates", true );
        auditInserts = VOMSConfiguration.instance().getBoolean(
                "voms.auditing.inserts", true );
        auditDeletions = VOMSConfiguration.instance().getBoolean(
                "voms.auditing.deletions", true );
    }

    public boolean onFlushDirty( Object entity, Serializable id,
            Object[] currentState, Object[] previousState,
            String[] propertyNames, Type[] types ) throws CallbackException {

        if ( entity instanceof Auditable ) {
            log.debug( "Creating history log record for update operation on: "
                    + entity );

            Object record = HistoryHelper.createUpdateHistoryObject( entity,
                    admin, id, propertyNames, previousState );
            updates.add( record );

        }

        return false;
    }

    public boolean onSave( Object entity, Serializable id, Object[] state,
            String[] propertyNames, Type[] types ) throws CallbackException {

        if ( entity instanceof Auditable ) {

            log.debug( "Creating history log record for insert operation on: "
                    + entity );
            Object record = HistoryHelper.createInsertHistoryObject( admin,
                    entity, id, propertyNames, state );
            inserts.put( entity, record );

        }

        return false;

    }

    // Here we manage deletion of persistend objects
    public void onDelete( Object entity, Serializable id, Object[] state,
            String[] propertyNames, Type[] types ) throws CallbackException {

        if ( entity instanceof Auditable ) {

            log.debug( "Creating history log record for delete operation on: "
                    + entity );
            Object record = HistoryHelper.createDeleteHistoryObject( admin,
                    entity, id, propertyNames, state );
            deletions.add( record );

        }
        return;
    }

    public void postFlush( Iterator entities ) throws CallbackException {

        try {

            // Process insert firsts
            while ( entities.hasNext() ) {
                Object entity = entities.next();

                if ( ( entity instanceof Auditable )
                        && inserts.containsKey( entity ) ) {

                    History record = (History) inserts.get( entity );

                    if ( record.getWho() == null
                            && ( entity instanceof VOMSAdmin ) ) {

                        record.setWho( (VOMSAdmin) entity );
                        setAdmin( (VOMSAdmin) entity );

                    }

                    HistoryHelper.updateHistoryObjectId( record,
                            (Auditable) entity );

                    HistoryHelper.saveHistoryObject( record, session
                            .connection() );

                    inserts.remove( entity );

                }
            }

            for ( Iterator it = updates.iterator(); it.hasNext(); ) {

                Object record = it.next();
                log.debug( "Recording update record: " + record );
                HistoryHelper.saveHistoryObject( record, session.connection() );
            }

            for ( Iterator it = deletions.iterator(); it.hasNext(); ) {
                Object record = it.next();
                log.debug( "Recording delete record:" + record );
                HistoryHelper.saveHistoryObject( record, session.connection() );

            }

        } catch ( VOMSFatalException e ) {

            throw ( e );

        } finally {
            inserts.clear();
            updates.clear();
            deletions.clear();
        }
    }

    /**
     * @return Returns the session.
     */
    public Session getSession() {

        return session;
    }

    /**
     * @param session
     *            The session to set.
     */
    public void setSession( Session session ) {

        this.session = session;
    }

    /**
     * @return Returns the admin.
     */
    public VOMSAdmin getAdmin() {

        return admin;
    }

    /**
     * @param admin
     *            The admin to set.
     */
    public void setAdmin( VOMSAdmin admin ) {

        this.admin = admin;
    }

}
