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
package org.italiangrid.voms.aa.impl;

import org.glite.security.voms.admin.persistence.model.VOMSBaseAttribute;
import org.italiangrid.voms.VOMSGenericAttribute;

public class VOMSGAImpl implements VOMSGenericAttribute {

  final VOMSBaseAttribute attribute;

  public VOMSGAImpl(VOMSBaseAttribute ua) {

    this.attribute = ua;
  }

  @Override
  public String getName() {

    return attribute.getName();
  }

  @Override
  public String getValue() {

    return attribute.getValue();
  }

  @Override
  public String getContext() {

    return attribute.getContext();
  }

}
