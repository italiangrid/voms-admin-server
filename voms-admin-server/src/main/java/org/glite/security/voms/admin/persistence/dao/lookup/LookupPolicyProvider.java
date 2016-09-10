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
 * A provider for the {@link LookupStrategy} used by DAOs for looking up user,
 * admins and certificate by certificate subject.
 * 
 * This is a singleton, and should be initialized with the {@link
 * #initialize(boolean)} method before any call to the {@link #instance()}
 * method.
 *
 */
public class LookupPolicyProvider {

  public static volatile LookupPolicyProvider INSTANCE = null;

  private final LookupStrategy lookupStrategy;

  private LookupPolicyProvider(boolean skipCaCheck) {

    if (skipCaCheck) {
      lookupStrategy = new LookupBySubject();
    } else {
      lookupStrategy = new LookupBySubjectAndIssuer();
    }

  }

  public static LookupPolicyProvider instance() {

    if (INSTANCE == null) {
      throw new IllegalStateException(
        "LookupPolicyProvider not initialized correctly!");
    }

    return INSTANCE;
  }

  public LookupStrategy lookupStrategy() {

    return lookupStrategy;
  }

  public synchronized static void initialize(boolean skipCaChek) {

    if (INSTANCE != null) {
      throw new IllegalStateException(
        "LookupPolicyProvider already initialized!");
    }

    INSTANCE = new LookupPolicyProvider(skipCaChek);
  }
}
