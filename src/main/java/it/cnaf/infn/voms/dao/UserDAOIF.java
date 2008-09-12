package it.cnaf.infn.voms.dao;

import org.glite.security.voms.admin.model.VOMSUser;


public interface UserDAOIF extends GenericDAO <VOMSUser, Long> {
    
    VOMSUser findByEmailAddress(String emailAddress);
    
    VOMSUser findByCertificate(String subject);
    
    VOMSUser findByCertificate(String subject, String issuerSubject);
    

}
