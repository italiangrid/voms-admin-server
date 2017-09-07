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
import java.util.concurrent.TimeUnit;
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
import org.apache.commons.lang3.StringUtils;
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
import org.glite.security.voms.admin.util.PathNamingScheme;
import org.glite.security.voms.service.acl.VOMSACL;
import org.glite.security.voms.service.acl.VOMSACLServiceLocator;
import org.glite.security.voms.service.admin.VOMSAdmin;
import org.glite.security.voms.service.admin.VOMSAdminServiceLocator;
import org.glite.security.voms.service.attributes.AttributeClass;
import org.glite.security.voms.service.attributes.VOMSAttributes;
import org.glite.security.voms.service.attributes.VOMSAttributesServiceLocator;

public class MigrateVo implements MigrateVoConstants, Runnable {

  public static final String USAGE = "Usage: voms-migrate-vo-util\n" + "\n"
      + "This command requires you to set the following environment variables:\n" + "\n"
      + "  VA_MIGRATE_ORIGIN_SERVER: the fully qualified host name of the VOMS Admin origin server\n"
      + "  VA_MIGRATE_ORIGIN_VO: the name of the VO that must be migrated\n"
      + "  VA_MIGRATE_DESTINATION_SERVER: the fully qualified host name of the VOMS Admin destionation server\n"
      + "  X509_USER_PROXY: a proxy certificate that has admin rights on the origin and destination vo";
  
  String originServer;
  String destinationServer;

  String originVo;
  String destinationVo;

  String proxy;

  VOMSAttributes originAttributeService, destinationAttributeService;
  VOMSAdmin originAdminService, destinationAdminService;
  VOMSACL originAclService, destinationAclService;

  List<String> originGroups = new ArrayList<>();
  List<String> originRoles = new ArrayList<>();

  HttpClient client = new HttpClient();

  Protocol vomsHttps;

  JSONReader jsonReader = new JSONReader();
  JSONWriter jsonWriter = new JSONWriter();

  JSONPopulator jsonPopulator = new JSONPopulator();

  protected void usage() {

    System.err.println(USAGE);
    System.exit(1);

  }

  protected void printInfo(){
    
  }
  
  protected void parseEnvironment() {

    String[] mandatoryEnvVars =
        {ORIGIN_SERVER_ENV, ORIGIN_VO_ENV, X509_USER_PROXY_ENV, DESTINATION_SERVER_ENV};

    for (String v : mandatoryEnvVars) {
      if (System.getenv(v) == null) {
        System.err.format("Please set the %s environment variable\n", v);
        usage();
      }
    }

    originServer = System.getenv(ORIGIN_SERVER_ENV);
    destinationServer = System.getenv(DESTINATION_SERVER_ENV);
    originVo = System.getenv(ORIGIN_VO_ENV);
    destinationVo = System.getenv(DESTINATION_VO_ENV);


    if (destinationVo == null) {
      destinationVo = originVo;
    }

    proxy = System.getenv(X509_USER_PROXY_ENV);

    if (originServer.equals(destinationServer) && originVo.equals(destinationVo)) {
      System.err.format(
          "I will not migrate a VO over itself. Please provide sensible values for %s, %s, %s, %s",
          ORIGIN_SERVER_ENV, DESTINATION_SERVER_ENV, ORIGIN_VO_ENV, DESTINATION_VO_ENV);
      System.exit(1);
    }

  }

  protected void initHttpClient() throws Exception {
    vomsHttps = new Protocol("https", (ProtocolSocketFactory) new HttpClientSocketFactory(), 8443);
  }

  protected void initAxis() {
    System.setProperty(AXIS_SOCKET_FACTORY_PROPERTY, CANLAxisSocketFactory.class.getName());
  }

  protected VOMSACL getACLService(String url) throws MalformedURLException, ServiceException {
    VOMSACLServiceLocator locator = new CSRFVOMSACLServiceLocator();
    return locator.getVOMSACL(new URL(url));
  }
  
  protected VOMSAdmin getAdminService(String url) throws MalformedURLException, ServiceException {
    VOMSAdminServiceLocator locator = new CSRFVOMSAdminServiceLocator();
    return locator.getVOMSAdmin(new URL(url));
  }

  protected VOMSAttributes getAttributesService(String url)
      throws MalformedURLException, ServiceException {
    VOMSAttributesServiceLocator locator = new CSRFVOMSAttributesServiceLocator();
    return locator.getVOMSAttributes(new URL(url));
  }
  
  protected String buildVOMSServiceUrl(String service, String host, String vo){
    return String.format("https://%s:8443/voms/%s/services/%s", host, vo, service);
  }

  protected String buildVOMSAclUrl(String host, String vo) {
    return buildVOMSServiceUrl("VOMSACL", host, vo);
  }
  protected String buildVOMSAttributesUrl(String host, String vo) {
    return buildVOMSServiceUrl("VOMSAttributes", host, vo);
  }

  protected String buildVOMSAdminUrl(String host, String vo) {
    return buildVOMSServiceUrl("VOMSAdmin", host, vo);
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

    System.out.println("Migrating AUPs");
    client.getHostConfiguration().setHost(originServer, 8443, vomsHttps);

    GetMethod getMethod =
        new GetMethod(String.format("/voms/%s/apiv2/aup-versions.action", originVo));

    getMethod.addRequestHeader(CSRFGuardHandler.CSRF_GUARD_HEADER_NAME, "");

    PostMethod postMethod =
        new PostMethod(String.format("/voms/%s/apiv2/import-aup-versions.action", destinationVo));

    postMethod.addRequestHeader(CSRFGuardHandler.CSRF_GUARD_HEADER_NAME, "");

    try {
      client.executeMethod(getMethod);
      if (getMethod.getStatusCode() != 200) {
        throw new RuntimeException("Error listing aup versions: " + getMethod.getStatusText());
      }

      client.getHostConfiguration().setHost(destinationServer, 8443, vomsHttps);

      StringRequestEntity requestEntity =
          new StringRequestEntity(getMethod.getResponseBodyAsString(), "application/json", "UTF-8");
      postMethod.setRequestEntity(requestEntity);

      client.executeMethod(postMethod);

      if (postMethod.getStatusCode() != 200) {
        throw new RuntimeException("Error importing aup versions: " + postMethod.getStatusText());
      }
    } catch (Exception e) {
      throw new RuntimeException("Error migrating AUP versions: "+e.getMessage(), e);
    } finally {
      getMethod.releaseConnection();
      postMethod.releaseConnection();
    }

  }

  private int groupNameComparator(String g1, String g2){
    Integer g1Slashes = StringUtils.countMatches(g1, "/");
    Integer g2Slashes = StringUtils.countMatches(g2, "/");
    
    if (g1Slashes == g2Slashes){
      return g1.compareTo(g2);
    } else {
      return g1Slashes.compareTo(g2Slashes);
    }
  }
  protected void migrateGroups() throws VOMSException, RemoteException {

    System.out.println("Migrating groups");
    Set<String> destinationGroups = destinationGroups();
    List<String> originGroupsTranslated = originGroups().stream()
      .map(s -> s.replaceFirst(originVo, destinationVo))
      .sorted(this::groupNameComparator)
      .distinct()
      .collect(Collectors.toList());

    if (destinationGroups.containsAll(originGroupsTranslated)) {
      System.out.println("All groups already migrated");
    } else {
      originGroupsTranslated.removeIf(g -> destinationGroups.contains(g));
      for (String g : originGroupsTranslated) {
        System.out.println("Creating group " + g);
        String parentGroupName = PathNamingScheme.getParentGroupName(g);
        destinationAdminService.createGroup(parentGroupName, g);
      }
    }
    
  }

  protected void migrateRoles() throws VOMSException, RemoteException {
    System.out.println("Migrating roles");
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

    System.out.println("Migrating Generic Attribute classes");
    AttributeClass[] originClasses = originAttributeService.listAttributeClasses();

    AttributeClass[] destinationClasses = destinationAttributeService.listAttributeClasses();
    Set<String> dcn = Collections.emptySet();
    if (destinationClasses != null) {
      dcn = Arrays.asList(destinationClasses).stream().map(a -> a.getName()).collect(toSet());
    }

    if (originClasses == null){
      System.out.println("No attribute classes defined in origin server.");
      return;
    }
    
    for (AttributeClass ac : originClasses) {
      if (!dcn.contains(ac.getName())) {
        System.out.println("Creating GA class: " + ac.getName());
        String description = ac.getDescription() == null ? "" : ac.getDescription();
        destinationAttributeService.createAttributeClass(ac.getName(), description,
            ac.isUniquenessChecked());
      }
    }
  }
  
  protected void migrateACLs(){
    
  }

  @Override
  public void run() {
    try {
      parseEnvironment();
      System.out.println("VOMS VO migration tool");
      System.out.format("Migrating VO %s from %s to %s\n",
          originVo, originServer, destinationServer);
      
      initAxis();
      initHttpClient();
      
      originAdminService = getAdminService(buildVOMSAdminUrl(originServer, originVo));
      destinationAdminService =
          getAdminService(buildVOMSAdminUrl(destinationServer, destinationVo));
      
      originAclService = getACLService(buildVOMSAclUrl(originServer, originVo));
      destinationAclService = getACLService(buildVOMSAclUrl(destinationServer, destinationVo));
      
      originAttributeService = getAttributesService(buildVOMSAttributesUrl(originServer, originVo));
      destinationAttributeService =
          getAttributesService(buildVOMSAttributesUrl(destinationServer, destinationVo));

      long startTime = System.currentTimeMillis();
      migrateAUPs();
      migrateGroups();
      migrateRoles();
      migrateACLs();
      migrateAttributeClasses();
      migrateUsers();
      long endTime = System.currentTimeMillis();
      System.out.format("Migration completed in %d seconds.\n", 
          TimeUnit.MILLISECONDS.toSeconds(endTime-startTime));

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void migrateUser(VOMSUserJSON user, int current, int count) throws HttpException, IOException, JSONException {

    System.out.format("%d / %d  - ", current, count);
    client.getHostConfiguration().setHost(destinationServer, 8443, vomsHttps);

    GetMethod getMethod =
        new GetMethod(String.format("/voms/%s/apiv2/user-info.action", destinationVo));
    
    getMethod.addRequestHeader(CSRFGuardHandler.CSRF_GUARD_HEADER_NAME, "");

    PostMethod postMethod =
        new PostMethod(String.format("/voms/%s/apiv2/import-user.action", destinationVo));

    postMethod.addRequestHeader(CSRFGuardHandler.CSRF_GUARD_HEADER_NAME, "");

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
        System.out.format("User %s, %s (%d) migrated.\n", user.getName(), user.getSurname(),
            user.getId());
      }

    } finally {
      getMethod.releaseConnection();
    }
  }

  private void migrateUsers() throws HttpException, IOException, IllegalAccessException,
      InvocationTargetException, NoSuchMethodException, IllegalArgumentException,
      InstantiationException, JSONException, IntrospectionException {

    System.out.println("Migrating users");
    client.getHostConfiguration().setHost(originServer, 8443, vomsHttps);

    GetMethod getMethod = new GetMethod(String.format("/voms/%s/apiv2/users.action", originVo));
    getMethod.addRequestHeader(CSRFGuardHandler.CSRF_GUARD_HEADER_NAME, "");

    NameValuePair pageSizeParam = new NameValuePair("pageSize", "10000");
    getMethod.setQueryString(new NameValuePair[] {pageSizeParam});

    try {
      client.executeMethod(getMethod);

      if (getMethod.getStatusCode() != 200) {
        throw new RuntimeException("Error users: " + getMethod.getStatusText());
      }

      ListUserResultJSON result = new ListUserResultJSON();
      Object json = jsonReader.read(getMethod.getResponseBodyAsString());

      jsonPopulator.populateObject(result, (Map) json);
      
      System.out.println("Migrating "+result.getResult().size()+" users...");

      int count = 0;
      
      for (VOMSUserJSON u : result.getResult()) {
        
        migrateUser(u,++count,result.getResult().size());
        
      }

    } catch(Exception e){
      throw new RuntimeException("Error migrating users: "+e.getMessage(), e);
    } finally {
      getMethod.releaseConnection();
    }

  }
}
