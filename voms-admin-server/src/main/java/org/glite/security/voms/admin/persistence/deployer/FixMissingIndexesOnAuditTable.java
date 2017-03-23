/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.glite.security.voms.admin.error.VOMSException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixMissingIndexesOnAuditTable implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(FixMissingIndexesOnAuditTable.class);

  private final Session session;

  public FixMissingIndexesOnAuditTable(Session s) {
    session = s;
  }

  private void safeCloseResultSet(ResultSet rs) {

    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        LOG.warn("Exception caught closing result set: " + e.getMessage(), e);
      }
    }
  }

  private void createIndexes() throws SQLException {

    LOG.info("No indexes found, the indexes will be created...");

    String[] createIndexCommands =
        new String[] {"create index ae_type_idx on audit_event (event_timestamp, event_type)",
            "create index ae_principal_idx on audit_event (principal, event_timestamp)"};

    for (String cmd : createIndexCommands) {
      LOG.info(cmd);
      SQLQuery q = session.createSQLQuery(cmd);
      q.executeUpdate();
    }
  }

  @Override
  public void run() {

    LOG.info("Checking indexes on audit_event table...");

    ResultSet rs = null;

    try {
      GetCatalogWork gcw = new GetCatalogWork();
      session.doWork(gcw);
      
      String dbName = gcw.getCatalogName();

      LOG.info("Database name: {}", dbName);

      String checkIndexQuery =
          String.format("select count(index_name) from information_schema.statistics "
              + "where table_schema = '%s' and table_name = 'audit_event' "
              + "and column_name = 'event_timestamp'", dbName);

      SQLQuery q = session.createSQLQuery(checkIndexQuery);

      BigInteger indexCount = (BigInteger) q.uniqueResult();

      LOG.info("indexCount: {}", indexCount);

      if (indexCount.intValue() == 0) {
        createIndexes();
      } else if (indexCount.intValue() == 2) {
        LOG.info("Indexes found on audit_event, no action required.");
        return;
      } else {
        throw new VOMSException(
            "Invalid schema configuration: audit_event table has an invalid number of indexes: "
                + indexCount);
      }

    } catch (SQLException e) {
      LOG.error("SQL error: " + e.getMessage(), e);
      throw new VOMSException(e);

    } finally {
      safeCloseResultSet(rs);
    }

  }

}
