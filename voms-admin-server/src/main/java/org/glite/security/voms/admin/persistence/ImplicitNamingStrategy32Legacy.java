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
package org.glite.security.voms.admin.persistence;

import java.util.List;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitForeignKeyNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl;

public class ImplicitNamingStrategy32Legacy extends ImplicitNamingStrategyLegacyHbmImpl {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * This method's code provides the logic that was formerly used by hibernate
   * (3.2.x) to generate unique column names used in generated foreign key constraints.
   * Is kept here as reference
   * 
   */
  public static String uniqueColumnName(String tableName, List<String> columnNames,
      String referencedTableName) {
    int result = 0;
    if (referencedTableName != null) {
      result += referencedTableName.hashCode();
    }

    for (String n : columnNames) {
      result += n.hashCode();
    }

    return (Integer.toHexString(tableName.hashCode()) + Integer.toHexString(result)).toUpperCase();
  }

  @Override
  public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
    int result = 0;
    
    if (source.getReferencedTableName() != null){
      
      Identifier referencedTableName = source.getReferencedTableName();
      result += referencedTableName.getCanonicalName().hashCode();
    }
    
    for (Identifier i: source.getColumnNames()){
      String text = i.getText();
      String canonicalName = i.getCanonicalName();
      result += text.hashCode();
    }
    
    String tableCanonicalName = source.getTableName().getCanonicalName();
    String tableTextName = source.getTableName().getText();
    
    String fkIdentifier = "FK" + (Integer.toHexString(source.getTableName().getText().hashCode())
        + Integer.toHexString(result)).toUpperCase();
    
    return toIdentifier(fkIdentifier, source.getBuildingContext());
  }
}
