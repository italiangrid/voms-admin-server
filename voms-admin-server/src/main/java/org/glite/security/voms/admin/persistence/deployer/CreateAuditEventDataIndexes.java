/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
