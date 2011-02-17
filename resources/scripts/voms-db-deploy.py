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
import os.path,getopt,sys,exceptions,pwd,grp,string,commands

from os import environ

from voms import *

option_list=["vo=","dn=","ca=","cert=", "email="]
supported_commands= ["deploy","undeploy","upgrade","add-admin","remove-admin", "check-connectivity"]

options={}

command=None

def build_classpath():
    
    jars = VomsConstants.voms_admin_libs
    
    if len(jars) == 0:
        raise ValueError, "voms-admin jar files not found!"
    
    jars.append(VomsConstants.voms_admin_jar)
    jars.append(VomsConstants.voms_admin_classes)
    
    return string.join(jars,":")


def build_env_vars():
    env_vars = string.join(map(lambda x: "-D%s=%s" % (x, os.environ[x]), VomsConstants.env_variables)," ") 
    return env_vars 
    

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
        options['email'] = cert.email
    else:
        if not options.has_key("dn"):
            raise VomsInvocationError, "No DN specified for the admin!"
        
        if not options.has_key("ca"):
            raise VomsInvocationError, "No CA specified for the admin!"
        
        if not options.has_key("email"):
            raise VomsInvocationError, "No email address specified for the admin!"
        
    cmd = "java %s -cp %s %s --command add-admin --vo %s --dn '%s' --ca '%s' --email '%s'" % (build_env_vars(),
                                                                                 build_classpath(),
                                                                                 VomsConstants.schema_deployer_class,
                                                                                 options['vo'],
                                                                                 options['dn'],
                                                                                 options['ca'],
                                                                                 options['email'])
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
    else:
        raise VomsInvocationError, "Unknown command specified: %s" % command
    
    
def check_env_var():
    for i in VomsConstants.env_variables:
        if environ.get(i, None) is None:
            raise VomsConfigureError, i+" environment variable is not set" 


def main():
    check_env_var()
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

voms-db-deploy.py add-admin --vo [VONAME] --cert [CERT_FILE]
voms-db-deploy.py add-admin --vo [VONAME] --dn [ADMIN_DN] --ca [ADMIN_CA] --email [EMAILADDRESS]

voms-db-deploy.py remove-admin --vo [VONAME] --cert [CERT_FILE]
voms-db-deploy.py remove-admin --vo [VONAME] --dn [ADMIN_DN] --ca [ADMIN_CA]

voms-db-deploy.py check-connectivity --vo [VONAME]
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