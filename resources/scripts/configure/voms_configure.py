#!/usr/bin/env python2.6
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

from optparse import OptionParser, OptionGroup
from voms_shared import voms_version, admin_conf_dir, VOMSDefaults,\
    admin_db_properties_path, admin_service_endpoint_path, vomses_path, lsc_path,\
    aup_path, admin_logging_conf_path, X509Helper, core_conf_dir, voms_conf_path, voms_pass_path,\
    voms_log_path, voms_lib_dir, voms_deploy_database_cmd,\
    voms_ro_auth_clients_cmd, voms_add_admin_cmd, mysql_util_cmd,\
    admin_service_properties_path, voms_undeploy_database_cmd, voms_upgrade_database_cmd

from sys import exit, stdout, stderr
import socket
import logging
import re
import os
import shutil
import pwd
import glob
import time
import string
import random



MYSQL="mysql"
ORACLE="oracle"

usage="""%prog command [options]

Commands:
  install: installs or reconfigures a VO
  upgrade: upgrades a VO
  remove:  removes a VO
"""
logger = None

parser = OptionParser(usage, version="%prog v. " + voms_version())
commands = ["install", "upgrade", "remove"]

HOST_CERT = "/etc/grid-security/hostcert.pem"
HOST_KEY = "/etc/grid-security/hostkey.pem"

VOMS_CERT = "/etc/grid-security/vomscert.pem"
VOMS_KEY = "/etc/grid-security/vomskey.pem"

def execute_cmd(cmd, error_msg=None):
    
    status = os.system(cmd)
    
    if status != 0:
        if not error_msg:
            error_and_exit("Error executing %s" % cmd)
        else:
            error_and_exit(error_msg)

def backup_dir_contents(d):
    logger.debug("Backing up contents for directory: %s", d)
    backup_filez = glob.glob(os.path.join(d,"*_backup_*"))
    
    ## Remove backup filez
    for f in backup_filez:
        ## Don't remove backup directories potentially created by the user
        if not os.path.isdir(f):
            os.remove(f)
    
    filez = glob.glob(os.path.join(d,"*"))
    backup_date = time.strftime("%d-%m-%Y_%H-%M-%S",time.gmtime())
    
    for f in filez:
        os.rename(f, f+"_backup_"+backup_date)

def check_args_and_options(options,args):
    if len(args) != 1 or args[0] not in commands:
        error_and_exit("Please specify a single command among the following:\n\t%s" % "\n\t".join(commands))

def setup_cl_options():
    ## Base options
    parser.add_option("--vo", dest="vo", help="the VO being configured", metavar="VO")
    parser.add_option("--config-owner", dest="config_owner", help="the USER that will own configuration files", metavar="USER", default="voms")
    parser.add_option("--verbose", dest="verbose", action="store_true", help="Be verbose.", default=False)
    parser.add_option("--dry-run", dest="dry_run", action="store_true", help="Dry run execution. No files are touched.", default=False)
    
    parser.add_option("--hostname", dest="hostname", help="the VOMS services HOSTNAME", metavar="HOSTNAME", default=socket.gethostname())
    
    ## Certificate and trust anchors (used for both voms and voms-admin services)
    parser.add_option("--cert", dest="cert", help="the certificate CERT used to run the VOMS services", metavar="CERT", default="/etc/grid-security/hostcert.pem")
    parser.add_option("--key", dest="key", help="the private key used to run the VOMS services", metavar="KEY", default="/etc/grid-security/hostkey.pem")
    parser.add_option("--trust-dir", dest="trust_dir", help="The directory where CA certificates are stored", metavar="DIR", default="/etc/grid-security/certificates")
    parser.add_option("--trust-refresh-period", type="int", dest="trust_refresh_period", help="How ofter CAs are refreshed from the filesystem (in seconds).", metavar="SECS", default=3600)
    
    parser.add_option("--skip-voms-core", dest="skip_voms_core", action="store_true", help="Skips VOMS core configuration", default=False)
    parser.add_option("--skip-voms-admin", dest="skip_voms_admin", action="store_true", help="Skips VOMS admin configuration", default=False)
    parser.add_option("--skip-database", dest="skip_database", action="store_true", help="Skips database operations", default=False)
    parser.add_option("--deploy-database", dest="deploy_database", action="store_true", help="Deploys the database for the VO being configured, if not present", default=True)
    parser.add_option("--undeploy-database", dest="undeploy_database", action="store_true", help="Undeploys the database for the VO being removed", default=False)
    
    # Other base options
    parser.add_option("--openssl", dest="openssl", help="the PATH to the openssl command", metavar="PATH", default="openssl")
    
    ## Admin service options
    admin_opt_group = OptionGroup(parser, "VOMS admin options", "These options drive the basic configuration of the VOMS admin service.")
    admin_opt_group.add_option("--admin-port", dest="admin_port", type="int", help="the PORT on which the admin service will bind", metavar="PORT", default=8443)
    admin_opt_group.add_option("--admin-cert", dest="admin_cert", help="Grants CERT full administrator privileges in the VO", metavar="CERT")
    admin_opt_group.add_option("--read-only", dest="read_only", action="store_true", help="Sets the VOMS admin service as read-only", default=False)
    admin_opt_group.add_option("--disable-ro-access-for-authenticated-clients", 
                               dest="read_only_auth_clients",
                               action="store_false",
                               help="Sets the configured VO as non-browsable by authenticated clients",
                               default="True")
    
    parser.add_option_group(admin_opt_group)
    
    
    ## DB options
    db_opt_group = OptionGroup(parser, "Database configuration options", "These options configure VOMS database access")
    db_opt_group.add_option("--dbtype", dest="dbtype", help="The database TYPE (mysql or oracle)", metavar="TYPE", default=MYSQL)
    db_opt_group.add_option("--dbname", dest="dbname", help="Sets the VOMS database name to DBNAME", metavar="DBNAME")
    db_opt_group.add_option("--dbusername", dest="dbusername", help="Sets the VOMS MySQL username to be created as USER", metavar="USER")
    db_opt_group.add_option("--dbpassword", dest="dbpassword", help="Sets the VOMS MySQL password for the user to be created as PWD", metavar="PWD")
    parser.add_option_group(db_opt_group)
    
    ## Connection pool options
    conn_pool_opt_group = OptionGroup(parser, "Database connection pool options", "These options configure the voms admin service database connection pool")
    conn_pool_opt_group.add_option("--c3p0-acquire-increment",
                            type='int', 
                            dest="c3p0_acquire_increment", 
                            help="Sets the number of new connections that are acquired from the database connection pool is exausted.", 
                            metavar="NUM", 
                            default=1)
    
    conn_pool_opt_group.add_option("--c3p0-idle-test-period",
                                   type='int', 
                                   dest="c3p0_idle_test_period", 
                                   help="Check idle connections in the pool every SEC seconds.", 
                                   metavar="SEC", 
                                   default=0)
    
    conn_pool_opt_group.add_option("--c3p0-min-size",
                                   type='int', 
                                   dest="c3p0_min_size", 
                                   help="Pool minimum size.", 
                                   metavar="NUM", 
                                   default=1)
    
    conn_pool_opt_group.add_option("--c3p0-max-size",
                                   type='int', 
                                   dest="c3p0_max_size", 
                                   help="Pool maximum size.", 
                                   metavar="NUM", 
                                   default=100)
    
    conn_pool_opt_group.add_option("--c3p0-max-statements",
                                   type='int', 
                                   dest="c3p0_max_statements", 
                                   help="The size of the connection pool prepared statements cache.", 
                                   metavar="NUM", 
                                   default=50)
    
    conn_pool_opt_group.add_option("--c3p0-timeout",
                                   type='int', 
                                   dest="c3p0_timeout", 
                                   help="The time in seconds a connection in the pool can remain pooled but unused before being discarded.", 
                                   metavar="SECS", 
                                   default=60)
    
    parser.add_option_group(conn_pool_opt_group)
    
    ## MySQL specifics
    mysql_opt_group = OptionGroup(parser, "MySQL-specific options", "These options are specific for MySQL database backend configuration")
    mysql_opt_group.add_option("--createdb", dest="createdb", action="store_true", help="Creates the MySQL database schema when installing a VO", default=False)
    mysql_opt_group.add_option("--dropdb", dest="dropdb", action="store_true", help="Drops the MySQL database schema when removing a VO", default=False)
    
    mysql_opt_group.add_option("--dbhost",dest="dbhost", help="Sets the HOST where the MySQL database is running", metavar="HOST", default="localhost")
    mysql_opt_group.add_option("--dbport",dest="dbport", type='int', help="Sets the PORT where the MySQL database is listening", metavar="PORT", default="3306")
    mysql_opt_group.add_option("--mysql-command", dest="mysql_command", help="Sets the MySQL command to CMD", metavar="CMD", default="mysql")
    mysql_opt_group.add_option("--dbauser", dest="dbauser", help="Sets MySQL administrator user to USER", metavar="USER", default="root")
    mysql_opt_group.add_option("--dbapwd", dest="dbapwd", help="Sets MySQL administrator password to PWD", metavar="PWD")
    mysql_opt_group.add_option("--dbapwdfile", dest="dbapwdfile", help="Reads MySQL administrator password from FILE", metavar="FILE")
    parser.add_option_group(mysql_opt_group)
    
    ## ORACLE specifics
    oracle_opt_group = OptionGroup(parser, "Oracle-specific options", "These options are specific for Oracle database backend configuration")
    oracle_opt_group.add_option("--use-thin-driver", dest="use_thin_driver", action="store_true", help="Configures the Oracle database using the pure-java native driver", default=False)
    parser.add_option_group(oracle_opt_group)
    
    ## VOMS core specifics
    voms_core_opt_group = OptionGroup(parser, "VOMS core options", "These options drive the configuration of the VOMS core service.")
    voms_core_opt_group.add_option("--core-port", dest="core_port", type="int", help="the PORT on which the VOMS core service will bind", metavar="PORT")
    voms_core_opt_group.add_option("--libdir", dest="libdir", help="the DIR where VOMS core will look for the database plugin modules.", metavar="PORT")
    voms_core_opt_group.add_option("--logdir", dest="logdir", help="the VOMS core log directory DIR", metavar="DIR")
    voms_core_opt_group.add_option("--sqlloc", dest="sqlloc", help="the PATH to the VOMS core database access library", metavar="PATH")
    voms_core_opt_group.add_option("--uri", dest="uri", help="Defines a non-standard the URI of the VOMS server included in the issued attribute certificates", metavar="URI")
    voms_core_opt_group.add_option("--timeout", dest="timeout", type="int", help="Defines the validity of the AC issued by the VOMS server in seconds. The default is 24 hours (86400)", metavar="SECS", default=86400)
    voms_core_opt_group.add_option("--socktimeout", dest="socktimeout", type="int", help="Sets the amount of time in seconds after which the server will drop an inactive connection. The default is 60 seconds", metavar="SECS", default=60)
    voms_core_opt_group.add_option("--shortfqans", dest="shortfqans", action="store_true", help="Configures VOMS to use the short fqans syntax", default=False)
    voms_core_opt_group.add_option("--skip-ca-check", dest="skip_ca_check", action="store_true", help="Configures VOMS to only consider a certificate subject when checking VO user membership", default=False)
    voms_core_opt_group.add_option("--max-reqs", type="int", dest="max_reqs", help="Sets the maximum number of concurrent request that the VOMS service can handle.", default=50)
    parser.add_option_group(voms_core_opt_group)
    
    ## Registration service specifics
    registration_opt_group = OptionGroup(parser, "Registration service options", "These options configure the VOMS Admin registration service")
    registration_opt_group.add_option("--disable-registration", dest="enable_registration", action="store_false", help="Disables registration service for the VO", default=True)
    registration_opt_group.add_option("--aup-url", dest="aup_url", help="Sets a custom URL for the VO AUP.", metavar="URL")
    registration_opt_group.add_option("--aup-signature-grace-period",
                                      type="int",
                                      dest="aup_signature_grace_period",
                                      help="The time (in days) given to users to sign the AUP, after being notified, before being suspended.",
                                      metavar="DAYS",
                                      default="15")
    
    registration_opt_group.add_option("--enable-attribute-requests", dest="enable_attribute_requests", action="store_true", 
                                      help="Enable attribute request at registration time.", default=False)
    
    registration_opt_group.add_option("--membership-request-lifetime", type="int", dest="membership_request_lifetime", 
                                      help="Time (in seconds) that unconfirmed membership request are maintained in the VOMS database.",
                                      metavar="SECS", default=604800)
    
    registration_opt_group.add_option("--disable-membership-expired-requests-warnings", 
                                      action="store_false", 
                                      dest="membership_request_warn_when_expired", 
                                      help="Disables email notifications when unconfirmed membership requests are removed from the voms database.",
                                      default=True)
                                      
    parser.add_option_group(registration_opt_group)
    
    
    ## Membership checks configuration 
    membership_opt_group = OptionGroup(parser, "Membership checks options", "These options configure the VOMS Admin membership checks")
    
    membership_opt_group.add_option("--preserve-expired-members", action="store_true", dest="preserve_expired_members", help="Do not suspend users whose membership has expired.", default=False)
    membership_opt_group.add_option("--disable-membership-end-time", action="store_true", dest="disable_membership_end_time", help="Disable membership end time checks completely.", default=False)
    
    membership_opt_group.add_option("--membership-default-lifetime", type="int", dest="membership_default_lifetime", help="Default VO membership lifetime duration (in months).", metavar="MONTHS", default=12)
    
    membership_opt_group.add_option("--membership-check-period", type="int", dest="membership_check_period", help="The membership check background thread period (in seconds)", metavar="SECS", default=600)
    
    membership_opt_group.add_option("--membership-expiration-warning-period", type="int", dest="membership_expiration_warning_period", 
                                    help="Warning period duration (in days). VOMS Admin will notify of users about to expire in the next number of days expressed by this configuration option.", 
                                    metavar="DAYS", default=30)
    
    membership_opt_group.add_option("--membership-expiration-grace-period", type="int", dest="membership_expiration_grace_period", 
                                    help="Membership expiration grace period (in days). In the grace period user will be maintained active even if membership has expired.", 
                                    metavar="DAYS", default=7)
    
    membership_opt_group.add_option("--membership-notification-resend-period", type="int", dest="membership_notification_resend_period", 
                                    help="Time (in days) that should pass between consecutive warning expiration messages sent to VO administrators to inform about expired and expiring VO members.", 
                                    metavar="DAYS", default=1)
    
    
    saml_opt_group = OptionGroup(parser, "SAML Attribute Authority options", "These options configure the VOMS SAML attribute authority service")
    saml_opt_group.add_option("--enable-saml", dest="enable_saml", action="store_true", help="Turns on the VOMS SAML service.", default=False)
    saml_opt_group.add_option("--saml-lifetime", dest="saml_lifetime", type="int", help="Defines the maximum validity of the SAML assertions issued by the VOMS SAML server in seconds. The default is 24 hours (86400)", metavar="SECS", default=86400)
    saml_opt_group.add_option("--disable-compulsory-group-membership", 
                              action="store_false", 
                              dest="compulsory_group_membership", 
                              help="Disables VOMS compulsory group membership for the SAML AA.", default=True)

    parser.add_option_group(saml_opt_group)
    
    
    x509aa_opt_group = OptionGroup(parser, "X.509 AC Attribute Authority options", "These options configure the VOMS X.509 attribute authority service")
    x509aa_opt_group.add_option("--enable-x509-aa", dest="enable_x509_aa", action="store_true", help="Turns on the X.509 Attribute authority", default=False)
    x509aa_opt_group.add_option("--x509-aa-port", dest="x509_aa_port", 
                                type="int", 
                                help="An additional port used to serve VOMS legacy request.",
                                metavar="PORT", default=-1)
    
    x509aa_opt_group.add_option("--ac-validity", dest="ac_validity", type="int", 
                                help="Defines the maximum validity (in hours) for the attribute certificates issued by this VOMS server. The default is 12 hours",
                                metavar="HOURS", default=24)
    
    x509aa_opt_group.add_option("--disable-legacy-fqan-encoding", 
                                dest="legacy_fqan_encoding", 
                                action="store_false", 
                                help="FQANs will be encoded in issued ACs following the old, deprecated format (i.e. the one including Role=NULL/Capability=NULL).", 
                                default=True)
    
    parser.add_option_group(x509aa_opt_group)
    
    notification_opt_group = OptionGroup(parser, "Notification service options", "These options configure the VOMS Admin notification service")
    notification_opt_group.add_option("--mail-from", dest="mail_from",help="The EMAIL address used for VOMS Admin notification messages.", metavar="EMAIL")
    notification_opt_group.add_option("--smtp-host", dest="smtp_host",help="The HOST where VOMS Admin will deliver notification messages.", metavar="HOST")
    notification_opt_group.add_option("--disable-notification", dest="disable_notification", action="store_true", help=" Turns off the VOMS admin notification service.", default=False)
    notification_opt_group.add_option("--notification-username", dest="notification_username",help="SMTP authentication USERNAME", metavar="USERNAME", default="")
    notification_opt_group.add_option("--notification-password", dest="notification_password",help="SMTP authentication PASSWORD", metavar="PASSWORD", default="")
    notification_opt_group.add_option("--notification-use-tls", action="store_true", dest="notification_use_tls",help="Use TLS to connect to SMTP server", default=False)
    
    parser.add_option_group(notification_opt_group)
            
    other_opt_group = OptionGroup(parser, "Other fancy options", "Configuration options that do not fall in the other categories")
    other_opt_group.add_option("--disable-conf-backup", 
                               dest="enable_conf_backup", 
                               action="store_false", 
                               help="Disables configuration backup creation.",
                               default=True)
    
    other_opt_group.add_option("--mkgridmap-translate-email", 
                               dest="mkgridmap_translate_email", 
                               action="store_true", 
                               help="Generate gridmapfiles containing the email part of user certificate subject as emailAddress besides the Email format used by default.",
                               default=False)
    
    other_opt_group.add_option("--csrf-log-only", 
                               action="store_true", 
                               dest="csrf_log_only", 
                               help="When this option is set, CSRF requests are not blocked but logged. Don't set this option for maximum security", 
                               default=False)
    
    parser.add_option_group(other_opt_group)


   
def configure_logging(options):
    """
    Configures logging so that debug and info messages are routed to stdout and higher level messages are to stderr.
    Debug messages are shown only if verbose option is set
    """
    class InfoAndBelowLoggingFilter(logging.Filter):
        def filter(self,record):
            if record.levelno <= logging.INFO:
                return 1
            return 0
    global logger
    
    out = logging.StreamHandler(stdout)
    err = logging.StreamHandler(stderr)
    
    if options.verbose:
        log_level = logging.DEBUG
    else:
        log_level = logging.INFO
        
    logging.basicConfig(format="%(message)s", level=log_level)
    out.setLevel(log_level)
    out.addFilter(InfoAndBelowLoggingFilter())
    
    err.setLevel(logging.WARNING)
    logger = logging.getLogger("voms-admin")
    logger.addHandler(out)
    logger.addHandler(err)
    logger.propagate=False
    logger.debug("Logging configured") 

def check_required_options(options, required_opts):
    def option_name_from_var(var_name):
        return "--"+re.sub(r'_', '-', var_name)
    
    missing_opts = []
    for o in required_opts:
        if not options.__dict__[o]:
            missing_opts.append(option_name_from_var(o))   
    
    if len(missing_opts) > 0:
        error_and_exit("Please set the following required options:\n\t%s" % '\n\t'.join(missing_opts))
        
    
def check_install_options(options):
    
    if not options.vo:
        error_and_exit("Please set the VO option")
    
    if options.skip_voms_core and options.skip_voms_admin:
        error_and_exit("There's not much to do if --skip-voms-core and --skip-voms-admin are both set!")
    
    required_opts = ["vo", "dbusername", "dbpassword"]
    
    if not options.skip_voms_admin:
        required_opts += ["mail_from",
                          "smtp_host"]
        
    if not options.skip_voms_core:
        required_opts += ["core_port"]
    
    if options.dbtype == ORACLE:
        required_opts += ["dbname"]
    
    check_required_options(options, required_opts)


def check_remove_options(options):
    if not options.vo:
        error_and_exit("Please set the VO option")

def check_upgrade_options(options):
    if not options.vo:
        error_and_exit("Please set the VO option")

def service_cert_sanity_checks(options):
    if not os.path.exists(options.cert):
        error_and_exit("Service certificate %s not found." % options.cert)
    
    if not os.path.exists(options.key):
        error_and_exit("Service private key %s not found." % options.key)
        
    if not os.path.exists(options.trust_dir):
        error_and_exit("Service trust anchor directory %s not found." % options.trust_dir)

def config_owner_ids(options):
    try:
        pwd_info = pwd.getpwnam(options.config_owner)
        return (pwd_info[2], pwd_info[3])
    except KeyError:
        logger.warn("User %s is not configured on this system." % options.config_owner)
        if os.geteuid() == 0:
            error_and_exit("User %s is not configured on this system." % options.config_owner)

def create_voms_service_certificate(options):
    if os.geteuid() == 0 and not options.dry_run:
        logger.info("Creating VOMS services certificate in %s, %s" % (VOMS_CERT, VOMS_KEY))
        shutil.copy(HOST_CERT, VOMS_CERT)
        shutil.copy(HOST_KEY, VOMS_KEY)
        
        (owner_id, owner_group_id) = config_owner_ids(options)
        
        os.chown(VOMS_CERT,owner_id, owner_group_id)
        os.chown(VOMS_KEY,owner_id, owner_group_id)
        
        os.chmod(VOMS_CERT,0644)
        os.chmod(VOMS_KEY,0400)
        options.cert = VOMS_CERT
        options.key = VOMS_KEY
        
def setup_service_certificate(options):
    service_cert_sanity_checks(options)
    if options.cert == HOST_CERT and options.key == HOST_KEY and os.geteuid() == 0:
        create_voms_service_certificate(options)
    
def driver_class(options):
    if options.dbtype == MYSQL:
        return VOMSDefaults.mysql_driver_class
    if options.dbtype == ORACLE:
        return VOMSDefaults.oracle_driver_class

def driver_dialect(options):
    if options.dbtype == MYSQL:
        return VOMSDefaults.mysql_dialect
    else:
        return VOMSDefaults.oracle_dialect

def change_owner_and_set_perms(path, owner_id, group_id, perms):
    if os.geteuid() == 0:
        os.chown(path, owner_id, group_id)
    os.chmod(path, perms)

def write_and_set_permissions(options, path, contents, perms):
    f = open(path, "w")
    f.write(contents)
    f.close()
    os.chmod(path, perms)
    if os.getuid() == 0:
        (owner_id, group_id) = config_owner_ids(options)
        os.chown(path,owner_id,group_id)

def append_and_set_permissions(path, contents, owner_id, group_id, perms):
    f = open(path, "a")
    f.write(contents)
    f.close()
    change_owner_and_set_perms(path, owner_id, group_id, perms)
    
def dburl_mysql(options):
    return "jdbc:mysql://%s:%d/%s" % (options.dbhost,
                                      options.dbport,
                                      options.dbname)

def dburl_oracle(options):
    if options.use_thin_driver:
        return "jdbc:oracle:thin:@//%s:%s/%s" % (options.dbhost,
                                                 options.dbport,
                                                 options.dbname)
    else:
        return "jdbc:oracle:oci:@%s" % (options.dbname)
    
def dburl(options):
    if options.dbtype == MYSQL:
        return dburl_mysql(options)
    else:
        return dburl_oracle(options)

def create_admin_db_properties(options):
    
    db_options = dict(dbdriver=driver_class(options),
                      dbdialect=driver_dialect(options),
                      dburl=dburl(options))
    
    template = string.Template(open(VOMSDefaults.db_props_template,"r").read())
    db_properties = template.substitute(**dict(db_options.items()+options.__dict__.items()))
    
    logger.debug("Admin service database properties:\n%s" % db_properties)
    
    if not options.dry_run:
        write_and_set_permissions(options,
                                  admin_db_properties_path(options.vo), 
                                  db_properties, 
                                  0640)

def create_admin_service_properties(options):
    template = string.Template(open(VOMSDefaults.service_props_template,"r").read())
    service_props = template.substitute(**options.__dict__)
    logger.debug("Admin service properties:\n%s" % service_props)
        
    if not options.dry_run:
        write_and_set_permissions(options,
                                  admin_service_properties_path(options.vo),
                                  service_props, 
                                  0640)

def create_endpoint_info(options):
    
    endpoint_path = admin_service_endpoint_path(options.vo)
    url = "%s:%s" % (options.hostname, options.admin_port)
    logger.debug("Admin service endpoint: %s" % url)
    if not options.dry_run:
        write_and_set_permissions(options, 
                                  endpoint_path, 
                                  url, 
                                  0644)
def create_vomses(options):    
    cert = X509Helper(options.cert, openssl_cmd=options.openssl)
    
    vomses_port = options.core_port
    if vomses_port is None:
        vomses_port = options.x509_aa_port
        
    vomses = '"%s" "%s" "%s" "%s" "%s"\n' % (options.vo,
                                             options.hostname,
                                             vomses_port,
                                             cert.subject,
                                             options.vo)
    
    logger.debug("VOMSES configuration: %s", vomses)
    if not options.dry_run:
        write_and_set_permissions(options, 
                                  vomses_path(options.vo), 
                                  vomses, 
                                  0644)
def create_lsc(options):
    cert = X509Helper(options.cert, openssl_cmd=options.openssl)
    lsc = "%s\n%s" % (cert.subject, cert.issuer)
    logger.debug("LSC configuration: %s", lsc)
    if not options.dry_run:
        write_and_set_permissions(options, 
                                  lsc_path(options.vo), 
                                  lsc, 
                                  0644)
def create_aup(options):
    if not options.dry_run:
        shutil.copyfile(VOMSDefaults.vo_aup_template,aup_path(options.vo))
        if os.geteuid() == 0:
            (owner_id, group_id) = config_owner_ids(options)
            change_owner_and_set_perms(aup_path(options.vo), 
                                       owner_id, 
                                       group_id, 
                                       0644)

def create_logging_configuration(options):
    if not options.dry_run:
        shutil.copyfile(VOMSDefaults.logging_conf_template,admin_logging_conf_path(options.vo))
        if os.geteuid() == 0:
            (owner_id, group_id) = config_owner_ids(options)
            change_owner_and_set_perms(admin_logging_conf_path(options.vo), 
                                       owner_id, 
                                       group_id, 
                                       0644)

def create_admin_configuration(options):
    if os.path.exists(admin_conf_dir(options.vo)):
        logger.info("VOMS Admin service configuration for VO %s exists.", options.vo)
        if not options.dry_run and options.enable_conf_backup:
            backup_dir_contents(admin_conf_dir(options.vo))
    else:
        ## Set the deploy database option if the VO is 
        ## installed for the first time on this host and this
        ## is not meant as a replica
        if not options.skip_database:
            options.deploy_database = True
            # options.createdb = True
        
        ## FIXME: set permissions
        if not options.dry_run:
            os.makedirs(admin_conf_dir(options.vo))    
    
    create_admin_db_properties(options)
    create_admin_service_properties(options)
    create_endpoint_info(options)
    create_vomses(options)
    create_lsc(options)
    create_aup(options)
    create_logging_configuration(options)
    

def create_voms_conf(options):
    
    core_opts = dict(core_logfile=os.path.join(options.logdir, "voms.%s" % options.vo),
                       core_passfile=voms_pass_path(options.vo),
                       core_sqlloc=os.path.join(options.libdir, options.sqlloc))
                       
                       
    template = string.Template(open(VOMSDefaults.voms_template,"r").read())
    all_core_opts = dict(core_opts.items() + options.__dict__.items())
    voms_props = template.substitute(**all_core_opts)
    
    if options.skip_ca_check:
        voms_props+="\n--skipcacheck"
    
    if options.shortfqans:
        voms_props+="\n--shortfqans"
    
    logger.debug("VOMS Core configuration:\n%s" % voms_props)
    if not options.dry_run:
        ## Core configuration
        write_and_set_permissions(options, 
                                  voms_conf_path(options.vo),
                                  voms_props, 
                                  0644)
        ## Core password file
        write_and_set_permissions(options,
                                  voms_pass_path(options.vo),
                                  options.dbpassword+"\n",
                                  0640)
    
    logger.info("VOMS core service configured succesfully.")

def create_core_configuration(options):
    if os.path.exists(core_conf_dir(options.vo)):
        logger.info("VOMS core service configuration for VO %s already exists.", options.vo)
        if not options.dry_run and options.enable_conf_backup:
            backup_dir_contents(core_conf_dir(options.vo))
    else:
        ## FIXME: set permissions
        os.makedirs(core_conf_dir(options.vo))
    
    create_voms_conf(options)
    

def generate_password(length=8, chars=string.ascii_uppercase + string.digits):
    return ''.join(random.choice(chars) for x in range(length))

def setup_core_defaults(options):
    if not options.uri:
        options.uri = "%s:%d" % (options.hostname, options.core_port)
    
    if not options.logdir:
        options.logdir = voms_log_path()
    
    if not options.libdir:
        options.libdir = voms_lib_dir()
    
    if not options.sqlloc:
        if options.dbtype == MYSQL:
            options.sqlloc = "libvomsmysql.so"
        if options.dbtype == ORACLE:
            options.sqlloc = "libvomsoracle.so"
    

def setup_defaults(options):    
    if not options.dbname and options.dbtype == MYSQL:
        options.dbname = "voms_%s" % (re.sub(r"[-.]","_",options.vo))
            
    if not options.dbhost:
        options.dbhost = "localhost"
    
    if not options.dbport:
        
        if options.dbtype == MYSQL:
            options.dbport = 3306
        
        if options.dbtype == ORACLE:
            options.dbport = 1521
    
    if options.createdb or options.dropdb:
        if not options.dbapwd:
            error_and_exit("Please set at least the --dbapwd option when attempting MySQL schema creation/removal.")

def setup_admin_defaults(options):
    
    if not options.aup_url:
        options.aup_url = "file:%s" % aup_path(options.vo)

def create_mysql_db(options):
    createdb_cmd = mysql_util_cmd("create_db", options)
    if not options.dbapwd or len(options.dbapwd) == 0:
        logger.warn("WARNING: No password has been specified for the mysql root account.")
        
    execute_cmd(createdb_cmd, "Error creating MySQL database schema.")
    
def deploy_database(options):
    logger.info("Deploying database for VO %s", options.vo)
    if options.dbtype == MYSQL and options.createdb:
        create_mysql_db(options)
    
    execute_cmd(voms_deploy_database_cmd(options.vo), "Error deploying VOMS database!")
    logger.info("Adding VO administrator reading information from %s", options.cert)
    execute_cmd(voms_add_admin_cmd(options.vo, options.cert, ignore_email=True), "Error adding VO administrator!")
    
    if options.read_only_auth_clients:
        logger.info("Adding read-only access to authenticated clients on the VO.")
        execute_cmd(voms_ro_auth_clients_cmd(options.vo), "Error setting read-only access on the VO!")
    
    if options.admin_cert:
        logger.info("Adding VO administrator reading information from %s", options.admin_cert)
        execute_cmd(voms_add_admin_cmd(options.vo, options.admin_cert), "Error adding VO administrator!")
    
def do_admin_install(options):
    logger.info("Configuring VOMS admin service for vo %s" , options.vo)
    setup_service_certificate(options)
    setup_admin_defaults(options)
    create_admin_configuration(options)
    
    if options.deploy_database:
        deploy_database(options)


def do_core_install(options):
    logger.info("Configuring VOMS core service for vo %s" , options.vo)
    
    if options.skip_voms_admin:
        setup_service_certificate(options)
    setup_core_defaults(options)
    create_core_configuration(options)
    pass

def do_install(options):
    check_install_options(options)
    setup_defaults(options)
    
    if not options.skip_voms_admin:
        do_admin_install(options)
    
    if not options.skip_voms_core:
        do_core_install(options)
    
    logger.info("VO %s configuration completed succesfully.", options.vo)
        

def upgrade_database(options):
    execute_cmd(voms_upgrade_database_cmd(options.vo))
    
  
def undeploy_database(options):
    logger.warning("Undeploying database for VO %s. The database contents will be lost.", options.vo)
    if options.dbtype == MYSQL and options.dropdb:
        execute_cmd(mysql_util_cmd("drop_db", options), "Error dropping MySQL database for VO %s!" % options.vo)
    else:
        execute_cmd(voms_undeploy_database_cmd(options.vo), "Error undeploying VOMS database for VO %s!" % (options.vo))
    
def remove_dir_and_contents(directory):
    logger.info("Removing directory %s and its contents", directory)
    if os.path.exists(directory):
        for i in glob.glob(directory+"/*"):
            logger.debug("Removing %s",i)
            os.remove(i)
            
        os.rmdir(directory)

def do_remove(options):
    check_remove_options(options)
    setup_defaults(options)
    
    if not options.skip_voms_admin:
        if not os.path.exists(admin_conf_dir(options.vo)):
            logger.error("The VOMS Admin service for VO %s is not configured on this host.", options.vo)
        else:
            if options.undeploy_database:
                if not options.skip_database:
                    undeploy_database(options)
                else:
                    logger.warning("Database will not be dropped since --skip-database option is set.")
                    
            logger.info("Removing VOMS Admin service configuration")
            remove_dir_and_contents(admin_conf_dir(options.vo))
    
    if not options.skip_voms_core:
        if not os.path.exists(core_conf_dir(options.vo)):
            logger.error("The VOMS core service for VO %s is not configured on this host.", options.vo)
        else:
            logger.info("Removing VOMS core service configuration")
            remove_dir_and_contents(core_conf_dir(options.vo))
    

def do_upgrade(options):
    check_upgrade_options(options)
    setup_defaults(options)
    
    if not os.path.exists(admin_conf_dir(options.vo)):
        logger.error("The VOMS Admin service for VO %s is not configured on this host.", options.vo)
    else:
        logger.info("Upgrading database for VO %s to the latest version.", 
                    options.vo)    
        upgrade_database(options)    
        logger.info("Upgrade completed successfully.")

def error_and_exit(msg):
    logger.critical(msg)
    exit(1)
        
def main():
    setup_cl_options()
    (options, args) = parser.parse_args()
    configure_logging(options)
    check_args_and_options(options, args)
    command = args[0]
    
    try:
        if command == "install":
            do_install(options)
        elif command == "remove":
            do_remove(options)
        elif command == "upgrade":
            do_upgrade(options)
    except SystemExit, e:
        exit(e)
    except:
        logger.exception("Unexpected error caught!") 

if __name__ == '__main__':
    main()