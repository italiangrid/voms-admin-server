package org.glite.security.voms.admin.util.validation.x509;

import javax.security.auth.x500.X500Principal;

import eu.emi.security.authn.x509.helpers.ns.NamespacePolicy;
import eu.emi.security.authn.x509.helpers.ns.OpensslNamespacePolicyImpl;

public final class DnValidationResult {

  public enum ValidationError {
    NAMESPACE_NOT_FOUND("No namespace found for CA"),
    NO_MATCHING_POLICY("Subject is not compliant with CA namespace policies");

    final String message;

    private ValidationError(String msg) {
      message = msg;
    }

    public String getMessage() {

      return message;
    }

  }

  private final X500Principal ca;
  private final X500Principal dn;

  private final boolean valid;

  private final NamespacePolicy policy;
  private final String policyRegexp;

  private final ValidationError error;

  private DnValidationResult(Builder b) {

    this.ca = b.ca;
    this.dn = b.dn;
    this.valid = b.valid;
    this.policy = b.policy;
    this.error = b.error;
    this.policyRegexp = b.policyRegexp;

  }

  public X500Principal getCa() {

    return ca;
  }

  public X500Principal getDn() {

    return dn;
  }

  public boolean isValid() {

    return valid;
  }

  public NamespacePolicy getPolicy() {

    return policy;
  }

  public String getPolicyRegexp() {

    return policyRegexp;
  }

  public ValidationError getError() {

    return error;
  }

  public static class Builder {

    private X500Principal ca;
    private X500Principal dn;
    private boolean valid = false;
    private NamespacePolicy policy;
    private ValidationError error;
    private String policyRegexp = null;

    public Builder ca(X500Principal ca) {

      this.ca = ca;
      return this;
    }

    public Builder dn(X500Principal dn) {

      this.dn = dn;
      return this;
    }

    public Builder valid(boolean valid) {

      this.valid = valid;
      return this;
    }

    public Builder policy(NamespacePolicy policy) {

      this.policy = policy;
      this.valid = policy.isPermit();
      // This is really ugly, but hopefully will be fixed in CANL someday
      this.policyRegexp = ((OpensslNamespacePolicyImpl) policy).getSuject();
      return this;
    }

    public Builder error(ValidationError error) {

      this.error = error;
      return this;
    }

    public DnValidationResult build() {

      return new DnValidationResult(this);
    }

  }

  public static final Builder build() {

    return new Builder();
  }

  @Override
  public String toString() {

    return "DnValidationResult [ca=" + ca + ", dn=" + dn + ", valid=" + valid
      + ", policy=" + policy + ", error=" + error + "]";
  }

  public String errorMessage() {

    if (error != null) {
      return error.getMessage();
    }

    if (!valid) {
      if (policy != null) {
        return String.format(
          "Subject matches with DENY namespace policy with regexp '%s'",
          policyRegexp);
      } else {
        return ValidationError.NO_MATCHING_POLICY.getMessage();
      }
    }

    // Unreachable
    throw new IllegalStateException("Unknown error");
  }
}
