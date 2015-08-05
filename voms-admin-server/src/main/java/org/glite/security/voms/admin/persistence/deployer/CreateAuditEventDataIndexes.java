package org.glite.security.voms.admin.persistence.deployer;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class CreateAuditEventDataIndexes implements Runnable {

  private static final Logger LOG = LoggerFactory
    .getLogger(CreateAuditEventDataIndexes.class);
  
  private final Session session;
  
  
  public CreateAuditEventDataIndexes(Session session) {
    this.session = session;
  }

  @Override
  public void run() {

    String[] statements = {
      "create index aed_value_idx on audit_event_data(value)",
      "create index aed_name_idx on audit_event_data(name)"
    };
    
    
    for (String s: statements){
      SQLQuery q = session.createSQLQuery(s);
      q.executeUpdate();
      LOG.info(s);
    }
    
    session.flush();
  }

}
