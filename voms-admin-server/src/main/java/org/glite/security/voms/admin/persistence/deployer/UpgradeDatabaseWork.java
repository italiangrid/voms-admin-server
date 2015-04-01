package org.glite.security.voms.admin.persistence.deployer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UpgradeDatabaseWork implements Work {

  private static final Logger LOG = LoggerFactory.getLogger(UpgradeDatabaseWork.class);
  
  final List<String> sqlCommands;
  
  public UpgradeDatabaseWork(List<String> sqlCommands) {
    this.sqlCommands = sqlCommands;
  }

  @Override
  public void execute(Connection connection) throws SQLException {
    
    Statement statement = connection.createStatement();
    
    try{
      
      for (String command: sqlCommands){
        
        LOG.info(command);
        
        statement.executeUpdate(command);
      
      }
      
    }catch(Throwable e){
      LOG.error("Error executing upgrade database work: "+e.getMessage(), e);
      throw e;
    }

  }

}
