package it.infn.cnaf.voms.aa;


import java.util.List;

/**
 * 
 * @author Andrea Ceccanti
 * @author Valerio Venturi
 *
 */
public interface VOMSAttributeAuthority {
        
    public VOMSAttributes getVOMSAttributes(String dn);
    
    public VOMSAttributes getVOMSAttributes(String dn, 
            List<String> requestedFQANs);
    
    public VOMSAttributes getVOMSAttributes(String dn, String ca,
            List<String> requestedFQANs);
    
    public VOMSAttributes getVOMSAttributes(String dn, String ca);
    
    public VOMSAttributes getAllVOMSAttributes(String dn);
    
    public VOMSAttributes getAllVOMSAttributes(String dn, String ca);
}
