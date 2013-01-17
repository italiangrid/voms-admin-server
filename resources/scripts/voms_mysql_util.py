#!/usr/bin/python
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

from optparse import OptionParser
from sys import stderr,exit
import subprocess,re, string, socket

usage = """%prog [options] command

Commands:
  create_db:        creates a MySQL database and read/write grants for the VOMS service 
                    based on the given options
  drop_db:          drops a MySQL database
  grant_rw_access:  Creates a read/write grant on an existing VOMS database for the user
                    specified in the options
  grant_ro_access:  Creates a read-only grant on an existing VOMS database for the user 
                    specified in the options 
"""
parser = OptionParser(usage=usage)

def setup_cl_options():
    parser.add_option("--dbauser", dest="dbauser", help="Sets MySQL administrator user to USER", metavar="USER", default="root")
    parser.add_option("--dbapwd", dest="dbapwd", help="Sets MySQL administrator password to PWD", metavar="PWD")
    parser.add_option("--dbapwdfile", dest="dbapwdfile", help="Reads MySQL administrator password from FILE", metavar="FILE")
    parser.add_option("--dbusername", dest="username", help="Sets the VOMS MySQL username to be created as USER", metavar="USER")
    parser.add_option("--dbpassword", dest="password", help="Sets the VOMS MySQL password for the user to be created as PWD", metavar="PWD")
    parser.add_option("--dbname", dest="dbname", help="Sets the VOMS database name to DBNAME", metavar="DBNAME")
    parser.add_option("--dbhost",dest="host", help="Sets the HOST where MySQL is running", metavar="HOST", default="localhost")
    parser.add_option("--dbport",dest="port", help="Sets the PORT where MySQL is listening", metavar="PORT", default="3306")
    parser.add_option("--mysql-command", dest="command", help="Sets the MySQL command to CMD", metavar="CMD", default="mysql")

def error_and_exit(msg):
    print >>stderr, msg
    exit(1)
    
def build_mysql_command_preamble(options):
    if options.dbapwdfile:
        try:
            dbapwd = open(options.dbapwdfile).read()
        except IOError as e:
            error_and_exit(e.strerror)
    else:
        dbapwd = options.dbapwd
    
    if not dbapwd:
        mysql_cmd = "%s -u%s --host %s --port %s" % (options.command,
                                                     options.dbauser,
                                                     options.host,
                                                     options.port)
    else:
        mysql_cmd = "%s -u%s -p%s --host %s --port %s" % (options.command,
                                                          options.dbauser,
                                                          dbapwd,
                                                          options.host,
                                                          options.port)
    return mysql_cmd

 

def db_exists(options):
    mysql_cmd = build_mysql_command_preamble(options)
    
    mysql_proc = subprocess.Popen(mysql_cmd, shell=True, stdin=subprocess.PIPE, stderr=subprocess.PIPE)
    try:
        print >>mysql_proc.stdin, "use %s;" % options.dbname
        mysql_proc.stdin.close()
    except IOError as e:
        err_msg = mysql_proc.stderr.read()
        error_and_exit("Error checking database existence: %s. %s" % (e, err_msg))
            
    status = mysql_proc.wait()
    if status == 0:
        return True
    else:
        err_msg = mysql_proc.stderr.read()
        match = re.match("ERROR 1049", string.strip(err_msg))
        if match:
            return False
        else:
            error_and_exit("Error checking schema existence: %s" % err_msg)
        
def create_db(options):
    print "Creating database %s" % options.dbname
    
    if db_exists(options):
        print "Schema for database %s already exists, will not create it..." % options.dbname
    else:
        mysql_cmd = build_mysql_command_preamble(options)
        ## The database is not there, let's create it
        mysql_proc = subprocess.Popen(mysql_cmd, shell=True, stdin=subprocess.PIPE)
        print >>mysql_proc.stdin, "create database %s;" % options.dbname
        mysql_proc.stdin.close()
        status = mysql_proc.wait()
        if status != 0:
            error_and_exit("Error creating MySQL database %s: %s" % (options.dbname, mysql_proc.stdout.read()))
        
    grant_rw_access(options)
    
    print "Done."

def drop_db(options):
    print "Dropping database %s" % options.dbname
    if not db_exists(options):
        print "Schema for database %s does not exist, exiting..." % options.dbname
        exit(1)
    else:
        mysql_cmd = build_mysql_command_preamble(options)
        mysql_proc = subprocess.Popen(mysql_cmd, shell=True, stdin=subprocess.PIPE)
        print >>mysql_proc.stdin, "drop database %s;" % options.dbname
        mysql_proc.stdin.close()
        status = mysql_proc.wait()
        if status != 0:
            error_and_exit("Error dropping MySQL database %s: %s" % (options.dbname, mysql_proc.stdout.read()))
    print "Done."

def grant_rw_access(options):
    print "Granting user %s read/write access on database %s" % (options.username, options.dbname)
    mysql_cmd = build_mysql_command_preamble(options)
    
    if len(options.username) > 16:
        error_and_exit("MySQL database accont names cannot be longer than 16 characters.") 
    
    if db_exists(options):
        mysql_proc = subprocess.Popen(mysql_cmd, shell=True, stdin=subprocess.PIPE)
        for host in ['localhost','localhost.%',socket.gethostname(),socket.getfqdn()]:
            print >>mysql_proc.stdin, "grant all privileges on %s.* to '%s'@'%s' identified by '%s' with grant option;" % (options.dbname,
                                                                                                                       options.username,
                                                                                                                       host,
                                                                                                                       options.password)
        print >>mysql_proc.stdin, "flush privileges;"
        mysql_proc.stdin.close()
        status = mysql_proc.wait()
        if status != 0:
            error_and_exit("Error granting read/write access to user %s on database %s: %s" % (options.username, 
                                                                                               options.dbname,
                                                                                               mysql_proc.stdout.read()))

def grant_ro_access():
    print "Still unimplemented"

supported_commands = {'create_db': create_db, 
                      'drop_db': drop_db, 
                      'grant_rw_access': grant_rw_access, 
                      'grant_ro_access': grant_ro_access}

required_options = [ "username", "password", "dbname"]

def check_mysql_command(options):
    test_cmd = "%s --version" % options.command
    proc = subprocess.Popen(test_cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    (out, err) = proc.communicate()
    
    combined_output = "%s %s" % (out, err)
    
    status = proc.wait()
    
    if status != 0:
        error_and_exit("Error executing %s: %s. Check your MySQL client installation." % (options.command, combined_output.strip()))
    
def check_args_and_options(options, args):
    if len(args) != 1 or args[0] not in supported_commands.keys():
        error_and_exit("Please specify a single command among the following:\n\t%s" % "\n\t".join(supported_commands.keys()))
        
    missing_options = []
    
    if not options.username:
        missing_options.append("--dbusername") 
    if not options.password:
        missing_options.append("--dbpassword")
    if not options.dbname:
        missing_options.append("--dbname")
    
    if len(missing_options) != 0:
        error_and_exit("Please specify the following missing options:\n\t%s" % "\n\t".join(missing_options))
    
def main():
    setup_cl_options()
    (options, args) = parser.parse_args()
    check_args_and_options(options,args)
    check_mysql_command(options)
    
    supported_commands[args[0]](options)
    
if __name__ == '__main__':
    main()