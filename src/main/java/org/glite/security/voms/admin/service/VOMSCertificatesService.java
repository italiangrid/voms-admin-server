package org.glite.security.voms.admin.service;

import java.rmi.RemoteException;

import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.common.UnimplementedFeatureException;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.users.AddUserCertificateOperation;
import org.glite.security.voms.admin.operations.users.FindUserOperation;
import org.glite.security.voms.admin.operations.users.RemoveUserCertificateOperation;
import org.glite.security.voms.service.certificates.VOMSCertificates;
import org.glite.security.voms.service.certificates.X509Certificate;


public class VOMSCertificatesService implements VOMSCertificates {

    
    public void addCertificate( long userId, X509Certificate cert )
            throws RemoteException{

        if ( cert == null )
            throw new NullArgumentException( "X509Certificate cannot be null!" );
        
        if (userId < 0)
            throw new IllegalArgumentException("the userId must be a positive integer!");
        
        VOMSUser user = (VOMSUser) FindUserOperation.instance( userId ).execute();
        
        if (cert.getBytes() != null){
            
            java.security.cert.X509Certificate x509Cert = ServiceUtils.certificateFromBytes( cert.getBytes() ); 
            AddUserCertificateOperation.instance( user,  x509Cert ).execute();
        
        }else
            AddUserCertificateOperation.instance( user, cert.getSubject(), cert.getIssuer(), cert.getNotAfter() ).execute();
            
        

    }

    public X509Certificate[] getCertificates( long userId ) throws RemoteException {

        VOMSUser u = (VOMSUser) FindUserOperation.instance( userId).execute();
        return ServiceUtils.toX509CertificateArray( u.getCertificates() );
    }
    
    
    public X509Certificate[] getCertificates( String subject, String issuer ) throws RemoteException {

        
        VOMSUser u = (VOMSUser) FindUserOperation.instance( subject, issuer).execute();
        return ServiceUtils.toX509CertificateArray( u.getCertificates() );
    }

    public long getUserIdFromDn( String subject, String issuer )
            throws RemoteException {

        if ( subject == null )
            throw new NullArgumentException( "User's subject cannot be null!" );
        
        if ( issuer == null )
            throw new NullArgumentException( "User's issuer cannot be null!" );
        
        
        VOMSUser u = (VOMSUser) FindUserOperation.instance( subject,
                issuer ).execute();
        
        if (u == null)
            return -1;
        
        return u.getId().longValue();
    }

    
    public void addCertificate( String registeredCertSubject, String registeredCertIssuer, X509Certificate cert) throws RemoteException {

        long userId = getUserIdFromDn( registeredCertSubject, registeredCertIssuer );
        addCertificate( userId, cert );
    }

    public void removeCertificate( X509Certificate cert ) throws RemoteException {

        if (cert.getBytes() != null){
            
            java.security.cert.X509Certificate x509Cert = ServiceUtils.certificateFromBytes( cert.getBytes() );
            RemoveUserCertificateOperation.instance( x509Cert ).execute();
        
        }else
            RemoveUserCertificateOperation.instance( cert.getSubject(), cert.getIssuer() ).execute();           
    }

    public void suspendCertificate( X509Certificate cert, String reason ) throws RemoteException {

        throw new UnimplementedFeatureException( "suspendCertificate(...)" );
        
    }

    

}
