/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
 */
package org.glite.security.voms.admin.util.migration;

import javax.xml.rpc.Call;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.MimeHeaders;

import org.apache.axis.Message;
import org.glite.security.voms.admin.service.CSRFGuardHandler;
import org.glite.security.voms.service.attributes.VOMSAttributesServiceLocator;

public class CSRFVOMSAttributesServiceLocator extends VOMSAttributesServiceLocator {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public Call createCall() throws ServiceException {

    _call = new org.apache.axis.client.Call(this) {

      @Override
      public void setRequestMessage(Message msg) {
        super.setRequestMessage(msg);

        MimeHeaders mimeHeaders = msg.getMimeHeaders();
        mimeHeaders.addHeader(CSRFGuardHandler.CSRF_GUARD_HEADER_NAME, "");
      }
    };

    return _call;
  }
}
