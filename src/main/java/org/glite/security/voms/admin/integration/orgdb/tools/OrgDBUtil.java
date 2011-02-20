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

package org.glite.security.voms.admin.integration.orgdb.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBDAOFactory;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBVOMSPersonDAO;
import org.glite.security.voms.admin.integration.orgdb.database.OrgDBSessionFactory;
import org.glite.security.voms.admin.integration.orgdb.model.Experiment;
import org.glite.security.voms.admin.integration.orgdb.model.Institute;
import org.glite.security.voms.admin.integration.orgdb.model.Participation;
import org.glite.security.voms.admin.integration.orgdb.model.VOMSOrgDBPerson;
import org.glite.security.voms.admin.integration.orgdb.model.Participation.Id;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrgDBUtil {

	public static final String EXPERIMENT_NAME = "ATLAS";
	
	public static final String INSTITUTE_CODE = "077630"; // CNAF
	
	public static final Logger log = LoggerFactory
			.getLogger(OrgDBUtil.class);
	
	protected CommandLineParser parser = new PosixParser();
	protected CommandLine commandLine = null;
	protected HelpFormatter helpFormatter = new HelpFormatter();
	
	protected Options options = new Options();
	
	protected Command command;
	
	protected String indentSpace ="  ";
	
	enum Command {
		list,find, create,expire, restore, delete
	}
	

	protected void parseCommandLine(String[] args) throws ParseException {

		commandLine = parser.parse(options, args);

		
		if (commandLine.hasOption("h"))
			printHelpMessageAndExit(0);
		
		if (!commandLine.hasOption("config")){
			log.error("Please specify the orgDb hibernate configuration using the --config option.");
			printHelpMessageAndExit(-1);
			
		}
			
		
		if (commandLine.getArgs().length == 0){
			log.error("Please specify a command.");
			printHelpMessageAndExit(-1);
		}
		
		try{
			
			command = Command.valueOf(commandLine.getArgs()[0]);
		
		}catch (IllegalArgumentException e){
			log.error("Unknown command: "+commandLine.getArgs()[0]);
			printHelpMessageAndExit(-1);
		}
		
		
	}

	private void printHelpMessageAndExit(int i) {
		helpFormatter.printHelp("OrgDBUtil", options);
		System.exit(i);
	}
	
	

	protected void setupCLParser() {

		options.addOption(OptionBuilder.withLongOpt("help").withDescription(
				"Displays helps and exits.").create("h"));
		
		options
		.addOption(OptionBuilder
				.withLongOpt("config")
				.withDescription(
						"Specifies the config to be used to connect to the OrgDB")
				.hasArg().create("c"));
		
		options
		.addOption(OptionBuilder
				.withLongOpt("email")
				.withDescription(
						"Specifies the email to be used when searching/creating users.")
				.hasArg().create("email"));
		
		options
		.addOption(OptionBuilder
				.withLongOpt("name")
				.withDescription(
						"Specifies the name to be used when searching/creating users.")
				.hasArg().create("name"));
		
		
		options
		.addOption(OptionBuilder
				.withLongOpt("surname")
				.withDescription(
						"Specifies the surname to be used when searching/creating users.")
				.hasArg().create("surname"));
		
		options
		.addOption(OptionBuilder
				.withLongOpt("id")
				.withDescription(
						"Specifies the OrgDB id to be used when searching/creating users.")
				.hasArg().create("id"));
		
		options
		.addOption(OptionBuilder
				.withLongOpt("institute")
				.withDescription(
						"Specifies the OrgDB id to be used when searching/creating users.")
				.hasArg().create("institute"));
		
		
		options
		.addOption(OptionBuilder
				.withLongOpt("experiment")
				.withDescription(
						"Specifies the OrgDB id to be used when searching/creating users.")
				.hasArg().create("experiment"));
		
		options
		.addOption(OptionBuilder
				.withLongOpt("verbose")
				.withDescription(
						"Produce verbose output.")
				.create("v"));
		
		

	}
	
	
	protected String readStringFromConsole() throws IOException{
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return reader.readLine();
		
	}
	
	
	protected void printVOMSPerson(VOMSOrgDBPerson person){
		
		
		
		if (commandLine.hasOption("verbose"))
			System.out.println(person);
		else
			System.out.printf("%d %s %s - %s\n", person.getId(), person.getFirstName(), person.getName(), person.getPhysicalEmail());
				
				
		for (Participation p: person.getParticipations()){
			
			if (commandLine.hasOption("verbose"))
				System.out.println(p);
			else
				System.out.printf("%s From: %s To: %s\n", p.getExperiment().getName(), p.getStartDate(), p.getEndDate());

		}
		
		System.out.println("\n");
		
	}
	protected void doListUser(){
		
		OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance().getVOMSPersonDAO();
		
		for (VOMSOrgDBPerson p: dao.findAll())
			printVOMSPerson(p);
		
		
	}
	
	
	protected void printErrorMessageAndExit(String message){
		
		System.err.println(message);
		System.exit(2);
	}
	
	
	protected Long getNewVOMSPersonId(Session s){
		
		if (s == null|| ! s.isOpen())
			s = OrgDBSessionFactory.getSessionFactory().openSession();
	
		Long id = (Long) s.createQuery("select max(id) from VOMSOrgDBPerson").uniqueResult();
		return id+1;
		
	}
	
	
	
	protected Participation cloneParticipation(Participation p){
		
		Participation clone = new Participation();
		Id nId = new Id();
		nId.setExperimentId(p.getExperiment().getName());
		nId.setInstituteId(p.getInstitute().getCode());
		clone.setInstitute(p.getInstitute());
		clone.setExperiment(p.getExperiment());
		clone.setStartDate(new Date());
		
		clone.setEndDate(null);
		
		return clone;
		
	}
	
	
	protected VOMSOrgDBPerson clonePerson(VOMSOrgDBPerson template, Long id){
		VOMSOrgDBPerson newPerson = new VOMSOrgDBPerson();
		newPerson.setId(id);
		
		newPerson.setPhysicalEmail(template.getPhysicalEmail());
		newPerson.setEmail(template.getEmail());
		newPerson.setInstitute(template.getInstitute());
		newPerson.setCernId(template.getCernId());
		newPerson.setAtCern(template.getAtCern());
		
		return newPerson;
		
		
	}
	
	
	protected String getValueFromConsoleIfOptionUndefined(String optionName) throws IOException{
		
		String value;
		if (!commandLine.hasOption(optionName)){
			System.out.println("Please enter "+optionName+":");
			value = readStringFromConsole();
		}else
			value = commandLine.getOptionValue(optionName);
		
		return value;
	}
	
	
	protected void updateParticipation(Participation p, Date newEndDate){
		
		Session s = OrgDBSessionFactory.getSessionFactory().getCurrentSession();
		s.getTransaction();
		
		Query q = s.createSQLQuery("update person_participation set end_date = ? where person_id = ?");
		q.setDate(0, newEndDate);
		q.setLong(1,p.getVomsPerson().getId());
		
		q.executeUpdate();
		s.getTransaction().commit();
		
	}
	
	
	protected void createParticipation(VOMSOrgDBPerson p, Experiment e, Institute i){
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		
		Session s = OrgDBSessionFactory.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		
		Query q = s.createSQLQuery("insert into person_participation(EXPERIMENT,INSTITUTE,PERSON_ID,START_DATE) values(?,?,?,?)");
		q.setString(0, e.getName());
		q.setString(1, i.getCode());
		q.setLong(2, p.getId());
		q.setDate(3, cal.getTime());
				
		q.executeUpdate();
		s.getTransaction().commit();
	}
	
	protected Experiment findExperiment(String name){
		
		Query q = OrgDBSessionFactory.getSessionFactory().openSession().createQuery("from Experiment where name = :name");
		return (Experiment) q.setString("name", name).uniqueResult();
		
	}
	
	protected Institute findInstitute(String code){
		
		Query q = OrgDBSessionFactory.getSessionFactory().openSession().createQuery("from Institute where code = :code");
		return (Institute) q.setString("code", code).uniqueResult();
	}
	
	protected void doDelete(){
		
		
		if (!commandLine.hasOption("id"))
			printErrorMessageAndExit("Please provide an id!");
		
		System.out.println("Deleting member with id "+commandLine.getOptionValue("id"));
		
		OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance().getVOMSPersonDAO();
		VOMSOrgDBPerson p = dao.findPersonById(Long.parseLong(commandLine.getOptionValue("id")));
		
		if (p == null)
			printErrorMessageAndExit("No user found for id "+commandLine.getOptionValue("id"));
		
		dao.makeTransient(p);
		
	}
	
	protected void doCreate() throws IOException{
		
		String name, surname, email, id, experiment, institute;
		
		name = getValueFromConsoleIfOptionUndefined("name").trim();
		surname = getValueFromConsoleIfOptionUndefined("surname").trim();
		email = getValueFromConsoleIfOptionUndefined("email").trim();
		experiment = EXPERIMENT_NAME;
		institute = INSTITUTE_CODE;
		
			
		OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance().getVOMSPersonDAO();
		
		if (dao.findPersonByEmail(email) != null){
			
			printErrorMessageAndExit("OrgDB already have a user bound to email "+email+"!");
		}
		
		Experiment e = findExperiment(experiment);
		if (e == null)
			printErrorMessageAndExit("Experiment not found");
		
		Institute i = findInstitute(institute);
		
		if (i == null)
			printErrorMessageAndExit("Institute not found");
		
		Long newId = getNewVOMSPersonId(null);
		VOMSOrgDBPerson newUser = new VOMSOrgDBPerson();
		newUser.setId(newId);
		newUser.setFirstName(name.toUpperCase());
		newUser.setName(surname.toUpperCase());
		newUser.setPhysicalEmail(email);
		newUser.setEmail(email);
		newUser.setAtCern("N");
		
		Calendar cal = Calendar.getInstance();
		cal.set(1975, 9, 15);
		newUser.setDateOfBirth(cal.getTime());
		
		dao.makePersistent(newUser);
		dao.flush();
		
		createParticipation(newUser, e, i);
		
		dao = OrgDBDAOFactory.instance().getVOMSPersonDAO();
		VOMSOrgDBPerson realNew = dao.findById(newId, false);		
		
		System.out.println("Created: ");
		printVOMSPerson(realNew);
		
	}
	
	
	protected void doExpire(){
		
		if (!commandLine.hasOption("id"))
			printErrorMessageAndExit("Please provide an id!");
		
		System.out.println("Expiring member with id "+commandLine.getOptionValue("id"));
		
		OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance().getVOMSPersonDAO();
		VOMSOrgDBPerson p = dao.findPersonById(Long.parseLong(commandLine.getOptionValue("id")));
		
		if (p == null)
			printErrorMessageAndExit("No user found for id "+commandLine.getOptionValue("id"));
		
		Participation participation = p.getValidParticipationForExperiment(EXPERIMENT_NAME);
		if (participation == null)
			printErrorMessageAndExit("No valid participation found for user "+p);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR,-1);
		Date newEnd = cal.getTime();
		
		updateParticipation(participation, newEnd);
		System.out.println("Member expired.");
		
		findAndPrintUser(p.getId());
		
	}
	
	protected void doFind(){
		
		if (!commandLine.hasOption("email") && 
				!commandLine.hasOption("name") && 
				!commandLine.hasOption("surname") &&
				!commandLine.hasOption("id")){
			
			System.err.println("Please provide an email or (name,surname) for the search!");
			printHelpMessageAndExit(1);
		}
		
		OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance().getVOMSPersonDAO();
		List<VOMSOrgDBPerson> results = new ArrayList<VOMSOrgDBPerson>();
		
		if (commandLine.hasOption("email")){	
			
			System.out.println("Searching for "+commandLine.getOptionValue("email"));
			
			VOMSOrgDBPerson p = dao.findPersonByEmail(commandLine.getOptionValue("email"));
			if (p != null)
				results.add(p);
			
		}
		
		if (commandLine.hasOption("name") && commandLine.hasOption("surname")){
		
			System.out.println("Searching for "+commandLine.getOptionValue("name")+" "+
					commandLine.getOptionValue("surname"));
			
			List<VOMSOrgDBPerson> persons = dao.findPersonByName(commandLine.getOptionValue("name"),
					commandLine.getOptionValue("surname"));
			results.addAll(persons);
			
		}
		
		if (commandLine.hasOption("id")){
			System.out.println("Searching for id "+commandLine.getOptionValue("id"));
			
			VOMSOrgDBPerson p = dao.findPersonById(Long.parseLong(commandLine.getOptionValue("id")));
			if (p != null)
				results.add(p);
		}
		
		System.out.println();
		if (results.isEmpty())
			System.out.println("No matches found.");
		else
			for(VOMSOrgDBPerson p: results)
				printVOMSPerson(p);
		
			
	}
	
	protected void execute() throws IOException{
		
		switch (command) {
		case list:
			doListUser();
			break;
			
		case create:
			doCreate();
			break;
			
		case expire:
			doExpire();
			break;

		case find:
			doFind();
			break;
			
		case restore:
			doRestore();
			break;
		
		case delete:
			doDelete();
			break;
			
		default:
			printErrorMessageAndExit("Unimplemented command!");
		}
		
		
	}
	
	public void findAndPrintUser(Long id){
		
		OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance().getVOMSPersonDAO();
		VOMSOrgDBPerson p = dao.findPersonById(id);
		
		if (p == null)
			System.out.println("No user found with id "+id);
		else{
			
			printVOMSPerson(p);
		}
			
		
	}
	public void doRestore() {
		
		if (!commandLine.hasOption("id"))
			printErrorMessageAndExit("Please provide an id!");
		
		System.out.println("Restoring member with id "+commandLine.getOptionValue("id"));
		OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance().getVOMSPersonDAO();
		VOMSOrgDBPerson p = dao.findPersonById(Long.parseLong(commandLine.getOptionValue("id")));
		
		if (p == null)
			printErrorMessageAndExit("No user found for id "+commandLine.getOptionValue("id"));
		
		Participation participation = p.getLastExpiredParticipationForExperiment(EXPERIMENT_NAME);
		
		if (participation == null)
			printErrorMessageAndExit("No expired participations found for user "+p);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 1);
		Date newEnd = cal.getTime();
		updateParticipation(participation, newEnd);
		
		System.out.println("Member restored.");
		findAndPrintUser(p.getId());
		
	}

	protected void initializeDatabase() throws FileNotFoundException, IOException{
		Properties configProps = new Properties();
		configProps.load(new FileReader(commandLine.getOptionValue("config")));
		
		OrgDBSessionFactory.initialize(configProps);
		
	}
	
	public OrgDBUtil(String[] args) throws ParseException, FileNotFoundException, IOException {
		setupCLParser();
		parseCommandLine(args);
		initializeDatabase();
		execute();
		
		Transaction tx = OrgDBSessionFactory.getSessionFactory().getCurrentSession().getTransaction(); 
		if (tx.isActive() && !tx.wasCommitted())
			tx.commit();
	}

	public static void main(String[] args) throws ParseException, FileNotFoundException, IOException {

		if ((System.getenv("GLITE_LOCATION_VAR") == null)
				&& (System.getenv("VOMS_ADMIN_LOCATION_VAR") == null))
			throw new VOMSException(
					"Please set the VOMS_ADMIN_LOCATION_VAR or GLITE_LOCATION_VAR environment variables before running this utility.");
		
		new OrgDBUtil(args);

	}

}
