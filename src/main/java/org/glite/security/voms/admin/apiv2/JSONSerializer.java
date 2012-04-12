package org.glite.security.voms.admin.apiv2;

import java.util.ArrayList;
import java.util.List;

import org.glite.security.voms.admin.persistence.model.VOMSUser;



public class JSONSerializer {
	
	
	public static List<SimpleVOMSUser> serialize(List<VOMSUser> users){
		
		List<SimpleVOMSUser> retval = new ArrayList<SimpleVOMSUser>();
		
		for (VOMSUser u: users)
			retval.add(SimpleVOMSUser.fromVOMSUser(u));
		
		return retval;
		
	}
	

}
