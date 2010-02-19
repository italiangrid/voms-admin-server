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

package it.infn.cnaf.voms.saml;

import it.infn.cnaf.voms.aa.VOMSAttributes;
import it.infn.cnaf.voms.aa.VOMSFQAN;
import it.infn.cnaf.voms.aa.VOMSGenericAttribute;

import java.util.ArrayList;
import java.util.List;

import org.opensaml.Configuration;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.impl.AttributeBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSStringBuilder;

/**
 * 
 *  @author Andrea Ceccanti
 *  @author Valerio Venturi
 *
 */
public class SAMLAttributeSerializer {
    
    public static final String ATTRIBUTE_NAME_FORMAT = Attribute.URI_REFERENCE;
    
    public static final String FQAN_ATTRIBUTE_NAME = "http://voms.forge.cnaf.infn.it/fqan";
    
    public static final String AUTHZ_INTEROP_FQAN_ATTRIBUTE_NAME = "http://authz-interop.org/xacml/subject/voms-fqan";
    
     
    static Attribute serializeFQAN(List<VOMSFQAN> fqans){
    
        XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
        
        AttributeBuilder attributeBuilder = 
            (AttributeBuilder) builderFactory.getBuilder(Attribute.DEFAULT_ELEMENT_NAME);
        
        Attribute fqansAttribute = attributeBuilder.buildObject();
        
        fqansAttribute.setName(AUTHZ_INTEROP_FQAN_ATTRIBUTE_NAME);
        fqansAttribute.setNameFormat( ATTRIBUTE_NAME_FORMAT );
        
        XSStringBuilder attributeValueBuilder = 
          (XSStringBuilder) builderFactory.getBuilder(XSString.TYPE_NAME);
        
        for (VOMSFQAN fqan: fqans){
          
            XSString attributeValue = attributeValueBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
            attributeValue.setValue( fqan.getFQAN() );
            fqansAttribute.getAttributeValues().add( attributeValue );
        }
        
        return fqansAttribute;
        
    }
    
    static Attribute serializeGenericAttribute(VOMSGenericAttribute genericAttribute){   
        XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
        
        AttributeBuilder attributeBuilder = 
            (AttributeBuilder) builderFactory.getBuilder(Attribute.DEFAULT_ELEMENT_NAME);
        
        Attribute gaAttribute = attributeBuilder.buildObject();
        
        // FIXME: find out a non-naive format for generic attributes names
        gaAttribute.setName( genericAttribute.getName() );
        gaAttribute.setNameFormat( ATTRIBUTE_NAME_FORMAT );
        
        XSStringBuilder attributeValueBuilder = 
            (XSStringBuilder) builderFactory.getBuilder(XSString.TYPE_NAME);
        
        XSString attributeValue = attributeValueBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
        
        // FIXME: gracefully forgot the context...
        attributeValue.setValue( genericAttribute.getValue() );
        gaAttribute.getAttributeValues().add( attributeValue );
     
        return gaAttribute;
        
    }
    
    public static List<Attribute> serializeAttributes(VOMSAttributes attributes){
        assert attributes != null: "Cannot serialize a NULL attribute!";
        
        List<Attribute> vomsSAMLAttributes = new ArrayList <Attribute>();
        
        // Serialize FQANs
        vomsSAMLAttributes.add(serializeFQAN( attributes.getFqans() ));
        
        // Serialized Generic Attributes
        for (VOMSGenericAttribute ga: attributes.getGenericAttributes())
            vomsSAMLAttributes.add( serializeGenericAttribute( ga ) );
        
        return vomsSAMLAttributes;   
    }
}
