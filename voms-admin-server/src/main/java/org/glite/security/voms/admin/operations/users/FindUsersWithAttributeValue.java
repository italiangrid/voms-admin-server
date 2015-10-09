package org.glite.security.voms.admin.operations.users;

import java.util.List;

import org.glite.security.voms.admin.persistence.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class FindUsersWithAttributeValue extends FindUsersWithAttribute {

  final String attributeValue;

  public FindUsersWithAttributeValue(String attr, String val) {

    super(attr);
    attributeValue = val;

  }

  @Override
  protected List<VOMSUser> doExecute() {

    VOMSAttributeDAO dao = VOMSAttributeDAO.instance();

    return dao.findUsersWithAttributeValue(attributeName, attributeValue);
  }

}
