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
package org.glite.security.voms.admin.service;


import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.common.PathNamingScheme;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.model.ACL;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMSAttributeDescription;
import org.glite.security.voms.admin.model.VOMSBaseAttribute;
import org.glite.security.voms.admin.model.VOMSCA;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.service.acl.ACLEntry;
import org.glite.security.voms.service.attributes.AttributeClass;
import org.glite.security.voms.service.attributes.AttributeValue;
import org.glite.security.voms.service.certificates.X509Certificate;



public class ServiceUtils {
	
    public static AttributeValue[] toAttributeValueArray(Collection attributes){
        
        if (attributes == null || attributes.isEmpty())
            return null;
        
        AttributeValue[] values = new AttributeValue[attributes.size()];
        
        Iterator i = attributes.iterator();
        int index = 0;
        
        while (i.hasNext())
            values[index++]=  ((VOMSBaseAttribute)i.next()).asAttributeValue();
        
        return values;
        
    }
    public static AttributeClass[] toAttributeClassArray(List descriptions){
     
        if (descriptions == null || descriptions.isEmpty())
            return null;
        
        AttributeClass[] classes = new AttributeClass[descriptions.size()];
        
        Iterator  i = descriptions.iterator();
        
        int index = 0;
        
        while (i.hasNext())
            classes[index++] = ((VOMSAttributeDescription)i.next()).asAttributeClass();
            
        return classes;
    }
    
    
    public static String[] rolesToStringArray(Collection c){
        
        if (c == null || c.isEmpty())
            return null;
        
        String[] res = new String[c.size()];
        
        int index = 0;
        Iterator i = c.iterator();
        
        while (i.hasNext())
            res[index++] = i.next().toString();
        
        return res;
        
    }
    public static String[] groupsToStringArray(Collection c){
        
        if (c == null || c.isEmpty())
            return null;
        
        String[] res = new String[c.size()];
        
        int index = 0;
        Iterator i = c.iterator();
        
        while (i.hasNext())
            res[index++] = ((VOMSGroup)i.next()).getName();
        
        return res;
        
    }
    
    public static String[] casToStringArray(Collection c){
        
        if (c == null || c.isEmpty())
            return null;
        
        String[] res = new String[c.size()];
        
        int index = 0;
        Iterator i = c.iterator();
        
        while (i.hasNext())
            res[index++] = ((VOMSCA)i.next()).getSubjectString();
        
        return res;
        
    }
    
    public static String[] toStringArray(Collection c){
        
        if (c == null || c.isEmpty() )
            return null;
        
        String[] res = new String[c.size()];
        
        int index = 0;
        Iterator i = c.iterator();
        
        while (i.hasNext())
            res[index++] = i.next().toString();
        
        return res;     
    }
    
    public static ACLEntry[] toACLEntryArray(ACL acl){
        
        if (acl == null || acl.getPermissions().isEmpty())
            return null;
        
        ACLEntry[] entries = new ACLEntry[acl.getPermissions().size()];
        int index = 0;
        
        for (Iterator <VOMSAdmin> adminIter = acl.getPermissions().keySet().iterator(); adminIter.hasNext();){
            
            VOMSAdmin admin = adminIter.next();
            VOMSPermission perms = acl.getPermissions( admin );
            
            
            ACLEntry entry = new ACLEntry();
            entry.setAdminSubject( admin.getDn() );
            entry.setAdminIssuer( admin.getCa().getDn() );
            entry.setVomsPermissionBits( perms.getBits() );
           
            entries[index++] = entry;
            
        }
        
        return entries;
    }
    
    public static Map <VOMSAdmin, VOMSPermission> toPermissionMap( ACLEntry[] entries ) {

        if ( entries == null || entries.length == 0 )
            throw new NullArgumentException( "entries cannot be null or empty!" );

        HashMap <VOMSAdmin, VOMSPermission> map = new HashMap <VOMSAdmin, VOMSPermission>();

        for ( ACLEntry e: entries) {

            VOMSAdmin admin = VOMSAdminDAO.instance().getByName(
                    e.getAdminSubject(), e.getAdminIssuer() );
            
            VOMSPermission perm = VOMSPermission.fromBits( e
                    .getVomsPermissionBits() );

            if ( admin != null )
                map.put( admin, perm );

        }

        return map;

    }
    
    public static X509Certificate toX509Certificate(Certificate c){
        
        X509Certificate cert = new X509Certificate();
        
        cert.setId( c.getId() );
        cert.setSubject( c.getSubjectString() );
        cert.setIssuer( c.getCa().getSubjectString() );
        
        
        return cert;
        
    }
    
    public static X509Certificate[] toX509CertificateArray(Set<Certificate> certificateSet){
     
        X509Certificate[] certs = new X509Certificate[certificateSet.size()];
        
        int counter = 0;
        
        for (Certificate c: certificateSet)
            certs[counter++] = toX509Certificate( c );
        
        return certs;   
    }
    /**
     * This method allows to have interoperability at the gridmapfile 
     * level with implementations that support the emailAddress format
     * for the email in the DN as opposed to the Email format that is
     * currently used in VOMS.
     * 
     * @param l, the input DN list 
     * @return the decorated list
     */
    public static List<String> decorateDNList(List<String> l){
        
        List<String> returnValue = new ArrayList<String>();
        
        for (String dn: l)
            if (dn.contains( "Email" ))
                returnValue.add(dn.replaceAll( "Email=", "emailAddress=" ));
        
        returnValue.addAll( l );
        return returnValue;
                   
    }
    
    public static String getBaseContext(HttpServletRequest request){
        
        String result = request.getScheme()+"://"+
        request.getServerName()+":"+
        request.getServerPort()+"/voms/"+
        VOMSConfiguration.instance().getVOName();

        return result;
    }
    
    public static java.security.cert.X509Certificate certificateFromBytes(byte[] certBytes){
        
        CertificateFactory cf;
        try {
            
            cf = CertificateFactory.getInstance( "X.509" );
        
        } catch ( CertificateException e ) {
             
            throw new VOMSException("Error instantiating X.509 Certificate factory!",e);
        }
        
        java.security.cert.X509Certificate x509Cert = null;
        
        try {
            
            x509Cert = (java.security.cert.X509Certificate) cf.generateCertificate( new ByteArrayInputStream(certBytes) );
        
        } catch ( CertificateException e ) {
        
            throw new VOMSException("Error parsing X.509 certificate passed as argument!",e);
        }
        
        return x509Cert;
    }
}
