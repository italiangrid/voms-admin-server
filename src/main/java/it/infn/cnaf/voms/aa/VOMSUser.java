package it.infn.cnaf.voms.aa;

/**
 * 
 * @author Andrea Ceccanti
 * @author Valerio Venturi
 *
 */
public class VOMSUser {
    
    String dn;
    String ca;
    
    public String getCa() {
    
        return ca;
    }
    
    public void setCa( String ca ) {
    
        this.ca = ca;
    }
    
    public String getDn() {
    
        return dn;
    }
    
    public void setDn( String dn ) {
    
        this.dn = dn;
    }
    

    public static VOMSUser fromModel(org.glite.security.voms.admin.model.VOMSUser user){
        
        VOMSUser u = new VOMSUser();
        
        u.setDn( user.getDn() );
        u.setCa( user.getCa().getDn() );
        
        return u;
    }
    
}
