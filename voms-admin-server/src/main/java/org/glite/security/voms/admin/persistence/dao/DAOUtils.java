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
package org.glite.security.voms.admin.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;

public class DAOUtils {

  public static List<VOMSGroup> filterUnvisibleGroups(List<VOMSGroup> groups) {

    Set<VOMSGroup> visiblePaths = new TreeSet<VOMSGroup>();
    Set<VOMSGroup> bannedPaths = new TreeSet<VOMSGroup>();

    List<VOMSGroup> filteredGroups = new ArrayList<VOMSGroup>();

    CurrentAdmin admin = CurrentAdmin.instance();

    for (VOMSGroup g : groups) {

      VOMSGroup parent = g.getParent();

      if (visiblePaths.contains(parent)) {
        filteredGroups.add(g);
        continue;
      }

      boolean descendsFromBannedPath = false;
      for (VOMSGroup bannedPath : bannedPaths) {
        if (g.isDescendant(bannedPath)) {
          descendsFromBannedPath = true;
          break;
        }
      }

      if (descendsFromBannedPath)
        continue;

      if (admin.hasPermissions(VOMSContext.instance(parent),
        VOMSPermission.getContainerReadPermission())) {
        visiblePaths.add(parent);
        filteredGroups.add(g);
      } else {
        bannedPaths.add(parent);
      }
    }

    return filteredGroups;
  }

}
