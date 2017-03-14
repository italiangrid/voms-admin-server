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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

public class PermissionType implements UserType {

  public static final Logger log = LoggerFactory
    .getLogger(PermissionType.class);

  private static final int[] SQL_TYPES = { Types.INTEGER };

  public PermissionType() {

    super();
  }

  public int[] sqlTypes() {

    return SQL_TYPES;
  }

  public Class<VOMSPermission> returnedClass() {

    return VOMSPermission.class;
  }

  public boolean equals(Object x, Object y) throws HibernateException {

    if (x == y)
      return true;

    if (x == null || y == null)
      return false;

    return x.equals(y);

  }

  public int hashCode(Object x) throws HibernateException {

    return x.hashCode();

  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names,
    SessionImplementor session, Object owner) throws HibernateException,
    SQLException {
    
    int bits = rs.getInt(names[0]);
    
    if (rs.wasNull()){
      return null;
    }
    
    return new VOMSPermission(bits);
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index,
    SessionImplementor session) throws HibernateException, SQLException {

    if (value  == null){
      st.setNull(index, Types.INTEGER);
    }else{
      
      VOMSPermission p = (VOMSPermission)value;
      st.setInt(index, p.getBits());
    } 
  }
  
  public Object deepCopy(Object value) throws HibernateException {

    VOMSPermission p = (VOMSPermission) value, clone = null;

    if (value == null)
      return null;

    try {

      clone = (VOMSPermission) p.clone();

    } catch (CloneNotSupportedException e) {
      log.info(e.getMessage(), e);
      return value;
    }

    return clone;
  }

  public boolean isMutable() {

    return true;
  }

  public Serializable disassemble(Object value) throws HibernateException {

    return (Serializable) deepCopy(value);
  }

  public Object assemble(Serializable cached, Object owner)
    throws HibernateException {

    return deepCopy(cached);
  }

  public Object replace(Object original, Object target, Object owner)
    throws HibernateException {

    return deepCopy(original);
  }

  

  

}
