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
package org.italiangrid.voms.aa;

import org.italiangrid.voms.aa.impl.AAImpl;
import org.italiangrid.voms.aa.impl.DefaultVOMSAttributeResolver;
import org.italiangrid.voms.aa.impl.LegacyFQANEncoding;
import org.italiangrid.voms.aa.impl.LimitToOwnedFQANsPolicy;
import org.italiangrid.voms.aa.impl.NullFQANEncoding;

public class AttributeAuthorityFactory {

  private AttributeAuthorityFactory() {

  }

  public static AttributeAuthority newAttributeAuthority(
    long maxAttrsValidityInSecs, boolean legacyFQANEncoding) {

    if (legacyFQANEncoding)
      return new AAImpl(new DefaultVOMSAttributeResolver(
        new LegacyFQANEncoding(), new LimitToOwnedFQANsPolicy()),
        maxAttrsValidityInSecs);

    return new AAImpl(new DefaultVOMSAttributeResolver(new NullFQANEncoding(),
      new LimitToOwnedFQANsPolicy()), maxAttrsValidityInSecs);
  }

}
