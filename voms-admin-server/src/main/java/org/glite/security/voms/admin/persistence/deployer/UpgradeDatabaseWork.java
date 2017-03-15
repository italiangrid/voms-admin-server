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
