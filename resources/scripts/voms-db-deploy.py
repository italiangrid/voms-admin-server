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
# 	Andrea Ceccanti (INFN)
#
import os.path,getopt,sys,exceptions,pwd,grp,string,commands,re

from os import environ

from voms import VomsConstants,VomsInvocationError, X509Helper

option_list=["vo=","dn=","ca=","cert=", "email=", "ignore-cert-email"]
supported_commands= ["deploy","undeploy","upgrade","add-admin","remove-admin", "check-connectivity", "grant-read-only-access"]

options={}

command=None

def build_env_vars():
    return ""

def build_classpath():
    
    jars = VomsConstants.voms_admin_libs
    
    if len(jars) == 0:
        raise ValueError, "voms-admin jar files not found!"
    
    jars.append(VomsConstants.voms_admin_jar)
    jars.append(VomsConstants.voms_admin_classes)
    
    return string.join(jars,":") 

def do_deploy():
    cmd = "java %s -cp %s %s --command deploy --vo %s" % (build_env_vars(),
                                                          build_classpath(),
                                                          VomsConstants.schema_deployer_class,
                                                          options['vo'])
    status = os.system(cmd)
    
    sys.exit(os.WEXITSTATUS(status))
    
    
def do_undeploy():
    cmd = "java %s -cp %s %s --command undeploy --vo %s" % (build_env_vars(),
                                                          build_classpath(),
                                                          VomsConstants.schema_deployer_class,
                                                          options['vo'])
    status = os.system(cmd)
    sys.exit(os.WEXITSTATUS(status))

def do_upgrade():
    cmd = "java %s -cp %s %s --command upgrade --vo %s" % (build_env_vars(),
                                                          build_classpath(),
                                                          VomsConstants.schema_deployer_class,
                                                          options['vo'])
    status = os.system(cmd)
    sys.exit(os.WEXITSTATUS(status))

def do_add_admin():
        
    if options.has_key("cert"):
        cert = X509Helper(options['cert'])
        options['dn'] = cert.subject
        options['ca'] = cert.issuer
        
        if not options.has_key("ignore-cert-email"):
            options['email'] = cert.email
    else:
        if not options.has_key("dn"):
            raise VomsInvocationError, "No DN specified for the admin!"
        
        if not options.has_key("ca"):
            raise VomsInvocationError, "No CA specified for the admin!"
        
        if not options.has_key("email"):
            print "WARNING: No email was specified for this administrator! The new administrator will not receive VOMS Admin notifications"
        
        
    cmd = "java %s -cp %s %s --command add-admin --vo %s --dn '%s' --ca '%s'" % (build_env_vars(),
                                                                                 build_classpath(),
                                                                                 VomsConstants.schema_deployer_class,
                                                                                 options['vo'],
                                                                                 options['dn'],
                                                                                 options['ca'])
    if options.has_key("email"):
        cmd += " --email '%s' " % options['email']
    
    status = os.system(cmd)
    sys.exit(os.WEXITSTATUS(status))

def do_remove_admin():
    
    if options.has_key("cert"):
        cert = X509Helper(options['cert'])
        options['dn'] = cert.subject
        options['ca'] = cert.issuer
        ## options['email'] = cert.email
    else:
        if not options.has_key("dn"):
            raise VomsInvocationError, "No DN specified for the admin!"
        
        if not options.has_key("ca"):
            raise VomsInvocationError, "No CA specified for the admin!"
        
        if not options.has_key("email"):
            raise VomsInvocationError, "No email address specified for the admin!"
        
    cmd = "java %s -cp %s %s --command remove-admin --vo %s --dn '%s' --ca '%s' " % (build_env_vars(),
                                                                                     build_classpath(),
                                                                                     VomsConstants.schema_deployer_class,
                                                                                     options['vo'],
                                                                                     options['dn'],
                                                                                     options['ca'])
    status = os.system(cmd)
    sys.exit(os.WEXITSTATUS(status))

def do_check_connectivity():
    cmd = "java %s -cp %s %s --command check-connectivity --vo %s" % (build_env_vars(),
                                                                      build_classpath(),
                                                                      VomsConstants.schema_deployer_class,
                                                                      options['vo'])
    status = os.system(cmd)
    sys.exit(os.WEXITSTATUS(status))

def do_grant_read_only_access():
    cmd = "java %s -cp %s %s --command grant-read-only-access --vo %s" % (build_env_vars(),
                                                                          build_classpath(),
                                                                          VomsConstants.schema_deployer_class,
                                                                          options['vo'])
    status = os.system(cmd)
    sys.exit(os.WEXITSTATUS(status))

def execute():
    global command
    if command == "deploy":
        do_deploy()
    elif command == "undeploy":
        do_undeploy()
    elif command == "upgrade":
        do_upgrade()
    elif command == "add-admin":
        do_add_admin()
    elif command == "remove-admin":
        do_remove_admin()
    elif command == "check-connectivity":
        do_check_connectivity()
    elif command == "grant-read-only-access":
        do_grant_read_only_access()
    else:
        raise VomsInvocationError, "Unknown command specified: %s" % command
    
    
def main():
    try:
        parse_arguments()
        execute()
    except VomsInvocationError, m:
        print m
        usage()
        sys.exit(2)
        
    

def usage():
    help_str = """
Usage:
    
voms-db-deploy.py deploy --vo [VONAME]
voms-db-deploy.py undeploy --vo [VONAME]
voms-db-deploy.py upgrade --vo [VONAME]

voms-db-deploy.py add-admin [--ignore-cert-email] --vo [VONAME] --cert [CERT_FILE]
voms-db-deploy.py add-admin --vo [VONAME] --dn [ADMIN_DN] --ca [ADMIN_CA] --email [EMAILADDRESS]

voms-db-deploy.py remove-admin --vo [VONAME] --cert [CERT_FILE]
voms-db-deploy.py remove-admin --vo [VONAME] --dn [ADMIN_DN] --ca [ADMIN_CA]

voms-db-deploy.py check-connectivity --vo [VONAME]

voms-db-deploy.py grant-read-only-access --vo [VONAME]
"""
    
    print help_str
    
    

def parse_arguments():
    global options,command,supported_commands
    
    if len(sys.argv) == 1:
        usage()
        sys.exit(2)
    
    if re.match("^--",sys.argv[1]):
        cmd_line = sys.argv[1:]
    else:
        command = sys.argv[1]
        cmd_line = sys.argv[2:]
        
    try:

        opts,args = getopt.getopt(cmd_line, "", option_list)
        
        if command is None:
            if len(args) == 0:
                print "No command specified!"
                usage()
                sys.exit(2)
            
            command = args[0]
            
            if command not in supported_commands:
                print "Unknown command specified!"
                usage()
                sys.exit(2)
        
        for key,val in opts:
            options[key[2:]]=val
        
        if not options.has_key("vo"):
            raise VomsInvocationError, "No vo specified!"
        
    except getopt.GetoptError:
        print "Erro parsing command line arguments!"
        usage()
        sys.exit(2)

if __name__ == '__main__':
    main()