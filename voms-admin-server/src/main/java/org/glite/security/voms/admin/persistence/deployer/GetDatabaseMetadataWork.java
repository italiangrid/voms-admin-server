package org.glite.security.voms.admin.persistence.deployer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.hibernate.jdbc.Work;

public class GetDatabaseMetadataWork implements Work {

  DatabaseMetaData metadata;

  @Override
  public void execute(Connection connection) throws SQLException {

    metadata = connection.getMetaData();

  }

  public DatabaseMetaData getMetadata() {

    return metadata;
  }

}
