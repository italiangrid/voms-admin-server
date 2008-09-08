package org.glite.security.voms.admin.version;

import java.util.regex.Pattern;



public class VersionNumber implements EndpointVersion{

    private static final String versionRegex= "\\d+\\.\\d+\\.\\d+$";
    private static final Pattern versionPattern = Pattern.compile( versionRegex );
    
    private String version;
    
    private int majorVersionNumber = 0;
    private int minorVersionNumber = 0;
    private int patchVersionNumber = 1;
    
    
    
    private void parseVersion(){
         
        if (!versionPattern.matcher( version ).matches())
            throw new IllegalArgumentException("voms version number must comply to the following format: MAJOR.MINOR.PATCH.");
        
        String tokens[] = version.split( "\\." );
        
        if (tokens.length != 3)
            throw new IllegalArgumentException("voms version numbers must comply to the following format: MAJOR.MINOR.PATCH.");
        
        majorVersionNumber = Integer.valueOf( tokens[0] );
        minorVersionNumber = Integer.valueOf( tokens[1] );
        patchVersionNumber = Integer.valueOf( tokens[2] );
    }
    
    
    public VersionNumber(String versionString) {

        assert versionString != null: "Cannot create a VersionNumber from a null string!";       
        version = versionString;
        
        parseVersion();
        
    }

    public int getMajorVersionNumber() {

        
        return majorVersionNumber;
    }

    public int getMinorVersionNumber() {

        return minorVersionNumber;
    }

    public int getPatchVersionNumber() {

        return patchVersionNumber;
    }

    public String getVersion() {

        return version;
    }
    
    public String toString() {
    
        return version;
    }
    
    public static void main( String[] args ) {

        VersionNumber vn = new VersionNumber("22.567.18");
        
        System.out.println(vn.getMajorVersionNumber());
        System.out.println(vn.getMinorVersionNumber());
        System.out.println(vn.getPatchVersionNumber());
        
        System.out.println(vn);
        
    }
}
