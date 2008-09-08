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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class VOMSPermission implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    public static final int CONTAINER_READ = 1;

    public static final int CONTAINER_WRITE = 2;

    public static final int MEMBERSHIP_READ = 4;

    public static final int MEMBERSHIP_WRITE = 8;

    public static final int ACL_READ = 16;

    public static final int ACL_WRITE = 32;

    public static final int ACL_DEFAULT = 64;

    public static final int REQUESTS_READ = 128;

    public static final int REQUESTS_WRITE = 256;

    public static final int ATTRIBUTES_READ = 512;

    public static final int ATTRIBUTES_WRITE = 1024;

    public static final int GRANT = 2048;

    private static final int NUM_PERMISSIONS = 12;

    private static final int ALL_PERMISSION_MASK = ~0 >>> ( 32 - NUM_PERMISSIONS );

    private int bits = 0;

    public VOMSPermission() {

    }

    public VOMSPermission( int bits ) {

        this.bits = bits;
    }

    protected boolean test( int permission ) {

        if ( permission <= 0 )
            throw new IllegalArgumentException(
                    "Permission must be a positive integer." );
        return ( ( bits & permission ) == permission );
    }

    protected VOMSPermission set( int permission ) {

        if ( permission <= 0 )
            throw new IllegalArgumentException(
                    "Permission must be a positive integer." );
        bits |= permission;
        return this;
    }

    protected VOMSPermission unset( int permission ) {

        if ( permission <= 0 )
            throw new IllegalArgumentException(
                    "Permission must be a positive integer." );
        bits &= ~permission;
        return this;
    }

    public int getBits() {

        return bits;
    }
    
    private List buildPermList(){
    		
    		ArrayList permList = new ArrayList();

        if ( test( CONTAINER_READ ) )
            permList.add( "CONTAINER_READ" );

        if ( test( CONTAINER_WRITE ) )
            permList.add( "CONTAINER_WRITE" );

        if ( test( MEMBERSHIP_READ ) )
            permList.add( "MEMBERSHIP_READ" );

        if ( test( MEMBERSHIP_WRITE ) )
            permList.add( "MEMBERSHIP_WRITE" );

        if ( test( ACL_READ ) )
            permList.add( "ACL_READ" );

        if ( test( ACL_WRITE ) )
            permList.add( "ACL_WRITE" );

        if ( test( ACL_DEFAULT ) )
            permList.add( "ACL_DEFAULT" );

        if ( test( REQUESTS_READ ) )
            permList.add( "REQUESTS_READ" );

        if ( test( REQUESTS_WRITE ) )
            permList.add( "REQUESTS_WRITE" );

        if ( test( ATTRIBUTES_READ ) )
            permList.add( "ATTRIBUTES_READ" );

        if ( test( ATTRIBUTES_WRITE ) )
            permList.add( "ATTRIBUTES_WRITE" );

        if ( test( GRANT ) )
            permList.add( "GRANT" );
        
        return permList;
    		
    }

    public String[] toStringArray(){
    	
    		if (bits == 0)
    			return null;
    		
    		List permList = buildPermList();
    		String[] result = new String[permList.size()];
    		
    		permList.toArray(result);
    		
    		permList.clear();
    		permList = null;
    		
    		return result;
    		
    }
    
    
    public String getCompactRepresentation(){
    	
    		StringBuffer buf  = new StringBuffer();
    		
    		if (bits == 0)
    			return "NONE";
    		
    		if (test(ALL_PERMISSION_MASK))
    			return "ALL";
    		
    		// Container perms
    		buf.append("C:");
    		buf.append(test(CONTAINER_READ)?"r":"-");
    		buf.append(test(CONTAINER_WRITE)?"w":"-");
    		
    		buf.append(" M:");
    		buf.append(test(MEMBERSHIP_READ)?"r":"-");
    		buf.append(test(MEMBERSHIP_WRITE)?"w":"-");
    		
    		buf.append(" Acl:");
    		buf.append(test(ACL_READ)?"r":"-");
    		buf.append(test(ACL_WRITE)?"w":"-");
    		if (test(ACL_DEFAULT))
    			buf.append("d");
    		
    		buf.append(" Attrs:");
    		buf.append(test(ATTRIBUTES_READ)?"r":"-");
    		buf.append(test(ATTRIBUTES_WRITE)?"w":"-");
    		
    		buf.append(" Req:");
    		buf.append(test(REQUESTS_READ)?"r":"-");
    		buf.append(test(REQUESTS_WRITE)?"w":"-");
    		
    		return buf.toString();
    		
    }
    public String toString() {

        if ( test( ALL_PERMISSION_MASK ) )
            return "ALL";

        if ( bits == 0 )
            return "NONE";

        List permList = buildPermList();
        
        String result = StringUtils.join( permList.iterator(), "," );

        permList.clear();
        permList = null;

        return result;
    }

    public boolean equals( Object other ) {

        if ( this == other )
            return true;

        if ( !( other instanceof VOMSPermission ) )
            return false;

        VOMSPermission that = (VOMSPermission) other;
        return ( this.bits == that.bits );

    }

    public int hashCode() {

        return new Integer( bits ).hashCode();

    }

    public Object clone() throws CloneNotSupportedException {

        VOMSPermission clone = (VOMSPermission) super.clone();
        clone.bits = this.bits;
        return clone;
    }

    public boolean hasContainerReadPermission() {

        return test( CONTAINER_READ );
    }

    public boolean hasContainerWritePermission() {

        return test( CONTAINER_WRITE );
    }

    public boolean hasMembershipReadPermission() {

        return test( MEMBERSHIP_READ );
    }

    public boolean hasMembershipWritePermission() {

        return test( MEMBERSHIP_WRITE );
    }

    public boolean hasACLReadPermission() {

        return test( ACL_READ );
    }

    public boolean hasACLWritePermission() {

        return test( ACL_WRITE );
    }

    public boolean hasACLDefaultPermission() {

        return test( ACL_DEFAULT );
    }

    public boolean hasRequestReadPermission() {

        return test( REQUESTS_READ );
    }

    public boolean hasRequestWritePermission() {

        return test( REQUESTS_WRITE );
    }

    public boolean hasAttributeReadPermission() {

        return test( ATTRIBUTES_READ );
    }

    public boolean hasAttributeWritePermission() {

        return test( ATTRIBUTES_WRITE );
    }

    public boolean hasGrantPermission() {

        return test( GRANT );

    }

    public VOMSPermission setContainerReadPermission() {

        set( CONTAINER_READ );
        return this;
    }

    public VOMSPermission setContainerWritePermission() {

        set( CONTAINER_WRITE );
        return this;
    }

    public VOMSPermission setMembershipReadPermission() {

        set( MEMBERSHIP_READ );
        return this;
    }

    public VOMSPermission setMembershipWritePermission() {

        set( MEMBERSHIP_WRITE );
        return this;
    }

    public VOMSPermission setMembershipRWPermission(){
    		
    		set(MEMBERSHIP_READ);
    		set(MEMBERSHIP_WRITE);
    		return this;
    		
    }
    public VOMSPermission setACLReadPermission() {

        set( ACL_READ );
        return this;
    }

    public VOMSPermission setACLWritePermission() {

        set( ACL_WRITE );
        return this;
    }

    public VOMSPermission setACLDefaultPermission() {

        set( ACL_DEFAULT );
        return this;
    }

    public VOMSPermission setRequestsReadPermission() {

        set( REQUESTS_READ );
        return this;
    }

    public VOMSPermission setRequestsWritePermission() {

        set( REQUESTS_WRITE );
        return this;
    }

    public VOMSPermission setAttributesReadPermission() {

        set( ATTRIBUTES_READ );
        return this;
    }

    public VOMSPermission setAttributesWritePermission() {

        set( ATTRIBUTES_WRITE );
        return this;
    }

    public VOMSPermission setGrantPermission() {

        set( GRANT );
        return this;

    }

    public VOMSPermission unsetContainerReadPermission() {

        unset( CONTAINER_READ );
        return this;
    }

    public VOMSPermission unsetContainerWritePermission() {

        unset( CONTAINER_WRITE );
        return this;
    }

    public VOMSPermission unsetMembershipReadPermission() {

        unset( MEMBERSHIP_READ );
        return this;
    }

    public VOMSPermission unsetMembershipWritePermission() {

        unset( MEMBERSHIP_WRITE );
        return this;
    }

    public VOMSPermission unsetACLReadPermission() {

        unset( ACL_READ );
        return this;
    }

    public VOMSPermission unsetACLWritePermission() {

        unset( ACL_WRITE );
        return this;
    }

    public VOMSPermission unsetACLDefaultPermission() {

        unset( ACL_DEFAULT );
        return this;
    }

    public VOMSPermission unsetRequestsReadPermission() {

        unset( REQUESTS_READ );
        return this;
    }

    public VOMSPermission unsetRequestsWritePermission() {

        unset( REQUESTS_WRITE );
        return this;
    }

    public VOMSPermission unsetAttributesReadPermission() {

        unset( ATTRIBUTES_READ );
        return this;
    }

    public VOMSPermission unsetAttributesWritePermission() {

        unset( ATTRIBUTES_WRITE );
        return this;
    }

    public VOMSPermission unsetGrantPermission() {

        unset( GRANT );
        return this;
    }

    public VOMSPermission setAllPermissions() {

        set( ALL_PERMISSION_MASK );
        return this;
    }

    public VOMSPermission setEmptyPermissions() {

        bits = 0;
        return this;
    }

    public VOMSPermission setPermissions( int bits ) {

        set( bits );
        return this;
    }

    public boolean satisfies( VOMSPermission other ) {

        int perms = bits & other.getBits();
        return ( perms == other.getBits() );
    }

    public static String asString( int bits ) {

        VOMSPermission p = new VOMSPermission( bits );
        return p.toString();
    }

    public static VOMSPermission getContainerRWPermissions() {

        return new VOMSPermission().setContainerReadPermission()
                .setContainerWritePermission();
    }

    public static VOMSPermission getMembershipRWPermissions() {

        return new VOMSPermission().setMembershipReadPermission()
                .setMembershipWritePermission();
    }

    public static VOMSPermission getContainerReadPermission() {

        return new VOMSPermission().setContainerReadPermission();
    }

    public static VOMSPermission getAllPermissions() {

        return new VOMSPermission().setAllPermissions();
    }

    public static VOMSPermission getEmptyPermissions() {

        return new VOMSPermission().setEmptyPermissions();
    }

    public static VOMSPermission getAttributesRWPermissions() {

        return new VOMSPermission().setAttributesReadPermission()
                .setAttributesWritePermission();
    }

    public static VOMSPermission getRequestsRWPermissions() {

        return new VOMSPermission().setRequestsReadPermission().
            setRequestsWritePermission();
    }
    
    public String toBinaryString() {

        return Integer.toBinaryString( bits );
    }

    
    public static VOMSPermission fromStringArray(String[] perms){
    	
    		VOMSPermission perm = new VOMSPermission();
    		
    		for ( int i = 0; i < perms.length; i++ ) {

            if ( perms[i].equals( "CONTAINER_READ" ) )
                perm.setContainerReadPermission();
            else if ( perms[i].equals( "CONTAINER_WRITE" ) )
                perm.setContainerWritePermission();
            else if ( perms[i].equals( "MEMBERSHIP_READ" ) )
                perm.setMembershipReadPermission();
            else if ( perms[i].equals( "MEMBERSHIP_WRITE" ) )
                perm.setMembershipWritePermission();
            else if ( perms[i].equals( "ACL_READ" ) )
                perm.setACLReadPermission();
            else if ( perms[i].equals( "ACL_WRITE" ) )
                perm.setACLWritePermission();
            else if ( perms[i].equals( "ACL_DEFAULT" ) )
                perm.setACLDefaultPermission();
            else if ( perms[i].equals( "REQUESTS_READ" ) )
                perm.setRequestsReadPermission();
            else if ( perms[i].equals( "REQUESTS_WRITE" ) )
                perm.setRequestsWritePermission();
            else if ( perms[i].equals( "ATTRIBUTES_READ" ) )
                perm.setAttributesReadPermission();
            else if ( perms[i].equals( "ATTRIBUTES_WRITE" ) )
                perm.setAttributesWritePermission();
            else if ( perms[i].equals( "GRANT" ) )
                perm.setGrantPermission();
            else if ( perms[i].equals( "ALL" ) )
                perm.setAllPermissions();
            else if ( perms[i].equals( "NONE" ) )
                perm.setEmptyPermissions();
            else
                throw new IllegalArgumentException(
                        "Unknown permission passed as argument: " + perms[i] );
        }
    		
    		return perm;
    	
    }
    
    public static VOMSPermission fromString( String permString ) {

        VOMSPermission perm = new VOMSPermission();

        String[] perms = StringUtils.split( permString, '|' );

        if ( perms.length == 1 && perms[0].equals( "" ) )
            return perm;

        return fromStringArray(perms);
        
    }
    
    public static VOMSPermission fromBits( int bits){
        
        if ( bits <= 0 )
            throw new IllegalArgumentException(
                    "Permission must be a positive integer." );
        
        VOMSPermission perm = new VOMSPermission();
        
        for (int i=0; i < NUM_PERMISSIONS; i++){
            int PERM_MASK = 1 << i;
            if ((bits & PERM_MASK) == PERM_MASK)
                perm.set( PERM_MASK );
        }
        
        return perm;
    }
}
