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

import java.beans.Expression;
import java.beans.Statement;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSFatalException;
import org.glite.security.voms.admin.model.History;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.hibernate.HibernateException;
import org.hibernate.Session;


public class HistoryHelper {

    public static final short HISTORY_INSERT = 0;

    public static final short HISTORY_UPDATE = 1;

    public static final short HISTORY_DELETE = 2;

    public static final Log log = LogFactory.getLog( HistoryHelper.class );

    private static String buildMethodName( String prefix, String property ) {

        StringBuffer buf = new StringBuffer();
        buf.append( prefix );

        buf.append( property.substring( 0, 1 ).toUpperCase() );
        buf.append( property.substring( 1 ) );

        return buf.toString();
    }

    public static void populateHistoryObject( Object target, Serializable id,
            String[] propertyNames, Object[] state ) {

        try {
            // Set the id property
            Statement s = new Statement( target, "setId", new Object[] { id } );
            s.execute();

            for ( int i = 0; i < propertyNames.length; i++ ) {
                // Set other properties
                s = new Statement( target, buildMethodName( "set",
                        propertyNames[i] ), new Object[] { state[i] } );
                s.execute();
            }

        } catch ( Exception e ) {
            log.fatal( e.getMessage(), e );
            throw new VOMSFatalException( e.getMessage(), e );
        }
    }

    public static void updateHistoryObjectId( Object target, Auditable entity ) {

        try {

            Expression e = new Expression( entity, "getId", null );
            Object id = e.getValue();

            Statement s = new Statement( target, "setId", new Object[] { id } );
            s.execute();

        } catch ( Exception e ) {

            log.fatal( e.getMessage(), e );
            throw new VOMSFatalException( e.getMessage(), e );
        }

    }

    public static void updateHistoryObjectAdmin( Object target, VOMSAdmin admin ) {

        History record = (History) target;
        record.setWho( admin );
    }

    private static Object createHistoryObject( short operation,
            VOMSAdmin admin, Object entity, Serializable id,
            String[] propertyNames, Object[] state ) {

        Date timestamp = Calendar.getInstance().getTime();

        String historyClassName = entity.getClass().getName() + "History";

        Class[] constructorTypes = new Class[] { short.class, Date.class,
                VOMSAdmin.class };

        Object[] params = new Object[] { new Short( operation ), timestamp,
                admin };

        Class historyClass;
        Constructor historyObjectConstructor;
        Object historyObject;

        try {

            historyClass = Class.forName( historyClassName );
            historyObjectConstructor = historyClass
                    .getConstructor( constructorTypes );
            historyObject = historyObjectConstructor.newInstance( params );

            HistoryHelper.populateHistoryObject( historyObject, id,
                    propertyNames, state );

        } catch ( Throwable t ) {

            // FIXME: maybe I could go into finer detail here....
            log.fatal( t.getMessage(), t );
            throw new VOMSFatalException( t.getMessage(), t );
        }

        return historyObject;

    }

    public static Object createUpdateHistoryObject( Object entity,
            VOMSAdmin admin, Serializable id, String[] propertyNames,
            Object[] state ) {

        return createHistoryObject( HISTORY_UPDATE, admin, entity, id,
                propertyNames, state );
    }

    public static Object createInsertHistoryObject( VOMSAdmin admin,
            Object entity, Serializable id, String[] propertyNames,
            Object[] state ) {

        return createHistoryObject( HISTORY_INSERT, admin, entity, id,
                propertyNames, state );

    }

    public static Object createDeleteHistoryObject( VOMSAdmin admin,
            Object entity, Serializable id, String[] propertyNames,
            Object[] state ) {

        return createHistoryObject( HISTORY_DELETE, admin, entity, id,
                propertyNames, state );

    }

    public static void saveHistoryObject( Object record, Connection conn ) {

        try {
            Session session = HibernateFactory.getFactory().openSession( conn );

            session.beginTransaction();
            session.save( record );
            session.getTransaction().commit();

        } catch ( HibernateException e ) {
            log.fatal( e.getMessage(), e );
            throw new VOMSFatalException( e.getMessage(), e );
        }

    }
}
