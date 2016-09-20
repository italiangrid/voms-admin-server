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
package org.glite.security.voms.admin.persistence.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.persistence.DBUtil;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.lookup.LookupPolicyProvider;
import org.hibernate.cfg.Configuration;

public class AuditLogCtl {

  Map<String, AuditLogCtlCommand> commands;
  HelpFormatter helpFormatter = new HelpFormatter();
  Options options;

  private void printHelp() {

    StringBuilder footer = new StringBuilder();

    footer.append("\nAvaiable commands:\n");

    for (AuditLogCtlCommand command : commands.values()) {
      footer.append(String.format("\n%s:\n    %s\n", command.getName(),
        command.getDescription()));
    }

    helpFormatter.printHelp("AuditLogCtl [command]",
      "\nPrints or deletes entries in the VOMS Admin audit log.\nOptions:\n\n",
      options, null, true);

    System.out.println(footer.toString());

  }

  private void errorAndHelpFormatter(String msg) {

    System.err.println(msg);
    printHelp();
    System.exit(1);
  }

  private void errorAndExit(String msg) {

    System.err.println(msg);
    System.exit(1);
  }

  private void errorAndExit(String msg, Throwable t) {

    System.err.println(msg);
    t.printStackTrace(System.err);
    System.exit(1);
  }

  private void initCommands() {

    commands = new HashMap<String, AuditLogCtlCommand>();
    commands.put(Count.NAME, new Count());
    commands.put(Print.NAME, new Print());
    commands.put(DeleteRecordCommand.NAME, new DeleteRecordCommand());
    commands.put(DeleteRecordsCommand.NAME, new DeleteRecordsCommand());
  }

  private void loadVomsAdminConfiguration(CommandLine line) {

    System.setProperty(VOMSConfigurationConstants.VO_NAME,
      line.getOptionValue("vo"));
    VOMSConfiguration.load(null);

  }

  private void initializeLookupPolicyProvider() {

    boolean skipCaCheck = VOMSConfiguration.instance()
      .getBoolean(VOMSConfigurationConstants.SKIP_CA_CHECK, false);

    LookupPolicyProvider.initialize(skipCaCheck);
  }

  private void initializePersistence(CommandLine line) {

    loadVomsAdminConfiguration(line);
    initializeLookupPolicyProvider();
    Configuration hibernateConfig = DBUtil
      .loadHibernateConfiguration("/etc/voms-admin", line.getOptionValue("vo"));

    hibernateConfig.configure();
    HibernateFactory.initialize(hibernateConfig);
  }

  @SuppressWarnings("static-access")
  private CommandLine parseArgs(String[] args) throws ParseException {

    Option help = OptionBuilder.withDescription("prints help").create("help");

    Option vo = OptionBuilder.withArgName("voname").hasArg()
      .withDescription("Act on VO <voname>").create("vo");

    Option fromTime = OptionBuilder.withArgName("time").hasArg()
      .withDescription(
        "Include records starting from this <time>. Time format is 'yyyy-MM-ddTHH:mm:ss.S'")
      .create("fromTime");

    Option toTime = OptionBuilder.withArgName("time").hasArg()
      .withDescription(
        "Include records to this <time>. Time format is 'yyyy-MM-ddTHH:mm:ss.S'")
      .create("toTime");

    Option type = OptionBuilder.withArgName("eventType").hasArg()
      .withDescription("Include records matching <eventType>")
      .create("eventType");

    Option principal = OptionBuilder.withArgName("principal").hasArg()
      .withDescription("Include records matching <principal>")
      .create("principal");

    Option first = OptionBuilder.withArgName("recordNumber").hasArg()
      .withDescription("Start from <recordNumber> record").create("first");

    Option maxResults = OptionBuilder.withArgName("maxResults").hasArg()
      .withDescription("Return at most <maxResults> records")
      .create("maxResults");

    Option id = OptionBuilder.withArgName("recordId").hasArg()
      .withDescription("Operate on record <recordId>").create("id");

    options = new Options();
    options.addOption(help);
    options.addOption(vo);
    options.addOption(fromTime);
    options.addOption(toTime);
    options.addOption(type);
    options.addOption(principal);
    options.addOption(first);
    options.addOption(maxResults);
    options.addOption(id);

    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);

  }

  private AuditLogCtlCommand validateCommandLine(CommandLine line) {

    if (line.hasOption("help")) {
      printHelp();
      System.exit(0);
    }

    if (!line.hasOption("vo")) {
      errorAndExit("Please specify a vo with the --vo option");
    }

    @SuppressWarnings("unchecked")
    List<String> args = line.getArgList();

    if (args.isEmpty()) {
      errorAndExit("No command specified");
    }

    if (args.size() > 1) {
      errorAndExit(
        "More than one command argument: " + StringUtils.join(args, ","));
    }

    String command = args.get(0);

    if (!commands.containsKey(command)) {
      errorAndExit("Unrecognized command: " + command);
    }

    return commands.get(command);

  }

  private void executeCommand(CommandLine line) {

    AuditLogCtlCommand command = validateCommandLine(line);
    initializePersistence(line);

    HibernateFactory.beginTransaction();
    try {
      command.execute(line);
      HibernateFactory.commitTransaction();
    } catch (Throwable t) {
      HibernateFactory.rollbackTransaction();
      errorAndExit("Command execution error: " + t.getMessage(), t);
    }

  }

  public AuditLogCtl(String[] args) {
    try {

      initCommands();
      CommandLine line = parseArgs(args);
      executeCommand(line);

    } catch (ParseException e) {
      errorAndHelpFormatter("Parse error: " + e.getMessage());

    }

  }

  public static void main(String[] args) {

    new AuditLogCtl(args);
  }

}
