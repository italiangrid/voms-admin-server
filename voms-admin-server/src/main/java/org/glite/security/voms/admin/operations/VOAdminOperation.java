package org.glite.security.voms.admin.operations;

import java.util.concurrent.Callable;

import org.glite.security.voms.admin.error.VOMSException;

public class VOAdminOperation<V> extends BaseVomsOperation<V> {

  private final Callable<V> task;

  public VOAdminOperation(Callable<V> task) {

    this.task = task;

  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(),
      VOMSPermission.getAllPermissions());
  }

  @Override
  protected V doExecute() {

    V result = null;

    try {

      result = task.call();

    } catch (Exception e) {
      throw new VOMSException("Error while executing task", e);
    }

    return result;
  }

}
