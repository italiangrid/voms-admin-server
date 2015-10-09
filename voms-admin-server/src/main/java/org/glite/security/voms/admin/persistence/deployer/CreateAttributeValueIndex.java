package org.glite.security.voms.admin.persistence.deployer;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateAttributeValueIndex implements Runnable {

  private static final Logger LOG = LoggerFactory
    .getLogger(CreateAttributeValueIndex.class);

  private final Session session;

  public CreateAttributeValueIndex(Session s) {

    this.session = s;
  }

  @Override
  public void run() {

    String statement = "create index ua_value_idx on usr_attrs(a_id,a_value)";
    SQLQuery q = session.createSQLQuery(statement);
    q.executeUpdate();
    LOG.info(statement);

    session.flush();

  }

}
