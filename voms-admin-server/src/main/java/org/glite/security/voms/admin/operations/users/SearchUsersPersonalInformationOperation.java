package org.glite.security.voms.admin.operations.users;

import java.util.concurrent.Callable;

import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.operations.BaseVoReadOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.SearchResults;

public class SearchUsersPersonalInformationOperation
  extends BaseVoReadOperation<SearchResults> {

  private final Callable<SearchResults> searchCallable;

  public static SearchUsersPersonalInformationOperation instance(
    Callable<SearchResults> callable) {

    return new SearchUsersPersonalInformationOperation(callable);
  }

  private SearchUsersPersonalInformationOperation(
    Callable<SearchResults> searchCallable) {
    this.searchCallable = searchCallable;
  }

  @Override
  protected SearchResults doExecute() {

    try {
      return searchCallable.call();
    } catch (Exception e) {
      throw new VOMSException(e);
    }

  }

  @Override
  protected void setupPermissions() {

    // @formatter:off
    addRequiredPermission(VOMSContext.getVoContext(), 
      VOMSPermission
        .getEmptyPermissions()
        .setContainerReadPermission()
        .setMembershipReadPermission()
        .setPersonalInfoReadPermission());
  }

}
