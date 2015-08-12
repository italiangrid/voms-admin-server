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
package org.glite.security.voms.admin.persistence.deployer;

import java.util.Iterator;
import java.util.List;

import org.glite.security.voms.admin.operations.VOMSPermission;

public class ACLMapper {

  public static final short ALL = 1;
  public static final short CREATE = 2;
  public static final short DELETE = 3;
  public static final short ADD = 4;
  public static final short REMOVE = 5;
  public static final short SET_ACL = 6;
  public static final short GET_ACL = 7;
  public static final short SET_DEFAULT_ACL = 8;
  public static final short GET_DEFAULT_ACL = 9;
  public static final short LIST = 10;
  public static final short LIST_ANY_REQUEST = 11;
  public static final short DELETE_ANY_REQUEST = 11;

  public static VOMSPermission translatePermissions(List operations) {

    VOMSPermission perms = VOMSPermission.getEmptyPermissions();

    Iterator i = operations.iterator();

    while (i.hasNext()) {

      short op = ((Short) i.next()).shortValue();

      switch (op) {

      case ALL:
        return VOMSPermission.getAllPermissions();

      case CREATE:
      case DELETE:
        perms.setContainerReadPermission().setContainerWritePermission()
          .setMembershipRWPermission();
        break;

      case ADD:
      case REMOVE:
        perms.setContainerReadPermission().setMembershipRWPermission();
        break;

      case SET_ACL:
        perms.setACLReadPermission().setACLWritePermission();
        break;

      case GET_ACL:
        perms.setACLReadPermission();
        break;

      case SET_DEFAULT_ACL:
        perms.setACLReadPermission().setACLWritePermission()
          .setACLDefaultPermission();
        break;

      case GET_DEFAULT_ACL:
        perms.setACLDefaultPermission().setACLReadPermission();
        break;

      case LIST:
        perms.setContainerReadPermission().setMembershipReadPermission();
        break;

      default:
        continue;
      }
    }

    return perms;
  }
}
