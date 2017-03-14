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
package org.glite.security.voms.admin.apiv2;

import java.util.ArrayList;
import java.util.List;

import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class JSONSerializer {

  private static final VOMSPermission VO_ATTRIBUTE_READ_PERMISSIONS = VOMSPermission
    .getEmptyPermissions().setAttributesReadPermission();

  private static final VOMSPermission VO_MEMBERSHIP_READ_PERMISSIONS = VOMSPermission
    .getEmptyPermissions().setMembershipReadPermission()
    .setContainerReadPermission();

  private static final VOMSPermission PI_READ_PERMISSIONS = VOMSPermission
    .getEmptyPermissions().setPersonalInfoReadPermission();

  public static VOMSUserJSON serialize(VOMSUser user) {

    final VOMSContext voContext = VOMSContext.getVoContext();

    final CurrentAdmin admin = CurrentAdmin.instance();

    VOMSUserJSON json = VOMSUserJSON.fromVOMSUser(user);

    if (admin.is(user)) {

      json.fqansFrom(user);
      json.attributesFrom(user);
      json.personalInformationFrom(user);

    } else {

      if (admin.hasPermissions(voContext, VO_ATTRIBUTE_READ_PERMISSIONS)) {
        json.attributesFrom(user);
      }

      if (admin.hasPermissions(VOMSContext.getVoContext(),
        VO_MEMBERSHIP_READ_PERMISSIONS)) {
        json.fqansFrom(user);
      }

      if (admin.hasPermissions(VOMSContext.getVoContext(), PI_READ_PERMISSIONS)) {
        json.personalInformationFrom(user);
      }

    }

    return json;

  }

  public static List<VOMSUserJSON> serialize(List<VOMSUser> users) {

    List<VOMSUserJSON> retval = new ArrayList<VOMSUserJSON>();

    for (VOMSUser u : users) {
      retval.add(serialize(u));
    }

    return retval;

  }

}
