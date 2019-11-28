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
package integration.hr;

import static utils.TestFileUtils.readFile;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class HrDbTestSupport {

  public static final Instant NOW = Instant.parse("2019-01-01T00:00:00.00Z");
  
  public static final Instant TWO_YEARS_AGO = NOW.minus(730, ChronoUnit.DAYS);
  public static final Instant ONE_YEAR_AGO = NOW.minus(365, ChronoUnit.DAYS);
  public static final Instant ONE_WEEK_AGO = NOW.minus(7, ChronoUnit.DAYS);

  public static final Instant A_DAY_AGO = NOW.minus(1, ChronoUnit.DAYS);

  public static final Instant ONE_WEEK_FROM_NOW = NOW.plus(7, ChronoUnit.DAYS);
  public static final Instant ONE_YEAR_FROM_NOW = NOW.plus(365, ChronoUnit.DAYS);
  
  public static final Clock CLOCK = Clock.fixed(NOW, ZoneId.systemDefault());

  public static final String VO_PERSON_1_JSON_PATH = "src/test/resources/cern/json/voPerson1.json";
  public static final String VO_PERSON_1_JSON = readFile(VO_PERSON_1_JSON_PATH);


}
