package org.glite.security.voms.admin.operations.users;

import java.util.List;

import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class FindUsersWithAttribute extends BaseVomsOperation<List<VOMSUser>> {

  public final String attributeName;

  public FindUsersWithAttribute(String attr) {

    attributeName = attr;
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), 
      VOMSPermission
      .getEmptyPermissions()
      .setAttributesReadPermission()
      .setMembershipReadPermission());

  }

  @Override
  protected List<VOMSUser> doExecute() {

    VOMSAttributeDAO dao = VOMSAttributeDAO.instance();
    return dao.findUsersWithAttribute(attributeName);
  }

}
