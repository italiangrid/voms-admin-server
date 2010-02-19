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

/**
 * AttributeAuthoritySoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.infn.cnaf.voms.saml.axis_skeletons;

public class AttributeAuthoritySoapBindingSkeleton implements it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthorityPortType, org.apache.axis.wsdl.Skeleton {
    private it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthorityPortType impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol", "AttributeQuery"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol", "AttributeQueryType"), org.opensaml.saml2.core.AttributeQuery.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("attributeQuery", _params, new javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol", "Response"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol", "ResponseType"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "attributeQuery"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("attributeQuery") == null) {
            _myOperations.put("attributeQuery", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("attributeQuery")).add(_oper);
    }

    public AttributeAuthoritySoapBindingSkeleton() {
        this.impl = new it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthoritySoapBindingImpl();
    }

    public AttributeAuthoritySoapBindingSkeleton(it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthorityPortType impl) {
        this.impl = impl;
    }
    public org.opensaml.saml2.core.Response attributeQuery(org.opensaml.saml2.core.AttributeQuery body) throws java.rmi.RemoteException
    {
        org.opensaml.saml2.core.Response ret = impl.attributeQuery(body);
        return ret;
    }

}
