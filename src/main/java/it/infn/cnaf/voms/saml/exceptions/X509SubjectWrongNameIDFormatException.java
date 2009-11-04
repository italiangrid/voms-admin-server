package it.infn.cnaf.voms.saml.exceptions;

import org.opensaml.saml2.core.NameID;

public class X509SubjectWrongNameIDFormatException extends Exception
{
  public X509SubjectWrongNameIDFormatException(String format)
  {
    super("Wrong Format, must match urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName but was " + format);
  }
}
