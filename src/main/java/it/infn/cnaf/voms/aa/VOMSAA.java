package it.infn.cnaf.voms.aa;

import it.infn.cnaf.voms.aa.impl.VOMSAAImpl;


public class VOMSAA {
    
    private static VOMSAttributeAuthority vomsAAInstance = null;
    
    public static VOMSAttributeAuthority getVOMSAttributeAuthority(){
        
        if (vomsAAInstance == null)
            vomsAAInstance = new VOMSAAImpl();
        
        return vomsAAInstance;
          
    }
}
