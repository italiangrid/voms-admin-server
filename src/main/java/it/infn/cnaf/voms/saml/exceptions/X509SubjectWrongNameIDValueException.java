package it.infn.cnaf.voms.saml.exceptions;


public class X509SubjectWrongNameIDValueException extends Exception
{
  public X509SubjectWrongNameIDValueException(String dn)
  {
    super("Wrong NameID value " + dn + ", distinguished name must be encoded according to RFC 2253.");
  }
}
