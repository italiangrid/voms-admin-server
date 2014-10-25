package org.glite.security.voms.admin.integration.orgdb.strategies;

import org.glite.security.voms.admin.persistence.model.VOMSUser;

public interface OrgDBEmailAddressValidationStrategy {

  public OrgDBEmailValidationResult validateEmailAddress(VOMSUser u,
    String emailAddress);

}
