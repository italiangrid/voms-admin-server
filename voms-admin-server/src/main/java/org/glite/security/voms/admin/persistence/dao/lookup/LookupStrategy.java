/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package org.glite.security.voms.admin.persistence.dao.lookup;

/**
 * 
 * A strategy to resolve an entity based on certificate subject and issuer
 *
 */
public interface LookupStrategy {

  /**
   * A strategy to lookup an entity by certificate, delegating the actual lookup
   * to the {@link FindByCertificateDAO} passed as argument.
   * 
   * The strategy is basically a selector for which method to call on {@link
   * FindByCertificateDAO} to resolve the entity.
   * 
   * @param dao the dao used to resolve the entity by certificate lookup
   * 
   * @param certificateSubject a certificate subject
   * 
   * @param certificateIssuer a certificate issuer
   * 
   * @return the entity linked to the certificate subject passed as argument,
   * <code>null</code> if no entity is found
   */
  public <T> T lookup(FindByCertificateDAO<T> dao, String certificateSubject,
    String certificateIssuer);

}
