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
package validation;

import org.glite.security.voms.admin.util.validation.x509.CanlDNValidator;
import org.glite.security.voms.admin.util.validation.x509.DnValidationResult;
import org.glite.security.voms.admin.util.validation.x509.DnValidationResult.ValidationError;
import org.glite.security.voms.admin.util.validation.x509.DnValidator;
import org.junit.Assert;
import org.junit.Test;

public class DnValidationTest {

  public static final String TRUST_ANCHORS_DIR = "src/test/resources/trust-anchors";
  public static final String IGI_TEST_CA_SUBJECT = "/C=IT/O=IGI/CN=Test CA";
  public static final String UNKNOWN_CA_SUBJECT = "/C=ZY/O=Whatever/CN=Unknown CA";

  private DnValidator validator = new CanlDNValidator(TRUST_ANCHORS_DIR, true);

  @Test
  public void testSimpleDnValidationError() {

    DnValidationResult result = validator.validate(IGI_TEST_CA_SUBJECT,
      "/CN=Ciccio");

    Assert.assertFalse(result.isValid());
    Assert.assertEquals(ValidationError.NO_MATCHING_POLICY, result.getError());

  }

  @Test
  public void testSimpleDnValidationSuccess() {

    DnValidationResult result = validator.validate(IGI_TEST_CA_SUBJECT,
      "/C=IT/O=IGI/CN=Whatever");
    Assert.assertTrue(result.isValid());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyStringValidationError() {

    validator.validate(IGI_TEST_CA_SUBJECT, "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullStringValidationError() {

    validator.validate(IGI_TEST_CA_SUBJECT, null);
  }

  @Test
  public void testUnkownCaValidationError() {

    DnValidationResult result = validator.validate(UNKNOWN_CA_SUBJECT,
      "/CN=Ciccio");

    Assert.assertEquals(ValidationError.NAMESPACE_NOT_FOUND, result.getError());

  }

}
