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
package org.glite.security.voms.admin.persistence.dao;

public class SearchDAO {

  public String query;
  public String allObjectsQuery;
  public String countQuery;
  public String allObjectsCountQuery;

  public String getAllObjectsCountQuery() {

    return allObjectsCountQuery;
  }

  public void setAllObjectsCountQuery(String allObjectsCountQuery) {

    this.allObjectsCountQuery = allObjectsCountQuery;
  }

  public String getAllObjectsQuery() {

    return allObjectsQuery;
  }

  public void setAllObjectsQuery(String allObjectsQuery) {

    this.allObjectsQuery = allObjectsQuery;
  }

  public String getCountQuery() {

    return countQuery;
  }

  public void setCountQuery(String countQuery) {

    this.countQuery = countQuery;
  }

  public String getQuery() {

    return query;
  }

  public void setQuery(String query) {

    this.query = query;
  }

  public SearchResults getAll() {

    return null;

  }

  public SearchResults getAll(int firstResult, int maxResults) {

    return null;
  }

  public SearchResults search(String searchParameter, int firstResult,
    int maxResults) {

    return null;
  }
}
