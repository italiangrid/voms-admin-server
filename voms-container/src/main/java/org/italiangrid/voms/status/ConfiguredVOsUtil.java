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
package org.italiangrid.voms.status;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.italiangrid.voms.container.SysconfigUtil;

public class ConfiguredVOsUtil {

  private ConfiguredVOsUtil() {

  }

  public static List<String> getConfiguredVONames() {

    TreeSet<String> voNames = new TreeSet<String>();

    File confDir = new File(SysconfigUtil.getConfDir());

    if (confDir.exists() && confDir.isDirectory() && confDir.canExecute()
      && confDir.canRead()) {

      File[] voFiles = confDir.listFiles(new FileFilter() {

        @Override
        public boolean accept(File pathname) {

          return pathname.isDirectory();
        }
      });

      for (File f : voFiles)
        voNames.add(f.getName());

    }

    return new ArrayList<String>(voNames);
  }
}
