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
