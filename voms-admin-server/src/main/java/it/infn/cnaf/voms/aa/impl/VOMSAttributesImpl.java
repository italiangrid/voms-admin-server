/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

package it.infn.cnaf.voms.aa.impl;

import it.infn.cnaf.voms.aa.VOMSAttributes;
import it.infn.cnaf.voms.aa.VOMSFQAN;
import it.infn.cnaf.voms.aa.VOMSGenericAttribute;
import it.infn.cnaf.voms.aa.VOMSUser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.error.SuspendedUserException;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSGroupAttribute;
import org.glite.security.voms.admin.persistence.model.VOMSMapping;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSRoleAttribute;
import org.glite.security.voms.admin.persistence.model.VOMSUserAttribute;
import org.glite.security.voms.admin.util.PathNamingScheme;

/**
 * 
 * @author Andrea Ceccanti
 *
 */
class VOMSAttributesImpl implements VOMSAttributes {

    VOMSUser user;

    List <VOMSFQAN> fqans;

    List <VOMSGenericAttribute> genericAttributes;

    private VOMSAttributesImpl() {

        fqans = new ArrayList <VOMSFQAN>();
        genericAttributes = new ArrayList <VOMSGenericAttribute>();

    }

    public List <VOMSFQAN> getFqans() {

        return fqans;
    }

    public List <VOMSGenericAttribute> getGenericAttributes() {

        return genericAttributes;
    }

    public VOMSUser getUser() {

        return user;
    }

    public void setUser( VOMSUser user ) {

        this.user = user;
    }

    void getGroupsFromUser( org.glite.security.voms.admin.persistence.model.VOMSUser user ) {

        assert user != null : "Cannot get groups from a NULL user!";

        Iterator <VOMSMapping> mIter = user.getMappings().iterator();

        while ( mIter.hasNext() ) {

            VOMSMapping m = mIter.next();
            if ( m.isGroupMapping() )
                fqans.add( VOMSFQAN.fromModel( m ) );
        }

    }

    void getRolesFromUser( org.glite.security.voms.admin.persistence.model.VOMSUser user ) {

        assert user != null : "Cannot get roles from a NULL user!";

        Iterator <VOMSMapping> mIter = user.getMappings().iterator();

        while ( mIter.hasNext() ) {

            VOMSMapping m = mIter.next();
            if ( m.isRoleMapping() )
                fqans.add( VOMSFQAN.fromModel( m ) );
        }

    }

    private void addMissingFQANsForUser(
            org.glite.security.voms.admin.persistence.model.VOMSUser user ) {

        assert user != null : "Cannot add missing FQANs for a NULL user!";

        // Check that all the user groups are actually in the fqans list

        Iterator<VOMSMapping> mappingIter = user.getMappings().iterator();

        while ( mappingIter.hasNext() ) {

            VOMSMapping mapping = mappingIter.next();

            if ( mapping.isGroupMapping() ) {

                VOMSFQAN possiblyMissingFQAN = VOMSFQAN.fromModel( mapping );

                if ( !fqans.contains( possiblyMissingFQAN ) )
                    fqans.add( possiblyMissingFQAN );

            }
        }
    }

    void getFQANsFromUser( org.glite.security.voms.admin.persistence.model.VOMSUser user,
            List <String> requestedFQANs ) {

        for ( String fqan : requestedFQANs ) {

            if ( PathNamingScheme.isQualifiedRole( fqan ) ) {

                if ( user.hasRole( fqan ) )
                    fqans.add( VOMSFQAN.fromString( fqan ) );

            } else if ( PathNamingScheme.isGroup( fqan ) ) {

                if ( user.isMember( fqan ) )
                    fqans.add( VOMSFQAN.fromString( fqan ) );

            }
        }
    }

    void getGenericAttributesFromUser(
            org.glite.security.voms.admin.persistence.model.VOMSUser user ) {

        assert user != null : "Cannot get Generic Attributes from a NULL user!";

        Iterator <VOMSUserAttribute> userAttrs = user.getAttributes()
                .iterator();

        while ( userAttrs.hasNext() ) {

            VOMSUserAttribute attribute = userAttrs.next();
            genericAttributes.add( VOMSGenericAttribute.fromModel( attribute ) );
        }

        // Get group and role attributes starting from requested FQANs
        for ( VOMSFQAN requestedFQAN : fqans ) {

            if ( requestedFQAN.isGroup() ) {

                VOMSGroup g = VOMSGroupDAO.instance().findByName(
                        requestedFQAN.getFQAN() );

                Iterator <VOMSGroupAttribute> groupAttrsIter = g
                        .getAttributes().iterator();

                while ( groupAttrsIter.hasNext() )
                    genericAttributes.add( VOMSGenericAttribute
                            .fromModel( groupAttrsIter.next() ) );

            } else if ( requestedFQAN.isRole() ) {

                String roleName = PathNamingScheme.getRoleName( requestedFQAN
                        .getFQAN() );
                String groupName = PathNamingScheme.getGroupName( requestedFQAN
                        .getFQAN() );

                VOMSRole r = VOMSRoleDAO.instance().findByName( roleName );
                VOMSGroup g = VOMSGroupDAO.instance().findByName( groupName );

                Iterator <VOMSRoleAttribute> roleAttrsIter = r
                        .getAttributesInGroup( g ).iterator();

                while ( roleAttrsIter.hasNext() )
                    genericAttributes.add( VOMSGenericAttribute
                            .fromModel( roleAttrsIter.next() ) );

            }
        }
    }

    
    public static VOMSAttributesImpl fromUser(
            org.glite.security.voms.admin.persistence.model.VOMSUser user){
        
        return fromUser( user, null );
        
    }
    
    public static VOMSAttributesImpl fromUser(
            org.glite.security.voms.admin.persistence.model.VOMSUser user,
            List <String> requestedFQANs ) {

        assert user != null : "Cannot get VOMS attributes for a NULL user!";
        
        if (user.isSuspended())
        	throw new SuspendedUserException("User '"+user.getShortName()+"' is currently suspended for the following reason: "+user.getSuspensionReason());
        
        VOMSAttributesImpl attrs = new VOMSAttributesImpl();

        attrs.setUser( VOMSUser.fromModel( user ) );

        if ( requestedFQANs == null ) {

            attrs.getGroupsFromUser( user );
            attrs.getGenericAttributesFromUser( user );

        } else {

            attrs.getFQANsFromUser( user, requestedFQANs );
            attrs.getGenericAttributesFromUser( user );

            boolean compulsoryGroupMembership = VOMSConfiguration
                    .instance()
                    .getBoolean(
                            VOMSConfigurationConstants.VOMS_AA_COMPULSORY_GROUP_MEMBERSHIP,
                            new Boolean( true ) );

            if ( compulsoryGroupMembership )
                attrs.addMissingFQANsForUser( user );

        }
        
        return attrs;

    }
    
    public static VOMSAttributes getAllFromUser(org.glite.security.voms.admin.persistence.model.VOMSUser user){
    	
    	assert user != null: "Cannot get all VOMS attributes for a NULL user!";
    	
    	if (user.isSuspended())
        	throw new SuspendedUserException("User '"+user.getShortName()+"' is currently suspended for the following reason: "+user.getSuspensionReason());
    	
    	VOMSAttributesImpl attrs = new VOMSAttributesImpl();
    	
    	attrs.setUser(VOMSUser.fromModel(user));
    	
    	attrs.getGroupsFromUser(user);
    	attrs.getRolesFromUser(user);
    	attrs.getGenericAttributesFromUser(user);
    	
    	return attrs;
    		
    }

	public boolean hasRoles() {
		
		for (VOMSFQAN f: getFqans()){
			
			if (f.isRole())
				return true;
			
		}
		
		return false;
	}

	@Override
	public String toString() {
		
		if (fqans ==  null || fqans.isEmpty())
			return "[]";
		
		return fqans.toString();
	}
}
