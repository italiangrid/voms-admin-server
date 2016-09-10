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
