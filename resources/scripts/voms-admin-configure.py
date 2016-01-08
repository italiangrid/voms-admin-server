#!/usr/bin/env python
#
# Copyright (c) Members of the EGEE Collaboration. 2006-2009.
# See http://www.eu-egee.org/partners/ for details on the copyright holders.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Authors:
#     Andrea Ceccanti (INFN)

# Help message about command line options by:
#
#     Karoly Lorentey - karoly.lorentey@cern.ch

from os import environ
from voms import *
import os.path
import getopt
import sys
import exceptions
import pwd
import grp
import socket

options = {}

certificate = None

command = None

supported_commands = ("install", "remove", "upgrade")

def vlog(msg):
    global options
    if options.has_key("verbose"):
        print msg

def setup_aa_defaults():
    global certificate, options
    
    vlog("Setting defaults for the VOMS AA credentials")
    
    ## Default assertion lifetime
    set_default(options, "saml-max-assertion-lifetime", "720")
    
    ## AA Certificate defaults
    tomcat_cert = "/etc/grid-security/tomcat-cert.pem"
    tomcat_key = "/etc/grid-security/tomcat-key.pem"
    
    if os.path.exists(tomcat_cert) and os.path.exists(tomcat_key):
        set_default(options, "aa-cert", tomcat_cert)
        set_default(options, "aa-key", tomcat_key)
        vlog("AA certificates settings:\ncert:%s\nkey:%s" % (tomcat_cert, tomcat_key))
        return
    
    host_cert =  "/etc/grid-security/hostcert.pem"
    host_key = "/etc/grid-security/hostkey.pem"
    
    set_default(options, "aa-cert", host_cert)
    set_default(options, "aa-key", host_key)
    vlog("Using host certificate for VOMS AA:\ncert:%s\nkey:%s" % (host_cert, host_key))


def setup_defaults():
    global options
    
    set_default(options,"hostname",socket.gethostname())
    
    set_default(options,"libdir", voms_lib_dir())
    set_default(options,"logdir", voms_log_dir())
    
    set_default(options, "mysql-command", "mysql")
    set_default(options, "openssl","openssl")
    set_default(options, "dbauser", "root")    
    

def setup_identity():

    global certificate,options
    
    vlog("Setting up user credentials...")
    
    user_id = os.geteuid()
    
    if options.has_key("config-owner"):
        if user_id != 0:
            raise VomsConfigureError, "I need to run as root for config-owner to work!"
        try:
            pwd_info = pwd.getpwnam(options['config-owner'])
        except KeyError:
            raise VomsConfigureError, "User unknown: %s" % options['config-owner']        
        
        user_id = pwd_info[2]
    
    tomcat_group = None
    
    if not options.has_key("tomcat-group"):
        if user_id == 0:
            for group in ['tomcat5','tomcat4','tomcat']:
                try:
                    
                    if grp.getgrnam(group):
                        (gr_name,gr_passwd, gr_gid,gr_mem) = grp.getgrnam(group)
                        options['tomcat-group-id']=gr_gid
                        break
                
                except KeyError, k:
                    continue
                    
            if not options.has_key('tomcat-group-id'):
                raise VomsConfigureError,"Please specify the --tomcat-group option. The default 'tomcat5', 'tomcat4', 'tomcat' are not applicable to your system."
        else:
            options['tomcat-group-id'] = os.getgid()
    else:
        try:
            (gr_name,gr_passwd, gr_gid,gr_mem) =  grp.getgrnam(options['tomcat-group'])
            options['tomcat-group-id']=gr_gid
            
        except KeyError, k:
            raise VomsConfigureError, "The tomcat-group passed as argument (%s) does not exist on this system!" % options['tomcat-group']
        
    if options.has_key('cert'):
    
        vlog("Using credentials specified with the --cert command line option.")
        certificate = X509Helper(options['cert'],options['openssl'])
    
    elif os.environ.has_key("X509_USER_CERT"):
        vlog("Using credentials specified in X509_USER_CERT environment variable")
        certificate = X509Helper(os.environ.get("X509_USER_CERT", None),options['openssl'])
    elif user_id == 0:
        vlog("Using host credentials (/etc/grid-security/hostcert.pem) since running as root.")
        certificate = X509Helper("/etc/grid-security/hostcert.pem",options['openssl'])
    else:
        vlog("Using user credentials from ~/.globus.")
        certificate = X509Helper(os.environ.get("HOME", None)+"/.globus/usercert.pem",options['openssl'])

    options['ta.subject']=certificate.subject
    options['ta.ca']=certificate.issuer

 
    
def check_installed_setup():
    
    vlog("Checking local installation...")
    checked_paths = [ 
                     VomsConstants.db_props_template,
                     VomsConstants.context_template,
                     VomsConstants.service_props_template,
                     VomsConstants.voms_admin_war,
                     VomsConstants.voms_admin_jar
                     ]
    
    for i in checked_paths:
        if not os.path.exists(i):
            raise VomsConfigureError, i+ " not found in local filesystem! Local installation check failed."    

def usage():
    usage_str = """

    Usage:
    
        voms-admin-configure install --vo foobar.example.org --dbtype mysql
                            --dbusername foobar --dbpassword secret --dbname foobar
                            
        voms-admin-configure remove --vo <vo name>
        
        voms-admin-configure upgrade --vo <vo name> [OPTIONS]
        
    
    Options:

    General options:
     --help                Print this help message and exit.
     --version             Print version string.
     --verbose             Print more messages.

     --vo VONAME           Install or delete the named VO.

    Options for configuring the admin service (ignored for "remove"):
     --admincert CERTFILE  The certificate file of an initial VO administrator.
                           The VO will be set up so that this user has full
                           VO administration privileges.

     --mail-from FROM      Set the address of the VO administration mailbox.
                           (There is no default; you must specify a valid
                           email address that reaches the VO administrators.)

     --smtp-host HOST      Submit service-generated emails to this host.  (There is
                           no default for this option.  Use "localhost" if you have
                           an fully configured SMTP server running on this host.
                           Otherwise specify the hostname of a working SMTP
                           submission service.)

    Options for configuring the core service (ignored for "remove"):
     --port NUMBER         The port that the VOMS core service should use.  (There
                           is no default.  The port number must be different for
                           each VO.  By convention, port numbers are allocated
                           starting with 15000.)

    Database options:
     --dbtype TYPE         Database type: "mysql" or "oracle".
                           (Default is "mysql".)

     --dbname NAME         Database name for the VO's database account.
                           Required on oracle installations, refers to the tns alias associated with the db.
                           On mysql denotes the name of the database, and may not be
                           specified (the default being voms_<vo_name>)
                           
     --dbusername NAME     Database user name for the VO's database account.
     --dbpassword PWD      Database password for the VO's database account.

     --dbhost HOST         Hostname of the database server.
                           (Default is "localhost".)

     --dbport PORT         Port number of the database server.
                           (Default is 1521 (Oracle) or 3306 (MySQL).)
                           
     --skip-database       For install, recreates the configuration files without
                           touching the database contents.

     --deploy-database     For install, clean out and (re)create database schema.
                           The current database contents will be lost. ( The default is
                           to not touch the database contents.)
                           
     --undeploy-database   For remove, clean out database by dropping all
                           database tables.


     If you use the above options, then you must make sure that the database
     account is set up and accessible before running this script.

    Oracle specific options:
     
                          
    Mysql-specific options (not supported on Oracle):
    
     --createdb            Automatically create database.

     --dbauser USER        Database userid of the MySQL administrator account.
                           (Default is root.  Implies --createdb.)

     --dbapwd PASSWORD     The password of the MySQL administrator account.
                           (Implies --createdb.)

     --dbapwdfile FILE     The location of a one-line file containing the DB
                           administrator password.  (Implies --createdb.)

     --mysql-command PATH  The path to the "mysql" executable.
                           (Default is "/usr/bin/mysql".)


    Additional options for special effects:
     --code CODE           An integer code for the core service that is
                           different for each VO installation using the
                           same server certificate.  Used for generating
                           the serial numbers of the attribute
                           certificates. (Default is the value of --port.)

     --libdir PATH         The directory that contains the database access
                           libraries of the VOMS core service.
                           
    
     --logdir PATH         The directory that will contain the VOMS core service
                           log files. 

     --sqlloc FILE         Full path to the database access library for the VOMS
                           core service.  (In case --libdir is not enough.)

     --uri                 This flag defines the VOMS core uri configuration 
                           parameter. The uri defines the issuing server information
                           that is included in the issued Attribute Certificates,
                           according to the "hostname:port"syntax. 
                           (Example: voms.cnaf.infn.it:15000)
     
     --timeout             This flag defines the VOMS core timeout configuration
                           parameter, i.e.the default timeout in seconds for 
                           ACs issued by the VOMS server. (Default is 86400)

     --shortfqans          Configures VOMS core to issue FQANs in the short format.
     

     --config-owner USER   The UNIX user that should own all configuration files.
                           (Default is the effective userid of the script.)

     --tomcat-group GROUP  The UNIX group that Tomcat is run under.
                           (Default is "tomcat5", "tomcat4", or "tomcat".)

     --voms-group GROUP    The UNIX group that the VOMS core service is run under.
                           (Default is "voms", if it exists.)

     --hostname FQDN       The fully qualified domain name of this host.
                           (Useful if you want to use an alias instead.)

     --openssl COMMAND     The path to the openssl executable used to interpret
                           certificates.

     --cert FILENAME       Override $X509_USER_CERT.
     --key FILENAME        Override $X509_USER_KEY.
     --certdir DIR         Override $X509_CERT_DIR.
     
     --disable-webui-requests         
                           
                           Disables user registration via the voms-admin web 
                           interface.
     
     --read-access-for-authenticated-clients    
     
                           Setup ACLs so that authenticated clients can browse the VO.
                           This is needed to support mkgridmap clients.
     
     --skip-voms-core
                           Skips voms core configuration creation (i.e., only voms-admin
                           is configured).
    
     --vo-aup-url          Sets the URL for the initial version of the VO acceptable usage 
                           policy. Usually the URL points to a local or remote, http accessible
                           text file. If this option is not set a template vo-aup file will
                           be created in vo runtime configuration directory.

     --admin-skip-ca-check

                           Skips the check on the certificate issuer when authenticating
                           VOMS Admin clients.
VOMS SAML options:

     --aa-cert
                          The certificate that will be used by the VOMS SAML attribute 
                          authority
     
     --aa-key             
                          The private key that will be used by the VOMS SAML attribute
                          authority
     
     --saml-max-assertion-lifetime
                          
                          The lifetime (expressed in minutes) of SAML attribute assertions
                          issued by the VOMS SAML attribute authority
                          (default value: 720 minutes = 12 hours) 
    """
    
    print usage_str
    
    
def parse_options():
    global command,supported_commands
    
    cmd_line = None

    if len(sys.argv) == 1:
        print "No command specified, Type voms-admin-configure --help for more information."
        sys.exit(2)
    
    if re.match("^--",sys.argv[1]):
        cmd_line = sys.argv[1:]
    else:
        command = sys.argv[1]
        cmd_line = sys.argv[2:]
    
    try:
    
        opts,args = getopt.getopt(cmd_line, "", VomsConstants.long_options)
    
        for key,val in opts:
            options[key[2:]]=val
        
        if len(opts)==0:
            print "No options specified!"
            sys.exit(2)
            
        if options.has_key('help'):
            usage()
            sys.exit(0)
            
        if options.has_key('version'):
            sys.exit(0)
        
        if command is None:
            if len(args) == 0:
                print "No command specified!"
                sys.exit(2)
            
            command = args[0]
            
            if not command in supported_commands:
                print "Unknown command specified!"
                sys.exit(2)
        
    except getopt.GetoptError, e:
        print e
        sys.exit(2)

def check_install_options():
    
    vlog("Cheking input parameters")
    if not options.has_key('vo'):
        raise VomsConfigureError, "No vo specified!"
    
    a = InstallVOAction(options)
    a.check_options()
    
    if options["dbtype"] == "oracle":        
        a = InstallOracleVO(options)
        a.check_options()
    
    elif options["dbtype"] == "mysql":
        
        a = InstallMySqlVO(options)
        a.check_options()
    else: 
        raise VomsConfigureError, "Unsupported database type: "+ options["dbtype"]
    
    return a

def check_remove_options():
    vlog("Cheking input parameters...")
    if not options.has_key('vo'):
        raise VomsConfigureError, "No vo specified!"
    
    if is_oracle_vo(options['vo']):
        a = RemoveOracleVO(options)
        a.check_options()
    else:
        a = RemoveMySQLVO(options)
        a.check_options()
    
    return a

def check_upgrade_options():
    vlog("Checking input parameters...")
    
    a = UpgradeVO(options)
    a.check_options()
    
    return a
    
def do_install():
    
    action = check_install_options()
    
    print "Installing vo", options['vo']
    
    action.install_vo()
    
    vlog("VO %s configured correctly." % options['vo'])
    
    print "\n"
    
    print Template("""
VO @voname@ installation finished.\n 
You can start the voms services using the following commands:
    @prefix@/etc/init.d/voms start @voname@
    @prefix@/etc/init.d/voms-admin start @voname@""").sub({'voname':options['vo'], 'prefix': voms_prefix()})
    
    
    
    
    

def do_remove():
    action = check_remove_options()
    print "Removing vo ",options['vo']
    action.remove_vo()
    
    print "VO %s succesfully removed." % options['vo']
    
def do_upgrade():
    if not options.has_key('vo'):
        
        vos = configured_vos()
        if len(vos) == 0:
            raise VomsConfigureError, "No vo specified!"
        else:
            for v in vos:
                options['vo'] = v
                
                action = check_upgrade_options()
                action.upgrade_vo()
                print "Vo %s succesfully upgraded" % options['vo']
    
    else:
        action = check_upgrade_options()
        action.upgrade_vo()        
        print "Vo %s succesfully upgraded" % options['vo']

def do_command():    
    if command is None:
        print "No command specified!"
        sys.exit(2)
    
    if command == "install":
        do_install()
        
    elif command == "remove":
        do_remove()
        
    elif command == "upgrade":        
        do_upgrade()


def main():
    
    try:
        print "voms-admin-configure, version %s\n" % voms_admin_server_version
        parse_options()
        vlog("Checking installation...")
        check_installed_setup()
        vlog("Installation ok.")
        
        setup_defaults()
        setup_identity()
        setup_aa_defaults()
        
        vlog("Prefix: %s" % voms_admin_prefix())
        vlog("Configuration dir: %s" % voms_admin_conf_dir())
        
        do_command()
    
    except VomsConfigureError, e:
        print e
        sys.exit(2)    
    

if __name__ == '__main__':
    main()