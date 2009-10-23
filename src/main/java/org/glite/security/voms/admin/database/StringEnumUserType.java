/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.database;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.*;
import org.hibernate.util.ReflectHelper;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.ParameterizedType;

/**
 * A generic UserType that handles String-based JDK 5.0 Enums.
 * 
 * @author Gavin King
 */
public class StringEnumUserType implements EnhancedUserType, ParameterizedType {

	private Class<Enum> enumClass;

	public void setParameterValues(Properties parameters) {
		String enumClassName = parameters.getProperty("enumClassname");
		try {
			enumClass = ReflectHelper.classForName(enumClassName);
		} catch (ClassNotFoundException cnfe) {
			throw new HibernateException("Enum class not found", cnfe);
		}
	}

	public Class returnedClass() {
		return enumClass;
	}

	public int[] sqlTypes() {
		return new int[] { Hibernate.STRING.sqlType() };
	}

	public boolean isMutable() {
		return false;
	}

	public Object deepCopy(Object value) {
		return value;
	}

	public Serializable disassemble(Object value) {
		return (Enum) value;
	}

	public Object replace(Object original, Object target, Object owner) {
		return original;
	}

	public Object assemble(Serializable cached, Object owner) {
		return cached;
	}

	public boolean equals(Object x, Object y) {
		return x == y;
	}

	public int hashCode(Object x) {
		return x.hashCode();
	}

	public Object fromXMLString(String xmlValue) {
		return Enum.valueOf(enumClass, xmlValue);
	}

	public String objectToSQLString(Object value) {
		return '\'' + ((Enum) value).name() + '\'';
	}

	public String toXMLString(Object value) {
		return ((Enum) value).name();
	}

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
			throws SQLException {
		String name = rs.getString(names[0]);
		return rs.wasNull() ? null : Enum.valueOf(enumClass, name);
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws SQLException {
		if (value == null) {
			st.setNull(index, Hibernate.STRING.sqlType());
		} else {
			st.setString(index, ((Enum) value).name());
		}
	}

}
