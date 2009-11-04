package it.infn.cnaf.voms.aa;

import org.glite.security.voms.admin.common.PathNamingScheme;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSMapping;

/**
 * 
 * @author Andrea Ceccanti
 *
 */
public class VOMSFQAN {

    String FQAN;

    
    private VOMSFQAN(String fqan) {

        FQAN = fqan;
    }
    
    public String getFQAN() {
    
        return FQAN;
    }

    
    public void setFQAN( String fqan ) {
    
        FQAN = fqan;
    }
    

    public String toString() {
    
        return FQAN;
    }
    
    public boolean isGroup(){
        assert FQAN != null;
        return PathNamingScheme.isGroup( FQAN );
        
    }
    
    public boolean isRole(){
        assert FQAN != null;
        return PathNamingScheme.isQualifiedRole( FQAN );   
    }
    
    public static VOMSFQAN fromModel(VOMSMapping m){
        
        return new VOMSFQAN(m.getFQAN());
        
    }
    
    public static VOMSFQAN fromModel(VOMSGroup m){
        
        return new VOMSFQAN(m.getName());
       
    }
    
    
    public static VOMSFQAN fromString(String fqan){
        assert (PathNamingScheme.isGroup( fqan ) || PathNamingScheme.isQualifiedRole( fqan )): "Illegal FQAN passed as argument: "+fqan;
        
        return new VOMSFQAN(fqan);
             
    }
    
    public boolean equals( Object obj ) {
    
        if (FQAN == null)
            return super.equals( obj );
        
        VOMSFQAN that = (VOMSFQAN) obj;
        
        return this.FQAN.equals( that.FQAN );
        
    }
    
    public int hashCode() {
    
        if (FQAN == null)
            return super.hashCode();
        
        return FQAN.hashCode();
    }
 
}
