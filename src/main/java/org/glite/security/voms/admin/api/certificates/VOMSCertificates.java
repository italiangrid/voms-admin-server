package org.glite.security.voms.admin.api.certificates;

public interface VOMSCertificates {

    public long getUserIdFromDn(String dn, String ca);
    
    public void addCertificate(long userId, X509Certificate cert);
    public void addCertificate(String registeredCertSubject, 
            String registeredCertIssuer, 
            X509Certificate cert);
        
    public X509Certificate[] getCertificates(long userId);
    public X509Certificate[] getCertificates(String registeredCertSubject,
            String registeredCertIssuer);
    
    public void suspendCertificate(X509Certificate cert, String reason);
    public void removeCertificate(X509Certificate cert);
    
}
