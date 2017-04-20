/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.glite.security.voms.admin.util.migration;

import static java.util.stream.Collectors.toSet;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.xml.rpc.ServiceException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONPopulator;
import org.apache.struts2.json.JSONReader;
import org.apache.struts2.json.JSONWriter;
import org.glite.security.voms.VOMSException;
import org.glite.security.voms.admin.apiv2.CertificateJSON;
import org.glite.security.voms.admin.apiv2.ListUserResultJSON;
import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
import org.glite.security.voms.admin.error.IllegalStateException;
import org.glite.security.voms.admin.service.CSRFGuardHandler;
import org.glite.security.voms.service.admin.VOMSAdmin;
import org.glite.security.voms.service.admin.VOMSAdminServiceLocator;
import org.glite.security.voms.service.attributes.AttributeClass;
import org.glite.security.voms.service.attributes.VOMSAttributes;
import org.glite.security.voms.service.attributes.VOMSAttributesServiceLocator;

public class MigrateVo implements MigrateVoConstants, Runnable {
  String originServer;
  String destinationServer;

  String originVo;
  String destinationVo;

  String proxy;

  VOMSAttributes originAttributeService, destinationAttributeService;
  VOMSAdmin originAdminService, destinationAdminService;

  List<String> originGroups = new ArrayList<>();
  List<String> originRoles = new ArrayList<>();

  HttpClient client = new HttpClient();

  Protocol vomsHttps;

  JSONReader jsonReader = new JSONReader();
  JSONWriter jsonWriter = new JSONWriter();

  JSONPopulator jsonPopulator = new JSONPopulator();

  protected void parseEnvironment() {
    originServer = System.getenv(ORIGIN_SERVER_ENV);
    destinationServer = System.getenv(DESTINATION_SERVER_ENV);
    originVo = System.getenv(ORIGIN_VO_ENV);
    destinationVo = System.getenv(DESTINATION_VO_ENV);
    proxy = System.getenv(X509_USER_PROXY_ENV);

  }

  protected void initHttpClient() throws Exception {
    vomsHttps = new Protocol("https", (ProtocolSocketFactory) new HttpClientSocketFactory(), 8443);
  }

  protected void initAxis() {
    System.setProperty(AXIS_SOCKET_FACTORY_PROPERTY, CANLAxisSocketFactory.class.getName());
  }

  protected VOMSAdmin getAdminService(String url) throws MalformedURLException, ServiceException {
    VOMSAdminServiceLocator locator = new VOMSAdminServiceLocator();
    return locator.getVOMSAdmin(new URL(url));
  }

  protected VOMSAttributes getAttributesService(String url)
      throws MalformedURLException, ServiceException {
    VOMSAttributesServiceLocator locator = new VOMSAttributesServiceLocator();
    return locator.getVOMSAttributes(new URL(url));
  }

  protected String buildVOMSAttributesUrl(String host, String vo) {
    return String.format("https://%s:8443/voms/%s/services/VOMSAttributes", host, vo);
  }

  protected String buildVOMSAdminUrl(String host, String vo) {
    return String.format("https://%s:8443/voms/%s/services/VOMSAdmin", host, vo);
  }

  protected String buildApiv2Url(String host, String vo, String action) {
    return String.format("https://%s:8443/voms/%s/apiv2/%s.action", host, vo, action);
  }

  public static void main(String[] args) {
    new MigrateVo().run();
  }


  protected void allGroups(VOMSAdmin adminService, String rootGroup, Set<String> groups)
      throws VOMSException, RemoteException {

    String[] childrenGroups = adminService.listSubGroups(rootGroup);

    if (childrenGroups == null) {
      return;
    }

    if (childrenGroups.length == 0) {
      return;
    }

    for (String g : childrenGroups) {
      groups.add(g);
      allGroups(adminService, g, groups);
    }
  }


  protected Set<String> originGroups() throws VOMSException, RemoteException {
    Set<String> originGroups = new TreeSet<>();
    allGroups(originAdminService, String.format("/%s", originVo), originGroups);
    return originGroups;
  }

  protected Set<String> destinationGroups() throws VOMSException, RemoteException {
    Set<String> groups = new TreeSet<>();
    allGroups(destinationAdminService, String.format("/%s", destinationVo), groups);
    return groups;
  }


  protected void migrateAUPs() throws Exception {

    client.getHostConfiguration().setHost(originServer, 8443, vomsHttps);

    GetMethod getMethod =
        new GetMethod(String.format("/voms/%s/apiv2/aup-versions.action", originVo));

    PostMethod postMethod =
        new PostMethod(String.format("/voms/%s/apiv2/import-aup-versions.action", destinationVo));


    try {
      client.executeMethod(getMethod);
      if (getMethod.getStatusCode() != 200) {
        throw new RuntimeException("Error listing aup versions: " + getMethod.getStatusText());
      }

      client.getHostConfiguration().setHost(destinationServer, 8443, vomsHttps);

      postMethod.addRequestHeader(CSRFGuardHandler.CSRF_GUARD_HEADER_NAME, "");

      StringRequestEntity requestEntity =
          new StringRequestEntity(getMethod.getResponseBodyAsString(), "application/json", "UTF-8");
      postMethod.setRequestEntity(requestEntity);

      client.executeMethod(postMethod);

      if (postMethod.getStatusCode() != 200) {
        throw new RuntimeException("Error importing aup versions: " + postMethod.getStatusText());
      }

    } finally {
      getMethod.releaseConnection();
    }

  }

  protected void migrateGroups() throws VOMSException, RemoteException {

    Set<String> destinationGroups = destinationGroups();
    Set<String> originGroupsTranslated = originGroups().stream()
      .map(s -> s.replaceFirst(originVo, destinationVo))
      .collect(Collectors.toSet());

    if (destinationGroups.containsAll(originGroupsTranslated)) {
      System.out.println("All groups already migrated");
    } else {
      originGroupsTranslated.removeIf(g -> destinationGroups.contains(g));
      for (String g : originGroupsTranslated) {
        System.out.println("Creating group " + g);
        destinationAdminService.createGroup(null, g);
      }
    }
  }

  protected void migrateRoles() throws VOMSException, RemoteException {
    Set<String> destinationRoles =
        new TreeSet<>(Arrays.asList(destinationAdminService.listRoles()));
    Set<String> originRoles = new TreeSet<>(Arrays.asList(originAdminService.listRoles()));

    if (destinationRoles.containsAll(originRoles)) {
      System.out.println("All roles already migrated");
    } else {
      originRoles.removeIf(r -> destinationRoles.contains(r));
      for (String r : originRoles) {
        System.out.println("Creating role " + r);
        destinationAdminService.createRole(r);
      }
    }
  }

  protected void migrateAttributeClasses() throws VOMSException, RemoteException {

    AttributeClass[] originClasses = originAttributeService.listAttributeClasses();

    AttributeClass[] destinationClasses = destinationAttributeService.listAttributeClasses();
    Set<String> dcn = Collections.emptySet();
    if (destinationClasses != null) {
      dcn = Arrays.asList(destinationClasses).stream().map(a -> a.getName()).collect(toSet());
    }

    for (AttributeClass ac : originClasses) {
      if (!dcn.contains(ac.getName())) {
        System.out.println("Creating GA class: " + ac.getName());
        destinationAttributeService.createAttributeClass(ac.getName(), ac.getDescription(),
            ac.isUniquenessChecked());
      }
    }
  }

  @Override
  public void run() {
    try {
      parseEnvironment();
      initAxis();
      initHttpClient();
      originAdminService = getAdminService(buildVOMSAdminUrl(originServer, originVo));
      destinationAdminService =
          getAdminService(buildVOMSAdminUrl(destinationServer, destinationVo));

      originAttributeService = getAttributesService(buildVOMSAttributesUrl(originServer, originVo));
      destinationAttributeService =
          getAttributesService(buildVOMSAttributesUrl(destinationServer, destinationVo));

      migrateAUPs();
      migrateGroups();
      migrateRoles();
      migrateAttributeClasses();
      migrateUsers();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  private void migrateUser(VOMSUserJSON user) throws HttpException, IOException, JSONException {

    client.getHostConfiguration().setHost(destinationServer, 8443, vomsHttps);

    GetMethod getMethod =
        new GetMethod(String.format("/voms/%s/apiv2/user-info.action", destinationVo));

    PostMethod postMethod =
        new PostMethod(String.format("/voms/%s/apiv2/import-user.action", destinationVo));

    // Check if user exists
    CertificateJSON firstCert = user.getCertificates().stream().findFirst().orElseThrow(
        () -> new IllegalStateException("User without certificate cannot be migrated"));

    NameValuePair dnParam = new NameValuePair("dn", firstCert.getSubjectString());
    NameValuePair caParam = new NameValuePair("ca", firstCert.getIssuerString());

    getMethod.setQueryString(new NameValuePair[] {dnParam, caParam});

    try {
      client.executeMethod(getMethod);

      int statusCode = getMethod.getStatusCode();
      
      if (statusCode == 200) {
        System.err.format("User %s %s (%d) already migrated. Skipping it...\n", user.getName(),
            user.getSurname(), user.getId());
        return;
      }
      
      if (statusCode != 404) {
        System.err.format("Error querying user %s %s (%d): %s \n", user.getName(),
            user.getSurname(), user.getId(), getMethod.getStatusText());
        return;
      }
      // Fix fqan names
      List<String> fixedFqans = user.getFqans()
        .stream()
        .map(s -> s.replaceFirst(originVo, destinationVo))
        .collect(Collectors.toList());
      
      user.setFqans(fixedFqans);

      String jsonString = jsonWriter.write(user);

      StringRequestEntity requestEntity =
          new StringRequestEntity(jsonString, "application/json", "UTF-8");
      postMethod.setRequestEntity(requestEntity);

      client.executeMethod(postMethod);

      if (postMethod.getStatusCode() != 200) {
        throw new RuntimeException("Error importing user: " + postMethod.getStatusText());
      } else {
        System.out.format("User %s, %s (%d) migrated.\n", user.getName(), user.getSurname(), user.getId());
      }

    } finally {
      getMethod.releaseConnection();
    }
  }

  private List<VOMSUserJSON> getUsersFromOriginVo() throws HttpException, IOException,
      JSONException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,
      IllegalArgumentException, InstantiationException, IntrospectionException {

    client.getHostConfiguration().setHost(originServer, 8443, vomsHttps);

    GetMethod getMethod = new GetMethod(String.format("/voms/%s/apiv2/users.action", originVo));

    NameValuePair pageSizeParam = new NameValuePair("pageSize", "10000");
    getMethod.setQueryString(new NameValuePair[] {pageSizeParam});

    try {
      client.executeMethod(getMethod);

      if (getMethod.getStatusCode() != 200) {
        throw new RuntimeException("Error listing aup versions: " + getMethod.getStatusText());
      }

      ListUserResultJSON result = new ListUserResultJSON();
      Object json = jsonReader.read(getMethod.getResponseBodyAsString());

      jsonPopulator.populateObject(result, (Map) json);

      for (VOMSUserJSON u : result.getResult()) {
        migrateUser(u);
      }

    } finally {
      getMethod.releaseConnection();
    }

    return null;
  }

  private void migrateUsers() throws HttpException, IOException, IllegalAccessException,
      InvocationTargetException, NoSuchMethodException, IllegalArgumentException,
      InstantiationException, JSONException, IntrospectionException {

    getUsersFromOriginVo();

  }
}
