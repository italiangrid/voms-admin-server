package it.infn.cnaf.voms.saml.emi;

import org.opensaml.saml2.core.Attribute;

public interface EMISAMLProfileConstants {
	
	public static final String ATTRIBUTE_NAME_FORMAT = Attribute.URI_REFERENCE;
	
	public static final String PROFILE_ID = "http://dci-sec.org/saml/profile/virtual-organization/1.0";
	
	public static final String SAML_ATTRIBUTE_PREFIX = "http://dci-sec.org/saml/attribute/";
	
	public static final String VO_ATTRIBUTE_NAME = SAML_ATTRIBUTE_PREFIX + "virtual-organization";
	
	public static final String GROUP_ATTRIBUTE_NAME = SAML_ATTRIBUTE_PREFIX + "group";
	
	public static final String PRIMARY_GROUP_ATTRIBUTE_NAME = SAML_ATTRIBUTE_PREFIX + "group/primary";
	
	public static final String ROLE_ATTRIBUTE_NAME = SAML_ATTRIBUTE_PREFIX + "role";
	
	public static final String PRIMARY_ROLE_ATTRIBUTE_NAME = SAML_ATTRIBUTE_PREFIX + "role/primary";
	
	public static final String DCI_SEC_NS = "http://dci-sec.org/saml/profile/virtual-organization/1.0";
	public static final String DCI_SEC_PREFIX = "dci-sec";
	
	public static final String DCI_SEC_GROUP = "group";
	public static final String DCI_SEC_ROLE = "role";
	public static final String DCI_SEC_VO = "vo";
	public static final String DCI_SEC_SCOPE = "scope";
	
	
	
}
