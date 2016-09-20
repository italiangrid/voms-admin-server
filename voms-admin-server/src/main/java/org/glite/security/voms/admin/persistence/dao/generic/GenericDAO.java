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
package org.glite.security.voms.admin.persistence.dao.generic;

import java.io.Serializable;
import java.util.List;

/**
 * An interface shared by all business data access objects.
 * <p>
 * All CRUD (create, read, update, delete) basic data access operations are
 * isolated in this interface and shared accross all DAO implementations. The
 * current design is for a state-management oriented persistence layer (for
 * example, there is no UDPATE statement function) that provides automatic
 * transactional dirty checking of business objects in persistent state.
 * 
 * @author Christian Bauer
 * 
 */
public interface GenericDAO<T, ID extends Serializable> {

  T findById(ID id, boolean lock);

  List<T> findAll();

  List<T> findByExample(T exampleInstance, String... excludeProperty);

  T makePersistent(T entity);

  void makeTransient(T entity);

  /**
   * Affects every managed instance in the current persistence context!
   */
  void flush();

  /**
   * Affects every managed instance in the current persistence context!
   */
  void clear();

}
