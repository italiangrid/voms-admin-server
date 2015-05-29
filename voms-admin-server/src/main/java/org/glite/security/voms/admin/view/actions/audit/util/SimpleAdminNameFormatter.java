package org.glite.security.voms.admin.view.actions.audit.util;

import org.glite.security.voms.admin.core.VOMSServiceConstants;


public class SimpleAdminNameFormatter implements AdminNameFormatter {


  @Override
  public String formatAdminName(String adminName) {
    if (adminName == null){
      return adminName;
    }
      
    if (adminName.equals(VOMSServiceConstants.INTERNAL_ADMIN)){
      return "VOMS Admin";
    }
    
    return adminName;
  }

}
