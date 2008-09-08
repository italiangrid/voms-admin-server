#!/usr/bin/python

#############################################################################
# Copyright (c) Members of the EGEE Collaboration. 2006.
# See http://www.eu-egee.org/partners/ for details on the copyright
# holders.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Authors:
#     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
#############################################################################

import getopt, sys, os, os.path, commands,urllib,xml.dom.minidom, getopt, shutil, time, re, glob

def configured_vos():
    ## Get directories in GLITE_LOCATION_VAR/etc/voms-admin
    conf_dir =  os.path.join(os.environ['GLITE_LOCATION_VAR'],"etc","voms-admin")
    
    return [os.path.basename(v) for v in glob.glob(os.path.join(conf_dir,"*")) if os.path.isdir(v)]

vo = None
command = None
url_opener = None

options = {}

tomcat_prefix = "http://localhost:8080/manager"
 
def parse_siblings_context():
    dom = xml.dom.minidom.parse(open(siblings_context_file(),"r"))
    
    context_el = dom.getElementsByTagName("Context")[0]
    return (context_el.getAttribute("docBase"),
            context_el.getAttribute("path"))
    
def parse_context(vo):
    dom = xml.dom.minidom.parse(open(context_file(vo),"r"))
    
    context_el = dom.getElementsByTagName("Context")[0]
    return (context_el.getAttribute("docBase"),
            context_el.getAttribute("path"))
                           
def setup_cert():
    
    global url_opener
    
    user_id = os.geteuid()
    user_cert = None
    user_key = None
    
    if os.environ.has_key("X509_USER_CERT") and os.environ.has_key("X509_USER_KEY"):
        user_cert = os.environ['X509_USER_CERT']
        user_key = os.environ['X509_USER_KEY']
    elif user_id == 0:
        user_cert = "/etc/grid-security/certificates/hostcert.pem"
        user_key = "/etc/grid-security/certificates/hostkey.pem"
    else:
        user_cert = os.environ['HOME']+"/.globus/usercert.pem"
        user_key = os.environ['HOME']+"/.globus/userkey.pem"
      
    # FIXME: This constructor is not implemented in python 2.2  
    # We need to find another way of setting up the ssl socket for the
    # url opener
    #url_opener = urllib.FancyURLopener(cert_file=user_cert,key_file=user_key)
    url_opener = urllib.FancyURLopener()


def siblings_context_file():
    return os.path.join(os.environ['GLITE_LOCATION_VAR'],"etc","voms-admin", "voms-siblings.xml")    

def context_file(vo):
    
    if options.has_key("context"):
        return os.path.abspath(options['context'])
    else:
        prefix = os.path.join(os.environ['GLITE_LOCATION_VAR'],"etc","voms-admin",vo)
        return "%s/voms-admin-%s.xml" % (prefix,vo)

def catalina_context_file(vo):
    return os.path.join(catalina_conf_dir(),"voms#%s.xml" % vo)

def catalina_siblings_file():
    return os.path.join(catalina_conf_dir(),"vomses.xml")

def context_path(vo):
    return "/voms/"+vo

def catalina_conf_dir():
    if not os.environ.has_key('CATALINA_HOME'):
        raise ValueError, "CATALINA_HOME is not defined!"
    
    return os.path.join(os.environ['CATALINA_HOME'],"conf","Catalina","localhost")

def catalina_webapp_vo_dir(vo):
    if not os.environ.has_key('CATALINA_HOME'):
        raise ValueError, "CATALINA_HOME is not defined!"
    
    return os.path.join(os.environ['CATALINA_HOME'],"webapps","voms",vo)

def remove_siblings_context_file():
    
    siblings_catalina_ctxt = catalina_siblings_file()
    
    if os.path.exists(siblings_catalina_ctxt):
        os.remove(siblings_catalina_ctxt)
    
def remove_siblings_webapp_dir():
    
    if os.path.exists(os.path.join(os.environ['CATALINA_HOME'],"webapps","vomses")):
        shutil.rmtree(os.path.join(os.environ['CATALINA_HOME'],"webapps","vomses"),
                      True)
    
def remove_context_file(vo):
    
    catalina_ctxt = catalina_context_file(vo)
    
    if os.path.exists(catalina_ctxt):
        os.remove(catalina_ctxt)
    
def remove_webapp_dir(vo):
    if os.path.exists(catalina_webapp_vo_dir(vo)):
        shutil.rmtree(catalina_webapp_vo_dir(vo),
                      True)     
        
def usage():
    
    usage_str = """
    Usage:
    init-voms-admin.py [--context=CONTEXT_FILE] [--use-manager] start [VONAME]
    init-voms-admin.py [--use-manager] (stop|reload|status) [VONAME]
    init-voms-admin.py [--use-manager] (start-siblings|stop-siblings)
    
    VONAME is the name of the vo. 
    CONTEXT_FILE is a a file that contains the web application context descriptor
    use-manager uses of the tomcat manager application to manage vo apps.
    
    The start-siblings and stop-siblings commands are used to start/stop the 
    siblings webapp indipendently from other vos.
    
    """
    print usage_str
    
    

def start():
    global options
    
    for v in configured_vos():
        start_vo(v)

    start_siblings()
    
def stop():
    global options

    for v in configured_vos():
        stop_vo(v)
        
    stop_siblings()
    
def start_siblings():
    global options
    
    print "Starting siblings webapp"
    
    if options.has_key('use-manager'):
        docBase, path = parse_siblings_context()
        manager_cmd = "%s/deploy?path=%s&config=file:%s&war=file:%s" % (tomcat_prefix,
                                                                        path,
                                                                        siblings_context_file(),
                                                                        docBase)
        out = url_opener.open(manager_cmd).read()
        print out
    else:
        shutil.copy(siblings_context_file(),
                    catalina_siblings_file())
    
    

def stop_siblings():
    global options
    print "Stopping siblings webapp"
    
    if options.has_key('use-manager'):
        manager_cmd = "%s/undeploy?path=%s" % (tomcat_prefix,"/vomses")
    
        out = url_opener.open(manager_cmd).read()
        print out
    else:
        remove_siblings_context_file()
        remove_siblings_webapp_dir()


def start_vo(vo):
    global options
    print "Starting vo", vo
    
    if options.has_key('use-manager'):

        docBase,path=parse_context(vo)    
    
        manager_cmd = "%s/deploy?path=%s&config=file:%s&war=file:%s" % (tomcat_prefix,
                                                                        path,
                                                                        context_file(vo),
                                                                        docBase)
    
        out = url_opener.open(manager_cmd).read()
        print out
    else:
        
        shutil.copy(context_file(vo), catalina_context_file(vo))
        
    

def reload_vo(vo):
    global options
    
    if options.has_key('use-manager'):
        manager_cmd = "%s/reload?path=%s" % (tomcat_prefix,context_path(vo))
        out = url_opener.open(manager_cmd).read()
        print out
    else:
        remove_context_file(vo)
        remove_webapp_dir()
        time.sleep(10)
        start_vo(vo)
      
    
    
def stop_vo(vo):
    global options
    print "Stopping vo", vo
    if options.has_key('use-manager'):
        manager_cmd = "%s/undeploy?path=%s" % (tomcat_prefix,context_path(vo))
    
        out = url_opener.open(manager_cmd).read()
        print out
    else:
        remove_context_file(vo)        
        remove_webapp_dir(vo)
        

def status():
    global options
    
    ret_status = 0
    
    for v in configured_vos():
        if status_vo(v) == 1:
            ret_status = 1
    
    sys.exit(ret_status)
    
def status_vo(vo):
    global options
    
    if os.path.exists(catalina_context_file(vo)):
        print "The '%s' vo is currently ACTIVE." % vo
        return 0
    else:
        print "The '%s' vo is currently INACTIVE." % vo
        return 1
    
        
def parse_cmd_line():   
    global command, vo, options, use_manager
    
    if len(sys.argv) == 1:
        print "No command given!"
        usage()
        sys.exit(2)
    
    
    cmd_line = sys.argv[1:]
    
    try:
        
        opts,args = getopt.getopt(cmd_line,"",["context=","use-manager"]) 
                
        for k,v in opts:
            options[k[2:]]=v
        
        command=args[0]
        
        if len(args) > 1:
            vo= args[1]
        
    except getopt.GetoptError, m:
        print "Error parsing command line arguments!", m
        usage()
        sys.exit(1)
        


def main():
    parse_cmd_line()
    setup_cert()
    
    if command == "start":
        if vo is None:
            start()
        else:
            start_vo(vo)
            
    elif command == "stop":
        if vo is None:
            stop()
        else:
            stop_vo(vo)
            
    elif command == "reload":
        if vo is None:
            raise RuntimeError, "Please specify the vo that needs to be reloaded!"
        
        reload_vo(vo)
    
    elif command == "status":
        if vo is None:
            status()
        else:
            status_vo(vo)
    
    elif command == "start-siblings":
        start_siblings()
        
    elif command == "stop-siblings":
        stop_siblings()
        
    else:
        print "Unknown command!"
        usage()

if __name__ == '__main__':
    main()
