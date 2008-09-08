package org.glite.security.voms.admin.operations.users;

import java.security.cert.X509Certificate;

import org.glite.security.voms.admin.dao.CertificateDAO;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;


public class RemoveUserCertificateOperation extends BaseVomsOperation {


    X509Certificate theCert;
    
    String subject;
    String issuer;
    
    
    private RemoveUserCertificateOperation(X509Certificate c) {

        theCert = c;
        
    }
    
    private RemoveUserCertificateOperation(String subject, String issuer) {
        
        this.subject = subject;
        this.issuer = issuer;
    }
    
    public static RemoveUserCertificateOperation instance(X509Certificate cert) {

        return new RemoveUserCertificateOperation(cert);
        
    }
    
    public static RemoveUserCertificateOperation instance(String subject, String issuer) {

        return new RemoveUserCertificateOperation(subject, issuer);
    }
    
    @Override
    protected Object doExecute() {

        if (theCert!= null){
            
            Certificate cert = CertificateDAO.instance().find( theCert );
            VOMSUserDAO.instance().deleteCertificate( cert);
            
        }else{
            
            Certificate cert = CertificateDAO.instance().findByDNCA( subject, issuer );
            VOMSUserDAO.instance().deleteCertificate( cert);   
        }
        
        return null;
    }

    @Override
    protected void setupPermissions() {

        addRequiredPermission(VOMSContext.getVoContext(),VOMSPermission.getContainerRWPermissions().setMembershipRWPermission());

    }

}
