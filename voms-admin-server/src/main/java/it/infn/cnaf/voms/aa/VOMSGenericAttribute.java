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

package it.infn.cnaf.voms.aa;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.persistence.model.attribute.GenericAttributeValue;
import org.glite.security.voms.admin.persistence.model.attribute.VOMSBaseAttribute;
import org.glite.security.voms.admin.util.PathNamingScheme;

public class VOMSGenericAttribute {

  String name;
  String value;
  String context;

  private VOMSGenericAttribute() {

  }

  public String getContext() {

    return context;
  }

  public void setContext(String context) {

    this.context = context;
  }

  public String getName() {

    return name;
  }

  public void setName(String name) {

    this.name = name;
  }

  public String getValue() {

    return value;
  }

  public void setValue(String value) {

    this.value = value;
  }

  public boolean isUserAttribute() {

    assert context != null && !context.equals("");

    String voRootGroupName = "/" + VOMSConfiguration.instance().getVOName();

    return context.equals(voRootGroupName);
  }

  public boolean isGroupAttribute() {

    assert context != null && !context.equals("");

    return PathNamingScheme.isGroup(context);

  }

  public boolean isRoleAttribute() {

    assert context != null && !context.equals("");

    return PathNamingScheme.isQualifiedRole(context);
  }

  public static VOMSGenericAttribute fromModel(GenericAttributeValue attributeValue) {

    assert attributeValue != null;

    VOMSGenericAttribute ga = new VOMSGenericAttribute();

    ga.setName(attributeValue.getAttributeDescription().getName());
    ga.setValue(attributeValue.getValue());

    if (attributeValue.getContext() == null)
      ga.setContext(VOMSConfiguration.instance().getVOName());
    else
      ga.setContext(attributeValue.getContext());

    return ga;

  }

  @Override
  public String toString() {

    return String.format("%s = %s (%s)", name, value, context);
  }
}
