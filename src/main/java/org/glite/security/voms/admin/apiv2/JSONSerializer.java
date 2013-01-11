package org.glite.security.voms.admin.apiv2;

import java.util.ArrayList;
import java.util.List;

import org.glite.security.voms.admin.persistence.model.VOMSUser;



public class JSONSerializer {
	
	
	public static List<VOMSUserJSON> serialize(List<VOMSUser> users){
		
		List<VOMSUserJSON> retval = new ArrayList<VOMSUserJSON>();
		
		for (VOMSUser u: users)
			retval.add(VOMSUserJSON.fromVOMSUser(u));
		
		return retval;
		
	}
	

}
