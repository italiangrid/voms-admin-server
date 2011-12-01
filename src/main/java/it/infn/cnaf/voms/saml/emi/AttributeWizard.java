/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

package it.infn.cnaf.voms.saml.emi;

import it.infn.cnaf.voms.aa.VOMSFQAN;

import java.util.List;

import javax.xml.namespace.QName;

import org.glite.security.voms.admin.util.PathNamingScheme;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.impl.AttributeBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSAnyBuilder;
import org.opensaml.xml.schema.impl.XSStringBuilder;

public class AttributeWizard {
	
	public static final QName GROUP_XSD_TYPE = new QName(EMISAMLProfileConstants.DCI_SEC_NS, 
			EMISAMLProfileConstants.DCI_SEC_GROUP, 
			EMISAMLProfileConstants.DCI_SEC_PREFIX);
	
	public static final QName ROLE_XSD_TYPE = new QName(EMISAMLProfileConstants.DCI_SEC_NS, 
			EMISAMLProfileConstants.DCI_SEC_ROLE, 
			EMISAMLProfileConstants.DCI_SEC_PREFIX);
	
	public static final QName VO_XSD_TYPE = new QName(EMISAMLProfileConstants.DCI_SEC_NS, 
			EMISAMLProfileConstants.DCI_SEC_VO, 
			EMISAMLProfileConstants.DCI_SEC_PREFIX);
	
	public static final QName SCOPE_XSD_ATTRIBUTE = new QName(EMISAMLProfileConstants.DCI_SEC_NS, 
			EMISAMLProfileConstants.DCI_SEC_SCOPE, 
			EMISAMLProfileConstants.DCI_SEC_PREFIX);	
	 
	protected static Attribute createAttribute(String attributeName){
		
		XMLObjectBuilderFactory bf = Configuration.getBuilderFactory();
        
        AttributeBuilder attributeBuilder = 
            (AttributeBuilder) bf.getBuilder(Attribute.DEFAULT_ELEMENT_NAME);
        
        Attribute attr = attributeBuilder.buildObject();
        attr.setName(attributeName);
        attr.setNameFormat(EMISAMLProfileConstants.ATTRIBUTE_NAME_FORMAT);
        
        return attr;
		
	}
	protected static XSAny createAttributeValue(QName type, String value){
		
		XMLObjectBuilderFactory bf = Configuration.getBuilderFactory();
		XSAnyBuilder builder = (XSAnyBuilder) bf.getBuilder(XSAny.TYPE_NAME);
        XSAny attrVal = builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME,type);
        attrVal.setTextContent(value);
        
		return attrVal;
	}

	protected static XSString createStringAttributeValue(String value){
		
		XMLObjectBuilderFactory bf = Configuration.getBuilderFactory();
		XSStringBuilder builder = (XSStringBuilder) bf.getBuilder(XSString.TYPE_NAME);
		
		XSString attrVal = builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		attrVal.setValue(value);
		
		return attrVal;
		
	}
	protected static XSAny createRoleAttributeValue(VOMSFQAN fqan){
		
		
		String role = PathNamingScheme.getRoleName(fqan.getFQAN());
		String group = PathNamingScheme.getGroupName(fqan.getFQAN());
		
		XSAny roleAttrVal = createAttributeValue(ROLE_XSD_TYPE, role);
		roleAttrVal.getUnknownAttributes().put(SCOPE_XSD_ATTRIBUTE, group);
		
		return roleAttrVal;
	}
	
	public static Attribute createGroupAttribute(List<VOMSFQAN> fqans){
	
		Attribute groupAttr = createAttribute(EMISAMLProfileConstants.GROUP_ATTRIBUTE_NAME);
		
		for (VOMSFQAN f: fqans){
			
			if (f.isGroup())
				groupAttr.getAttributeValues().add(createStringAttributeValue(f.getFQAN()));
			
		}
        
		return groupAttr;
		
	}
	
	public static Attribute createPrimaryGroupAttribute(VOMSFQAN fqan){
		
		Attribute pGroupAttr = createAttribute(EMISAMLProfileConstants.PRIMARY_GROUP_ATTRIBUTE_NAME);
		
		if (fqan.isGroup())
			pGroupAttr.getAttributeValues().add(createAttributeValue(GROUP_XSD_TYPE, fqan.getFQAN()));
		
		return pGroupAttr;
	}
	
	
	public static Attribute createRoleAttribute(List<VOMSFQAN> fqans){
		Attribute roleAttr = createAttribute(EMISAMLProfileConstants.ROLE_ATTRIBUTE_NAME);
		
		for (VOMSFQAN f: fqans){
			if (f.isRole())
				roleAttr.getAttributeValues().add(createRoleAttributeValue(f));
			
		}
		
		return roleAttr;
	}
	
	
	public static Attribute createPrimaryRoleAttribute(VOMSFQAN fqan){
		
		Attribute pRoleAttr = createAttribute(EMISAMLProfileConstants.PRIMARY_ROLE_ATTRIBUTE_NAME);
		
		if (fqan.isRole())
			pRoleAttr.getAttributeValues().add(createRoleAttributeValue(fqan));
		
		return pRoleAttr;
	}
	
	
	public static Attribute createVOAttribute(String voName){
		Attribute voAttr = createAttribute(EMISAMLProfileConstants.VO_ATTRIBUTE_NAME);
		
		voAttr.getAttributeValues().add(createStringAttributeValue(voName));
		
		return voAttr;
	}
}
