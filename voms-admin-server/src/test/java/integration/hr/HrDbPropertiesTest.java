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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.glite.security.voms.admin.integration.cern.HrDbError;
import org.glite.security.voms.admin.integration.cern.HrDbProperties;
import org.junit.Test;

public class HrDbPropertiesTest {


  @Test
  public void testRequiredProperties() {

    Properties props = new Properties();
    
    for (String k: HrDbProperties.REQUIRED_KEYS) {
      try {
        
        HrDbProperties.fromProperties(props);
        fail("Expected error not thrown");

      } catch (HrDbError e) {
        assertThat(e.getMessage(), containsString(String.format("required property '%s' not found", k)));
        props.setProperty(k, "");
        
      }
    }
    
    HrDbProperties.fromProperties(props);
  }

  @Test
  public void testValueParsing() {
    Properties props = new Properties();
    
    props.setProperty(HrDbProperties.EXPERIMENT_KEY, "atlas");
    props.setProperty(HrDbProperties.API_ENDPOINT_KEY, "http://example");
    props.setProperty(HrDbProperties.API_USERNAME_KEY, "test");
    props.setProperty(HrDbProperties.API_PASSWORD_KEY, "password");
    props.setProperty(HrDbProperties.MEMBERSHIP_CHECK_ENABLED_KEY, "false");
    props.setProperty(HrDbProperties.MEMBERSHIP_CHECK_PERIOD_KEY, "86400");
    
    HrDbProperties hrConfig = HrDbProperties.fromProperties(props);
    
    assertThat(hrConfig.getExperimentName(), is("atlas"));
    assertThat(hrConfig.getApi().getEndpoint(), is("http://example"));
    assertThat(hrConfig.getApi().getUsername(), is("test"));
    assertThat(hrConfig.getApi().getPassword(), is("password"));
    assertThat(hrConfig.getMembesrshipCheck().isEnabled(), is(false));
    assertThat(hrConfig.getMembesrshipCheck().getPeriodInSeconds(), is(86400L));
  }
  
  @Test
  public void testApiTimeoutChecks() {
    Properties props = new Properties();
    
    props.setProperty(HrDbProperties.EXPERIMENT_KEY, "atlas");
    props.setProperty(HrDbProperties.API_ENDPOINT_KEY, "http://example");
    props.setProperty(HrDbProperties.API_USERNAME_KEY, "test");
    props.setProperty(HrDbProperties.API_PASSWORD_KEY, "password");
    props.setProperty(HrDbProperties.API_TIMEOUT_KEY, "-1");
    
    HrDbProperties hrConfig = HrDbProperties.fromProperties(props);
    assertThat(hrConfig.getApi().getTimeoutInSeconds(), is(5L));
    
    props.setProperty(HrDbProperties.API_TIMEOUT_KEY, "1");
    hrConfig = HrDbProperties.fromProperties(props);
    assertThat(hrConfig.getApi().getTimeoutInSeconds(), is(1L)); 
  }

}
