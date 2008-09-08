package org.glite.security.voms.admin.version;


public interface EndpointVersion {
    
    public int getMajorVersionNumber();
    public int getMinorVersionNumber();
    public int getPatchVersionNumber();
    
    public String getVersion();

}
