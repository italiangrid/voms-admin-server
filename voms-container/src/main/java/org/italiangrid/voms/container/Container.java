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
package org.italiangrid.voms.container;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.log.Log;
import org.italiangrid.utils.https.JettyRunThread;
import org.italiangrid.utils.https.SSLOptions;
import org.italiangrid.utils.https.ServerFactory;
import org.italiangrid.utils.https.impl.canl.CANLListener;
import org.italiangrid.voms.ac.VOMSACValidator;
import org.italiangrid.voms.ac.impl.DefaultVOMSValidator;
import org.italiangrid.voms.container.legacy.VOMSSslConnectorConfigurator;
import org.italiangrid.voms.container.lifecycle.ServerListener;
import org.italiangrid.voms.util.CertificateValidatorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import eu.emi.security.authn.x509.CrlCheckingMode;
import eu.emi.security.authn.x509.OCSPCheckingMode;
import eu.emi.security.authn.x509.X509CertChainValidatorExt;

public class Container {

  public static final Logger log = LoggerFactory.getLogger(Container.class);

  private static final String TAGLIBS_JAR_NAME = "org.apache.taglibs.standard.glassfish";

  public static final String CONF_FILE_NAME = "voms-admin-server.properties";

  public static final String DEFAULT_WAR = "/usr/share/webapps/voms-admin.war";

  public static final String DEFAULT_TMP_PREFIX = "/var/tmp";
  public static final String DEFAULT_DEPLOY_DIR = "/var/lib/voms-admin/vo.d";
  public static final String DEFAULT_WORK_DIR = "/var/lib/voms-admin/work";

  public static final String HTTP_CONNECTOR_NAME = "voms-http";
  public static final String HTTPS_CONNECTOR_NAME = "voms-https";

  public static final String HTTP_CONNECTOR_PORT = "8088";

  private static final String ARG_WAR = "war";
  private static final String ARG_CONFDIR = "confdir";
  private static final String ARG_DEPLOYDIR = "deploydir";

  private static final String SERVICE_PROPERTIES_FILE = "service.properties";
  private static final String SERVICE_PORT_KEY = "voms.aa.x509.additional_port";

  private Options cliOptions;
  private CommandLineParser parser = new GnuParser();

  private Properties serverConfiguration;

  private String war;
  private String confDir;
  private String deployDir;
  private String workDir;

  private String host;
  private String port;
  private String statusPort;

  private String certFile;
  private String keyFile;
  private String trustDir;
  private long trustDirRefreshIntervalInMsec;

  private String bindAddress;

  private String tlsExcludeProtocols;
  private String tlsIncludeProtocols;
  private String tlsExcludeCipherSuites;
  private String tlsIncludeCipherSuites;

  private Server server;
  private DeploymentManager deploymentManager;
  private HandlerCollection handlers = new HandlerCollection();
  private ContextHandlerCollection contexts = new ContextHandlerCollection();


  protected SSLOptions getSSLOptions() {

    SSLOptions options = new SSLOptions();

    options.setCertificateFile(certFile);
    options.setKeyFile(keyFile);
    options.setTrustStoreDirectory(trustDir);
    options.setTrustStoreRefreshIntervalInMsec(trustDirRefreshIntervalInMsec);

    options.setWantClientAuth(true);

    options.setNeedClientAuth(false);

    if (tlsExcludeCipherSuites != null){
      options.setExcludeCipherSuites(tlsExcludeCipherSuites.split(","));
    }

    if (tlsIncludeCipherSuites != null){
      options.setIncludeCipherSuites(tlsIncludeCipherSuites.split(","));
    }

    if (tlsExcludeProtocols != null){
      options.setExcludeProtocols(tlsExcludeProtocols.split(","));
    }

    if (tlsIncludeProtocols != null){
      options.setIncludeProtocols(tlsIncludeProtocols.split(","));
    }

    return options;

  }

  protected void confDirSanityChecks() {

    File confDirFile = new File(confDir);

    if (!confDirFile.exists() || !confDirFile.isDirectory()) {

      throw new IllegalArgumentException("VOMS Admin configuration directory "
        + "does not exists or is not a directory: "
        + confDirFile.getAbsolutePath());
    }
  }

  protected Properties getVOServiceProperties(String voName) {

    if (!getConfiguredVONames().contains(voName))
      throw new IllegalArgumentException("VO " + voName + " is "
        + "not configured on this host.");

    Properties voServiceProps = new Properties();

    String propsPath = String.format("%s/%s/%s", confDir, voName,
      SERVICE_PROPERTIES_FILE).replaceAll("/+", "/");

    try {
      voServiceProps.load(new FileInputStream(propsPath));
      return voServiceProps;

    } catch (IOException e) {
      log.error(
        "Error reading service properties configuration for VO {}: {}.",
        new Object[] { voName, e.getMessage() }, e);
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  protected Map<Integer, String> getConfiguredVOPortMappings() {

    Map<Integer, String> voPorts = new HashMap<Integer, String>();

    for (String vo : getConfiguredVONames()) {
      Properties sp = getVOServiceProperties(vo);
      if (sp.containsKey(SERVICE_PORT_KEY)) {
        int port = Integer.parseInt(sp.getProperty(SERVICE_PORT_KEY));
        if (port > 0)
          voPorts.put(port, vo);
      }
    }

    return voPorts;
  }

  protected List<String> getConfiguredVONames() {

    confDirSanityChecks();

    List<String> voNames = new ArrayList<String>();

    File confDirFile = new File(confDir);

    File[] voFiles = confDirFile.listFiles(new FileFilter() {

      @Override
      public boolean accept(File pathname) {

        return pathname.isDirectory();
      }
    });

    for (File f : voFiles) {
      voNames.add(f.getName());
    }

    return voNames;
  }

  protected void configureDeploymentManager() {

    contexts.setServer(server);

    deploymentManager = new DeploymentManager();

    VOMSAppProvider provider = new VOMSAppProvider();

    provider.setConfigurationDir(confDir);
    provider.setDeploymentDir(deployDir);
    provider.setHostname(host);
    provider.setPort(port);
    provider.setWarFile(war);
    provider.setWorkDir(workDir);

    deploymentManager.addAppProvider(provider);
    deploymentManager.setContexts(contexts);

  }

  protected void addNameToHTTPSConnector() {

    for (Connector c : server.getConnectors()) {
      if (c.getPort() == Integer.parseInt(port)) {
        SslSelectChannelConnector conn = (SslSelectChannelConnector) c;
        conn.setName(HTTPS_CONNECTOR_NAME);
      }
    }

  }

  protected void configureLocalHTTPConnector() {

    SelectChannelConnector conn = new SelectChannelConnector();
    conn.setHost("localhost");
    conn.setPort(Integer.parseInt(statusPort));
    conn.setName(HTTP_CONNECTOR_NAME);
    server.addConnector(conn);
  }

  protected void configureLegacyConnectors(String host,
    X509CertChainValidatorExt validator, SSLOptions options,
    int maxConnections, int maxRequestQueueSize) {

    VOMSSslConnectorConfigurator configurator = new VOMSSslConnectorConfigurator(
      validator);

    Map<Integer, String> voMappings = getConfiguredVOPortMappings();

    for (Map.Entry<Integer, String> e : voMappings.entrySet()) {
      int voPort = e.getKey();

      Connector c = configurator.configureConnector(host, voPort, options);

      ((SslSelectChannelConnector) c).setName("voms-" + e.getValue());

      server.addConnector(c);
      log.info("Configured VOMS legacy connector for VO {} on port {}.",
        e.getValue(), voPort);
    }

  }

  protected void configureJettyServer() {

    SSLOptions options = getSSLOptions();

    CANLListener l = new CANLListener();

    CertificateValidatorBuilder builder = new CertificateValidatorBuilder();

    X509CertChainValidatorExt validator = builder
      .trustAnchorsDir(options.getTrustStoreDirectory())
      .trustAnchorsUpdateInterval(trustDirRefreshIntervalInMsec)
      .crlChecks(CrlCheckingMode.IF_VALID).lazyAnchorsLoading(false)
      .ocspChecks(OCSPCheckingMode.IGNORE).storeUpdateListener(l)
      .validationErrorListener(l)
      .build();
    
    int maxConnections = Integer
      .parseInt(getConfigurationProperty(ConfigurationProperty.MAX_CONNECTIONS));

    int maxRequestQueueSize = Integer
      .parseInt(getConfigurationProperty(ConfigurationProperty.MAX_REQUEST_QUEUE_SIZE));

    server = ServerFactory.newServer(bindAddress, Integer.parseInt(port),
      getSSLOptions(), validator, maxConnections, maxRequestQueueSize);

    MBeanContainer mbContainer = new MBeanContainer(ManagementFactory
      .getPlatformMBeanServer());

    // Enable JMX
    server.addBean(mbContainer);
    server.getContainer().addEventListener(mbContainer);
    mbContainer.addBean(Log.getLog());
    
    addNameToHTTPSConnector();
    configureLocalHTTPConnector();
    
    server.addLifeCycleListener(new ServerListener());

    configureDeploymentManager();

    configureLegacyConnectors(null, validator, options, maxConnections,
      maxRequestQueueSize);

    // Handlers contains VOMS webapp, status webapp, and as final
    // choice the default handler to correctly handle 404 for non-handled
    // contexts etc.
    handlers.setHandlers(new Handler[] { contexts, new DefaultHandler() });

    // The rewrite handler, will internally foward
    // generate-ac request to legacy voms ports to the appropriate
    // voms-admin webapp
    RewriteHandler rh = new RewriteHandler();
    rh.setRewritePathInfo(true);
    rh.setRewriteRequestURI(true);

    rh.addRule(new VOMSRewriteRule(getConfiguredVOPortMappings()));

    // webapps are wrapped in the rewrite handler
    // or VOMS port rewriting will not work
    rh.setHandler(handlers);
    server.setHandler(rh);

    server.setDumpAfterStart(false);
    server.setDumpBeforeStop(false);
    server.setStopAtShutdown(true);

    server.addBean(deploymentManager);

  }

  private void start() {

    JettyRunThread vomsService = new JettyRunThread(server);
    vomsService.start();
  }

  private void initOptions() {

    cliOptions = new Options();

    cliOptions.addOption(ARG_WAR, true, "The WAR used to start this server.");

    cliOptions.addOption(ARG_DEPLOYDIR, true,
      "The VOMS Admin deploy directory.");

    cliOptions.addOption(ARG_CONFDIR, true,
      "The configuration directory where " + "VOMS configuration is stored.");

  }

  private void failAndExit(String errorMessage, Throwable t) {

    if (t != null) {

      System.err.format("%s: %s\n", errorMessage, t.getMessage());

    } else {

      System.err.println(errorMessage);

    }

    System.exit(1);

  }

  private void parseCommandLineOptions(String[] args) {

    try {

      CommandLine cmdLine = parser.parse(cliOptions, args);

      Properties sysconfigProperties = SysconfigUtil.loadSysconfig();
      String installationPrefix = SysconfigUtil.getInstallationPrefix();

      String defaultPrefixedWarPath = String.format("%s/%s",
        installationPrefix, DEFAULT_WAR).replaceAll("/+", "/");

      war = cmdLine.getOptionValue(ARG_WAR, defaultPrefixedWarPath);

      confDir = cmdLine.getOptionValue(ARG_CONFDIR);

      if (confDir == null)
        confDir = sysconfigProperties
          .getProperty(SysconfigUtil.SYSCONFIG_CONF_DIR);

    } catch (ParseException e) {

      failAndExit("Error parsing command line arguments", e);

    }
  }

  private String getConfigurationProperty(ConfigurationProperty prop) {

    return serverConfiguration.getProperty(prop.getPropertyName(),
      prop.getDefaultValue());
  }

  private void loadServerConfiguration(String configurationDir) {

    try {
      serverConfiguration = new Properties();

      String configurationPath = String.format("%s/%s", configurationDir,
        CONF_FILE_NAME).replaceAll("/+", "/");

      serverConfiguration.load(new FileReader(configurationPath));

    } catch (IOException e) {
      throw new RuntimeException("Error loading configuration:"
        + e.getMessage(), e);
    }

  }

  private void loadConfiguration() {

    confDir = SysconfigUtil.getConfDir();

    // FIXME: move this to standard server conf?
    // Then it would be harder to source from the init
    // script, which is the main (and only) client of
    // the status handler
    statusPort = SysconfigUtil.loadSysconfig().getProperty(
      SysconfigUtil.SYSCONFIG_STATUS_PORT, HTTP_CONNECTOR_PORT);

    loadServerConfiguration(confDir);

    host = getConfigurationProperty(ConfigurationProperty.HOST);
    port = getConfigurationProperty(ConfigurationProperty.PORT);

    bindAddress = getConfigurationProperty(ConfigurationProperty.BIND_ADDRESS);

    certFile = getConfigurationProperty(ConfigurationProperty.CERT);
    keyFile = getConfigurationProperty(ConfigurationProperty.KEY);

    trustDir = getConfigurationProperty(ConfigurationProperty.TRUST_ANCHORS_DIR);

    tlsIncludeCipherSuites = getConfigurationProperty(
      ConfigurationProperty.TLS_INCLUDE_CIPHER_SUITES);

    tlsExcludeCipherSuites = getConfigurationProperty(
      ConfigurationProperty.TLS_EXCLUDE_CIPHER_SUITES);

    tlsExcludeProtocols = getConfigurationProperty(
      ConfigurationProperty.TLS_EXCLUDE_PROTOCOLS);

    tlsIncludeProtocols = getConfigurationProperty(
      ConfigurationProperty.TLS_INCLUDE_PROTOCOLS);

    long refreshIntervalInSeconds = Long
      .parseLong(getConfigurationProperty(ConfigurationProperty.TRUST_ANCHORS_REFRESH_PERIOD));

    trustDirRefreshIntervalInMsec = TimeUnit.SECONDS
      .toMillis(refreshIntervalInSeconds);

    deployDir = String.format("%s/%s", SysconfigUtil.getInstallationPrefix(),
      DEFAULT_DEPLOY_DIR).replaceAll("/+", "/");

    workDir = String.format("%s/%s", SysconfigUtil.getInstallationPrefix(),
      DEFAULT_WORK_DIR).replaceAll("/+", "/");

  }

  // Without this trick JSP page rendering on VOMS admin does not work
  private void forceTaglibsLoading() {

    try {

      String classpath = java.lang.System.getProperty("java.class.path");
      String entries[] = classpath.split(System.getProperty("path.separator"));

      if (entries.length >= 1) {

        JarFile f = new JarFile(entries[0]);
        Attributes attrs = f.getManifest().getMainAttributes();
        Name n = new Name("Class-Path");
        String jarClasspath = attrs.getValue(n);
        String jarEntries[] = jarClasspath.split(" ");

        boolean taglibsFound = false;

        for (String e : jarEntries) {
          if (e.contains(TAGLIBS_JAR_NAME)) {
            taglibsFound = true;
            ClassLoader currentClassLoader = Thread.currentThread()
              .getContextClassLoader();

            File taglibsJar = new File(e);
            URLClassLoader newClassLoader = new URLClassLoader(
              new URL[] { taglibsJar.toURI().toURL() }, currentClassLoader);

            Thread.currentThread().setContextClassLoader(newClassLoader);
          }
        }
        
        f.close();
        
        if (!taglibsFound) {
          throw new RuntimeException("Error configuring taglibs classloading!");
        }

      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      System.exit(1);
    }
  }

  public Container(String[] args) {

    // Leave this here and first
    forceTaglibsLoading();

    try {

      initOptions();
      parseCommandLineOptions(args);
      configureLogging();

    } catch (Throwable t) {
      // Here we print the error to standard error as the logging setup
      // could be incomplete
      System.err.println("Error starting voms-admin server: " + t.getMessage());
      t.printStackTrace(System.err);
      System.exit(1);
    }

    try {

      loadConfiguration();
      logStartupConfiguration();
      configureJettyServer();
      start();
    } catch (Throwable t) {
      log.error("Error starting voms-admin server: " + t.getMessage(), t);
      System.exit(1);
    }
  }

  private void logStartupConfiguration() {

    log.info("VOMS Admin version {}.", Version.version());
    log.info("Hostname: {}", host);

    if (bindAddress != null) {
      log.info("Binding on: {}:{}", bindAddress, port);
    } else {
      log.info("Binding on all interfaces on port: {}", port);
    }

    if (tlsIncludeProtocols != null){
      log.info("TLS included protocols: {}", tlsIncludeProtocols);
    }

    if (tlsExcludeProtocols != null){
      log.info("TLS excluded protocols: {}", tlsExcludeProtocols);
    }

    if (tlsIncludeCipherSuites != null){
      log.info("TLS included cipher suites: {}", tlsIncludeCipherSuites);
    }

    if (tlsExcludeCipherSuites != null){
      log.info("TLS excluded cipher suites: {}", tlsExcludeCipherSuites);
    }

    log.info("HTTP status handler listening on: {}", statusPort);
    log.info("Service credentials: {}, {}", certFile, keyFile);
    log.info("Trust anchors directory: {}", trustDir);
    log.info("Trust anchors directory refresh interval (in minutes): {}",
      TimeUnit.MILLISECONDS.toMinutes(trustDirRefreshIntervalInMsec));
    log.info("Web archive location: {}", war);
    log.info("Configuration dir: {}", confDir);
    log.info("Deployment dir: {}", deployDir);

    log.info("Max # of concurrent connections: {}",
      getConfigurationProperty(ConfigurationProperty.MAX_CONNECTIONS));

    log.info("Max request queue size: {}",
      getConfigurationProperty(ConfigurationProperty.MAX_REQUEST_QUEUE_SIZE));

  }

  private void configureLogging() {

    String loggingConf = String.format("%s/%s", confDir,
      "voms-admin-server.logback").replaceAll("/+", "/");

    File f = new File(loggingConf);

    if (!f.exists() || !f.canRead()) {
      log.error("Error loading logging configuration: "
        + "{} does not exist or is not readable.");
      return;
    }

    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    JoranConfigurator configurator = new JoranConfigurator();

    configurator.setContext(lc);
    lc.reset();

    try {
      configurator.doConfigure(loggingConf);

    } catch (JoranException e) {

      failAndExit("Error setting up the logging system", e);

    }
  }

  public static void main(String[] args) {

    new Container(args);
  }

}
