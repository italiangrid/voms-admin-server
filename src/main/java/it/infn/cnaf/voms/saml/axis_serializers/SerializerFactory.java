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

package it.infn.cnaf.voms.saml.axis_serializers;

import java.util.Iterator;

import javax.xml.rpc.encoding.Serializer;

/**
 * @author Valerio Venturi (valerio.venturi@cnaf.infn.it)
 *
 */
public class SerializerFactory implements
		org.apache.axis.encoding.SerializerFactory {

	/* (non-Javadoc)
	 * @see javax.xml.rpc.encoding.SerializerFactory#getSerializerAs(java.lang.String)
	 */
	public Serializer getSerializerAs(String arg0) {
		return new it.infn.cnaf.voms.saml.axis_serializers.Serializer();
	}

	/* (non-Javadoc)
	 * @see javax.xml.rpc.encoding.SerializerFactory#getSupportedMechanismTypes()
	 */
	public Iterator getSupportedMechanismTypes() {
		// TODO Auto-generated method stub
		return null;
	}

}
