/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package org.glite.security.voms.admin.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.glite.security.voms.VOMSException;
import org.glite.security.voms.service.admin.VOMSAdminServiceLocator;

public class CSRFAxisTestClient {

  private static final String HOST = "wilco.cnaf.infn.it";
  private static final String VO = "mysql";

  public CSRFAxisTestClient() throws VOMSException, RemoteException,
    ServiceException, MalformedURLException {

    String url = String.format("https://%s:8443/voms/%s/services/VOMSAdmin",
      HOST, VO);

    VOMSAdminServiceLocator loc = new VOMSAdminServiceLocator();

    loc.getVOMSAdmin(new URL(url)).listRoles();
  }

  /**
   * @param args
   * @throws ServiceException
   * @throws RemoteException
   * @throws VOMSException
   * @throws MalformedURLException
   */
  public static void main(String[] args) throws VOMSException, RemoteException,
    ServiceException, MalformedURLException {

    new CSRFAxisTestClient();
  }

}
