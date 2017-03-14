package org.glite.security.voms.admin.persistence.deployer;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.jdbc.Work;

public class GetCatalogWork implements Work{

  private String catalogName;
  
  @Override
  public void execute(Connection connection) throws SQLException {

   catalogName = connection.getCatalog(); 
    
  }
  
  public String getCatalogName() {

    return catalogName;
  }
}
