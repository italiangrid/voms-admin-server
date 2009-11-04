/**************************************************************************

 Copyright 2006-2007 Istituto Nazionale di Fisica Nucleare (INFN)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 File : UnknownPrincipalException.java

 Authors: Valerio Venturi <valerio.venturi@cnaf.infn.it>
 
**************************************************************************/

package it.infn.cnaf.voms.saml.exceptions;



/**
 * @author Valerio Venturi <valerio.venturi@cnaf.infn.it>
 *
 */
public class UnknownPrincipalException extends VOMSServiceException
{
 
  public UnknownPrincipalException(String principal)
  {
    super("The principal " + principal + " is unknown.");
  }
   
}
