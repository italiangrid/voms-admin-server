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
package org.glite.security.voms.admin.integration.cern;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

  private DateUtils() {
    // empty on purpose
  }

  public static final Date parseDate(String dateString) {

    LocalDate date = LocalDate.parse(dateString);
    return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

}
