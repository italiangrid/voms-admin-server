package org.glite.security.voms.admin.operations.users;

import java.util.List;

import org.glite.security.voms.admin.operations.BaseVoReadOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class ListAUPFailingUsersOperation extends
  BaseVoReadOperation<List<VOMSUser>> {

  @Override
  protected List<VOMSUser> doExecute() {

    AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();
    return VOMSUserDAO.instance().findAUPFailingUsers(aupDAO.getVOAUP());

  }

}
