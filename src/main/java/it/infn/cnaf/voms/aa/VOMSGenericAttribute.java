package it.infn.cnaf.voms.aa;

import org.glite.security.voms.admin.common.PathNamingScheme;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.model.VOMSBaseAttribute;


public class VOMSGenericAttribute {
    
    String name;
    String value;
    String context;
    
    
    private VOMSGenericAttribute() {}
    
    
    public String getContext() {
    
        return context;
    }
    
    public void setContext( String context ) {
    
        this.context = context;
    }
    
    public String getName() {
    
        return name;
    }
    
    public void setName( String name ) {
    
        this.name = name;
    }
    
    public String getValue() {
    
        return value;
    }
    
    public void setValue( String value ) {
    
        this.value = value;
    }
    
    public boolean isUserAttribute(){
        assert context != null && ! context.equals( "" );
        
        String voRootGroupName = "/"+VOMSConfiguration.instance().getVOName();
        
        return context.equals( voRootGroupName );
    }
    
    public boolean isGroupAttribute(){
        assert context != null && ! context.equals( "" );
        
        return PathNamingScheme.isGroup( context );
        
    }
    
    public boolean isRoleAttribute(){
        assert context != null && ! context.equals( "" );
        
        return PathNamingScheme.isQualifiedRole( context );
    }

    public static VOMSGenericAttribute fromModel(VOMSBaseAttribute attributeValue){
        assert attributeValue != null;
        
        VOMSGenericAttribute ga = new VOMSGenericAttribute();
        
        ga.setName( attributeValue.getAttributeDescription().getName() );
        ga.setValue( attributeValue.getValue() );
        
        if (attributeValue.getContext() == null)
            ga.setContext( VOMSConfiguration.instance().getVOName() );
        else
            ga.setContext( attributeValue.getContext() );
        
        return ga;
        
    }
    
    @Override
    public String toString() {
    	return String.format("%s = %s (%s)", name,value, context);
    }
}
