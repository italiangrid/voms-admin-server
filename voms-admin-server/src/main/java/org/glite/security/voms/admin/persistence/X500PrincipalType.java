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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.security.auth.x500.X500Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class X500PrincipalType implements UserType {

  private static final Logger log = LoggerFactory
    .getLogger(X500PrincipalType.class);

  private static final int[] SQL_TYPES = { Types.BLOB };

  private static final int BUF_SIZE = 2048;

  public X500PrincipalType() {

    super();

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

  public boolean isMutable() {

    return true;
  }

  private X500Principal readPrincipal(InputStream is) {

    try {

      byte[] buf = new byte[BUF_SIZE];

      int readBytes = is.read(buf);

      byte[] val = new byte[readBytes];

      System.arraycopy(buf, 0, val, 0, readBytes);

      return new X500Principal(val);

    } catch (IOException e) {
      log.error("Error deserializing principal DER representation!");
      return null;

    }

  }

  public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
    throws HibernateException, SQLException {

    InputStream is = rs.getBinaryStream(names[0]);

    if (rs.wasNull())
      return null;

    return readPrincipal(is);
  }

  public void nullSafeSet(PreparedStatement statement, Object value, int index)
    throws HibernateException, SQLException {

    if (value == null) {

      statement.setNull(index, Types.BLOB);

    } else {

      X500Principal p = (X500Principal) value;
      ByteArrayInputStream bas = new ByteArrayInputStream(p.getEncoded());
      statement.setBinaryStream(index, bas, p.getEncoded().length);

    }

  }

  public Class returnedClass() {

    return X500Principal.class;

  }

  public int[] sqlTypes() {

    return SQL_TYPES;
  }

  public Object deepCopy(Object value) throws HibernateException {

    if (value == null)
      return null;

    X500Principal p = (X500Principal) value;
    X500Principal clone = new X500Principal(p.getEncoded());

    return clone;

  }

  public Object assemble(Serializable cached, Object owner)
    throws HibernateException {

    return deepCopy(cached);
  }

  public Object replace(Object original, Object target, Object owner)
    throws HibernateException {

    return deepCopy(original);
  }

  public Serializable disassemble(Object value) throws HibernateException {

    return (Serializable) deepCopy(value);
  }

}
