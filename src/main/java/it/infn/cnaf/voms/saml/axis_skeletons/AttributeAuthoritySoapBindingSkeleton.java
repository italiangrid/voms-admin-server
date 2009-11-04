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
