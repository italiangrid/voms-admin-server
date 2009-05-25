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
 *     Karoly Lorentey - karoly.lorentey.@cern.ch
 *******************************************************************************/
package org.glite.security.voms.admin.common;

/**
 * Constants used in both the clients and the server. None of these constants
 * should ever be changed.
 * 
 * @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti</a>
 * @author <a href="mailto:lorentey@elte.hu">Karoly Lorentey</a>
 * 
 */
final public class VOMSServiceConstants {

    /** The prefix distinguishing real CAs and users from virtual ones. */
    static public final String INTERNAL_DN_PREFIX = "/O=VOMS/O=System";

    /** The DN of the internal virtual CA. */
    static public final String VIRTUAL_CA = "/O=VOMS/O=System/CN=Dummy Certificate Authority";

    /**
     * The DN of the virtual administrator for bootstrap rows and local org.glite.security.voms.admin.database
     * maintanence.
     */
    static public final String LOCAL_ADMIN = "/O=VOMS/O=System/CN=Local Database Administrator";

    /**
     * The DN used for denoting records changed by an internal VOMS mechanism.
     */
    static public final String INTERNAL_ADMIN = "/O=VOMS/O=System/CN=Internal VOMS Process";

    /** The DN of the virtual administrator for unauthenticated clients. */
    static public final String UNAUTHENTICATED_CLIENT = "/O=VOMS/O=System/CN=Unauthenticated Client";

    /**
     * The DN of the virtual ACL entry subject matching any authenticated user.
     * The user must have a certificate issued by a known CA.
     */
    static public final String ANYUSER_ADMIN = "/O=VOMS/O=System/CN=Any Authenticated User";

    /**
     * The DN of the virtual ACL entry subject matching absolutely anyone,
     * including unauthenticated users.
     */
    static public final String PUBLIC_ADMIN = "/O=VOMS/O=System/CN=Absolutely Anyone";

    /**
     * The special CA value in the ADMINS table corresponding to a collective
     * administrator that is an authz manager attribute.
     */
    static public final String AUTHZMANAGER_ATTRIBUTE_CA = "/O=VOMS/O=System/CN=Authorization Manager Attributes";

    /**
     * The special CA value in the ADMINS table corresponding to a collective
     * administrator that is a group.
     */
    static public final String GROUP_CA = "/O=VOMS/O=System/CN=VOMS Group";

    /**
     * The special CA value in the ADMINS table corresponding to a collective
     * administrator that is a role.
     */
    static public final String ROLE_CA = "/O=VOMS/O=System/CN=VOMS Role";

    /**
     * The special CA value in the ADMINS table corresponding to a collective
     * administrator that is a capability.
     */
    static public final String CAPABILITY_CA = "/O=VOMS/O=System/CN=VOMS Capability";
    
    /**
     * This special CA value in the ADMINS table corresponds to a collective administrator
     * that is a VOMS tag.
     * 
     */
    static public final String TAG_CA = "/O=VOMS/O=System/CN=VOMS Admin Tag";

    /** Limit on the length of DNs in the org.glite.security.voms.admin.database. */
    static public final int SIZE_LIMIT = 255;

    /**
     * Limit on the length of class names of requests and org.glite.security.voms.admin.actions in the
     * org.glite.security.voms.admin.database.
     */
    static public final int REQUEST_CLASSNAME_LIMIT = 128;

    /** Limit on the length of short state names of requests in the org.glite.security.voms.admin.database. */
    static public final int REQUEST_STATENAME_LIMIT = 32;

    /** Security context property name for remote address. */
    static public final String SECURITY_CONTEXT_REMOTE_ADDRESS = "org.glite.security.voms.remote_address";
    
    public static final String SUCCESS = "success";

    public static final String FAILURE = "failure";

    public static final String CONFIRM = "confirm";

    public static final Integer VOMS_DB_VERSION = new Integer( 3 );

    public static final String VO_NAME_KEY = "voName";

    public static final String CURRENT_ADMIN_KEY = "currentAdmin";

    public static final String USER_KEY = "vomsUser";

    public static final String GROUP_KEY = "vomsGroup";

    public static final String ROLE_KEY = "vomsRole";

    public static final String RESULTS_KEY = "searchResults";

    public static final String UNASSIGNED_ROLES_KEY = "unassignedRoles";
    
    public static final String STATUS_MAP_KEY = "uiStatusMap";
    
    
    // TAG administrators constants below
    
    public static final String TAG_VO_MANAGER = "VO-Manager";
    
    public static final String TAG_GROUP_MANAGER = "Group-Manager";
    
    public static final String TAG_ROLE_MANAGER = "Role-Manager";
    
    public static final String TAG_ATTRIBUTES_MANAGER = "Attributes-Manager";
    
    public static final String TAG_INSTITUTE_REPRESENTATIVE  = "Institute-Representative";
    
    public static final String TAG_VO_USER = "VO-User";
    
    public static final String USER_INFO_COMPATIBILITY_NULL_VALUE = "null";
    
}

// Please do not change this line.
// arch-tag: 47047210-5c25-4af2-b4e8-77614f1b2e66
