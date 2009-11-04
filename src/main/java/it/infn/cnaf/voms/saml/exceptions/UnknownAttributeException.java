package it.infn.cnaf.voms.saml.exceptions;


public class UnknownAttributeException extends Exception
{
  public UnknownAttributeException(String str)
  {
    super("The attribute with name " + str + " is unknown.");
  }
}
