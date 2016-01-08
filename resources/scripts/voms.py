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
#

import commands
import exceptions
import glob
import os.path
import platform
import re
import shutil
import socket
import string
import subprocess
import time

voms_admin_server_version = "${server-version}"
package_prefix= "${package.prefix}"

class PropertyHelper(dict):
    empty_or_comment_lines = re.compile("^\\s*$|^#.*$")
    property_matcher = re.compile("^\\s*([^=\\s]+)=?\\s*(\\S.*)$")

    
    def __init__(self,filename):
        self._filename = filename
        self._load_properties()
    
    def _load_properties(self):
        f = open(self._filename,"r")
        for l in f:
            if re.match(PropertyHelper.empty_or_comment_lines, l) is None:
                m = re.search(PropertyHelper.property_matcher,l) 
                if m:
                    PropertyHelper.__setitem__(self,m.groups()[0],m.groups()[1])
        f.close()
    
    def save_properties(self):
        def helper(l):
            m = re.search(PropertyHelper.property_matcher,l) 
            if m:
                return re.sub("=.*$","=%s" % self[m.groups()[0]],l)
            else:
                return l
        
        f = open(self._filename,"rw+")
        lines = map(helper,f.readlines())
        f.seek(0)
        f.writelines(lines)
        f.truncate()
        f.close()

def load_sysconfig():     
    return PropertyHelper("%s/etc/sysconfig/voms-admin" % package_prefix )

voms_admin_sysconfig_props = load_sysconfig()

def exit_status(status):
    ## FIXME: No op
    return status

def setup_permissions(f,perms,tomcat_group):
    
    if os.getgid() == 0:
        os.chown(f, 0, tomcat_group)
    
    os.chmod(f,perms)

def configured_vos():
    conf_dir =  VomsConstants.voms_admin_conf_dir
    return [os.path.basename(v) for v in glob.glob(os.path.join(conf_dir,"*")) if os.path.isdir(v)]

def is_oracle_vo(vo):
    if not vo_config_dir_exists(vo):
        raise VomsConfigureError, "VO %s is not configured on this host!" % vo
    db_props = vo_database_properties(vo)
    
    return db_props['hibernate.dialect'] == VomsConstants.oracle_dialect

def vo_config_dir(vo):
    return os.path.join(VomsConstants.voms_admin_conf_dir,vo)

def voms_config_dir(vo):
    return os.path.join(VomsConstants.voms_conf_dir,vo)

def voms_config_file(vo):
    return os.path.join(VomsConstants.voms_conf_dir,vo,"voms.conf")

def voms_pass_file(vo):
    return os.path.join(VomsConstants.voms_conf_dir,vo,"voms.pass")

def voms_config_dir_exists(vo):
    return os.path.exists(voms_config_dir(vo))

def vo_config_dir_exists(vo):
    return os.path.exists(vo_config_dir(vo))

def vo_dababase_properties_file(vo):
    return os.path.join(vo_config_dir(vo),"voms.database.properties")

def vo_database_properties(vo):
    f = os.path.join(vo_config_dir(vo),"voms.database.properties")
    return PropertyHelper(f)

def vo_service_properties_file(vo):
    return os.path.join(vo_config_dir(vo),"voms.service.properties")

def vo_service_properties(vo):
    f = os.path.join(vo_config_dir(vo),"voms.service.properties")
    return PropertyHelper(f)

def vo_context_file(vo):
    return os.path.join(vo_config_dir(vo),"voms-admin-%s.xml"%vo)

def vomses_file(vo):
    return os.path.join(vo_config_dir(vo),"vomses")

def set_default(d,key,value):
    if not d.has_key(key):
        d[key]=value
"""
This methods returns a tuple containing host, port, database name 
extracted from the mysql connection string"""
def mysql_database_connection_properties(vo):
    dbProps = vo_database_properties(vo)
    if dbProps.has_key('jdbc.URL'):
        ## voms 1.2.x property
        url = dbProps['jdbc.URL']
    else:
        url = dbProps['hibernate.connection.url']
    
    matcher = re.compile("jdbc:mysql:\/\/([^:]+):(\d+)\/(.+)$")
    
    if not re.match(matcher,url):
        raise VomsConfigureError, "'%s' found in configuration is not a mysql connection URL!" % url
    
    return re.search(matcher,url).groups()
    
    
    
def backup_dir_contents(d):
    
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
    
class VomsConfigureError(exceptions.RuntimeError):   
    pass

class VomsInvocationError(exceptions.RuntimeError):
    pass

class ConfigureAction:
    def __init__(self,name,required_options,user_options):
        self.name = name
        self.required_options=required_options
        self.user_options=user_options
        
        ## Special effects options handling (implemented here since needed both for install and upgrade methods
        ## and I don't want replicated code around)
        self.set_ca_files()
        self.set_webui_requests_enabled()
    
    
    def name(self,name=None):
        if name is None:
            return self.name
        self.name=name

    def required_options(self,options=None):
        
        if options is None:
            return self.required_options
        self.required_options = options
        
    def check_options(self):
        
        missing_options = []
        for i in self.required_options:
            if not self.user_options.has_key(i):
                missing_options.append(i)
        
        if len(missing_options) != 0:
            raise VomsConfigureError, "Please set the following required options: " + string.join(missing_options,",")  
            
    def write_and_close(self, filename, content):
        
        f = open(filename,"w")
        f.write(content)
        f.close()
        
    def append_and_close(self,filename, content):
        f = open(filename,"a")
        f.write(content)
        f.close()
        
    def set_ca_files(self):
        ca_files = "/etc/grid-security/certificates/*.0"
        
        if os.environ.has_key('X509_CERT_DIR'):
            ca_files = os.path.join(os.environ['X509_CERT_DIR'], '*.0')
        
        ## Ovveride X509_CERT_DIR here
        if self.user_options.has_key('certdir'):
            ca_files = os.path.join(self.user_options['certdir'],'*.0')
        
        self.user_options['ca-files'] = ca_files
    
    def set_webui_requests_enabled(self):
        if self.user_options.has_key('disable-webui-requests'):
            self.user_options['webui-enabled'] = 'false'
        else:
            self.user_options['webui-enabled'] = 'true'
    
    def vlog(self, msg):
        if self.user_options.has_key('verbose') :
            print msg  
        
    

class UpgradeVO(ConfigureAction):
    def __init__(self,user_options):
        ConfigureAction.__init__(self, "upgrade_vo", ["vo"],user_options)
    
    def upgrade_vo(self):
        
        set_default(self.user_options,"vo-aup-url", "file://%s/vo-aup.txt" % (vo_config_dir(self.user_options['vo'])))
                                                                                                  
        print "Upgrading vo ",self.user_options['vo']
        
        self.upgrade_configuration()
        
        if self.user_options.has_key('skip-database'):
            print "Skipping database upgrade as requested by user (--skip-database option)."
        else:
            self.upgrade_database()
    
    def upgrade_configuration_1_2_19(self):
        db_props = vo_database_properties(self.user_options['vo'])
        self.jdbc_url = db_props['jdbc.URL']
        
        if re.match("^jdbc:oracle",db_props['jdbc.URL']):
            ## Oracle vo
            self.driver_class = VomsConstants.oracle_driver_class
            self.dialect = VomsConstants.oracle_dialect    
        else:
            ## Mysql vo
            self.driver_class = VomsConstants.mysql_driver_class
            self.dialect = VomsConstants.mysql_dialect
        
        
        self.dbusername = db_props['voms.database.username']
        self.dbpassword = db_props['voms.database.password']
        
        ## save old db_props
        os.rename(vo_dababase_properties_file(self.user_options['vo']),
                  vo_dababase_properties_file(self.user_options['vo'])+".old")
        
        self.write_database_properties()
        
        service_props = vo_service_properties(self.user_options['vo'])
        
        self.mail_from = service_props['mail.smtp.from']
        self.mail_host = service_props['mail.smtp.host']
        
        ## save old service_props
        os.rename(vo_service_properties_file(self.user_options['vo']),
                  vo_service_properties_file(self.user_options['vo'])+".old")
        
        self.write_service_properties()
        
        ## salve old context
        os.rename(vo_context_file(self.user_options['vo']),
                  vo_context_file(self.user_options['vo'])+".old")
        
        self.write_context_file()
        self.write_siblings_context()
        
        pass
    
    def upgrade_configuration_2_0_x(self):
        
        vo_conf_dir = vo_config_dir(self.user_options['vo'])
        
        ## Deploy AUP template
        shutil.copy(VomsConstants.vo_aup_template, vo_conf_dir)
        
        setup_permissions(os.path.join(vo_conf_dir, os.path.basename(VomsConstants.vo_aup_template)), 
                          0644, 
                          self.user_options['tomcat-group-id'])
        
    
    def upgrade_configuration(self):
        if not vo_config_dir(self.user_options['vo']):
            raise VomsConfigureError, "Configuration directory not found for vo ", self.user_options['vo']
        
        db_props = vo_database_properties(self.user_options['vo'])
        
        if db_props.has_key('jdbc.URL'):
        
            self.upgrade_configuration_1_2_19()
        
        elif db_props.has_key('hibernate.dialect'):
            
            self.upgrade_configuration_2_0_x()
        
        else:
            raise VomsConfigureError, "Unrecognized voms database configuration, upgrade failed!"
        
        ## common upgrade configuration behaviour
        
        ## Put logging configuration in place
        shutil.copy(VomsConstants.logging_conf_template, vo_config_dir(self.user_options['vo']))
        setup_permissions(os.path.join(vo_config_dir(self.user_options['vo']), os.path.basename(VomsConstants.logging_conf_template)), 
                          0644, 
                          self.user_options['tomcat-group-id'])
        
    def write_database_properties(self):
        m = {'DRIVER_CLASS':self.driver_class,
             'DIALECT':self.dialect,
             'USERNAME':self.dbusername,
             'PASSWORD':self.dbpassword,
             'JDBC_URL': self.jdbc_url
             }
     
        t = Template(open(VomsConstants.db_props_template,"r").read())
        
        db_props_file = open(vo_dababase_properties_file(self.user_options['vo']),"w")
        db_props_file.write(t.sub(m))
        db_props_file.close()

    
    def write_service_properties(self):
                
        m = {'NOTIFICATION.EMAIL_ADDRESS': self.mail_from,
             'NOTIFICATION.SMTP_SERVER': self.mail_host,
             'WEBUI.ENABLED': self.user_options['webui-enabled'],
             'CA.FILES': self.user_options['ca-files'],
             'READ_ACCESS' : str(self.user_options.has_key('read-access-for-authenticated-clients')).lower(),
             'VO_AUP_URL': self.user_options['vo-aup-url'],
             'ADMIN_SKIP_CA_CHECK' : str(self.user_options.has_key('skip-ca-check')).lower(),
             'AA.CERT' : self.user_options['aa-cert'],
             'AA.KEY' : self.user_options['aa-key'],
             'SAML.MAX_ASSERTION_LIFETIME' : self.user_options['saml-max-assertion-lifetime'],
             'HOSTNAME' : self.user_options['hostname']
             }
        
        t = Template(open(VomsConstants.service_props_template,"r").read())
        
        service_props_file = open(vo_service_properties_file(self.user_options['vo']),"w")
        service_props_file.write(t.sub(m))
        service_props_file.close()
        
    def write_context_file(self):
        war_file = VomsConstants.voms_admin_war
        
        if self.user_options.has_key('use-skinny-war'):
            war_file = VomsConstants.voms_admin_war_nodeps
        
        m = {'VO_NAME': self.user_options['vo'], 'WAR_FILE': war_file} 
             
        t = Template(open(VomsConstants.context_template,"r").read())
        
        context_file = open(vo_context_file(self.user_options['vo']),"w")
        context_file.write(t.sub(m))
        context_file.close()
    
    def write_siblings_context(self):
        m = {'WAR_FILE': VomsConstants.voms_siblings_war }
        
        t = Template(open(VomsConstants.voms_siblings_context_template,"r").read())
        ConfigureAction.write_and_close(self, 
                                        VomsConstants.voms_siblings_context, 
                                        t.sub(m))
        
        setup_permissions(VomsConstants.voms_siblings_context, 
                          0644, 
                          self.user_options['tomcat-group-id'])
    
    def upgrade_database(self):
        upgrade_cmd = "%s  upgrade --vo %s" % (VomsConstants.voms_db_deploy,self.user_options['vo'])
        
        status = os.system(upgrade_cmd)
        
        if status != 0:
            raise VomsConfigureError, "Error upgrading voms database!"
        
        print "Database upgraded"
        
        
class RemoveVOAction(ConfigureAction):
    def __init__(self,user_options):
        ConfigureAction.__init__(self, "remove_vo", ["vo"],user_options)
        
    
    def remove_configuration(self):
        
        ## Removing voms-admin configuration
        for i in glob.glob(vo_config_dir(self.user_options['vo'])+"/*"):
            os.remove(i)
        os.removedirs(vo_config_dir(self.user_options['vo']))
        
        ## Removing voms configuration
        if not self.user_options.has_key('skip-voms-core'):
            if voms_config_dir_exists(self.user_options['vo']):
                for i in glob.glob(voms_config_dir(self.user_options['vo'])+"/*"):
                    os.remove(i)
                            
                os.removedirs(voms_config_dir(self.user_options['vo']))
        else:
            print "Skipping removal of voms core configuration..."
    
    def remove_vo(self):
        
        if not vo_config_dir_exists(self.user_options['vo']):
            raise VomsConfigureError, "Vo %s not configured on this host!" % self.user_options['vo']
                   
        if self.user_options.has_key('undeploy-database'):
            self.undeploy_db()
        
        self.remove_configuration()         
        
    
    def undeploy_db(self):
        
        deploy_cmd = "%s  undeploy --vo %s" % (VomsConstants.voms_db_deploy,self.user_options['vo'])
        
        status = os.system(deploy_cmd)
        
        if status != 0:
            raise VomsConfigureError, "Error undeploying voms database!"
    
class RemoveMySQLVO(RemoveVOAction):
    def __init__(self,user_options):
        
        RemoveVOAction.__init__(self,user_options)
        
        (host,port,dbname) = mysql_database_connection_properties(self.user_options['vo'])
        
        # set_default(self.user_options,"dbname","voms_"+self.user_options['vo'])
        set_default(self.user_options,"dbname",dbname)
        
        # set_default(self.user_options,'dbhost','localhost')
        set_default(self.user_options,'dbhost',host)
        
        # set_default(self.user_options,'dbport','3306')
        set_default(self.user_options,'dbport', port)
        
        self.name = "remove_mysql_vo"
        
    def check_options(self):
        if self.user_options.has_key('dropdb'):
            
            self.required_options += ['dbauser']
        
        RemoveVOAction.check_options(self)
    
    def undeploy_db(self):
        
        # Dropping the database will drop the db content
        if self.user_options.has_key('dropdb'):
                        
            print "Dropping mysql db..."
            
            if self.user_options.has_key('dbapwdfile'):
                dbapwd = open(self.user_options['dbapwdfile']).read()
                self.user_options['dbapwd'] = dbapwd
            
            ## Support for mysql admin empty password (VDT request).
            if (not self.user_options.has_key('dbapwd')) or len(self.user_options['dbapwd']) == 0:
                print "WARNING: No password has been specified for the mysql root account! I will continue the db deployment assuming no password has been set for such account."
                mysql_cmd = "%s -u%s --host %s --port %s" % (self.user_options['mysql-command'],
                                         self.user_options['dbauser'],
                                         self.user_options['dbhost'],
                                         self.user_options['dbport'])
            else:
                mysql_cmd = "%s -u%s -p%s --host %s --port %s" % (self.user_options['mysql-command'],
                                              self.user_options['dbauser'],
                                              self.user_options['dbapwd'],
                                              self.user_options['dbhost'],
                                              self.user_options['dbport'])
                
                
            mysql_proc = subprocess.Popen(mysql_cmd, shell=True, stdin=subprocess.PIPE, stderr=subprocess.PIPE)
                        
            ## Drop database 
            print >>mysql_proc.stdin, "drop database %s;" % self.user_options['dbname']
            mysql_proc.stdin.close()
            
            status = exit_status(mysql_proc.wait())
            
            if status != 0:
                err_msg = mysql_proc.stderr.read()
                raise VomsConfigureError, "Error dropping mysql database! "+err_msg
        else:
            RemoveVOAction.undeploy_db(self)
            

class RemoveOracleVO(RemoveVOAction):
    def __init__(self,user_options):
        RemoveVOAction.__init__(self,user_options)
        self.name = "remove_oracle_vo"
        

class InstallVOAction(ConfigureAction):
    def __init__(self,user_options):
        ConfigureAction.__init__(self, "install_vo", 
                                 ["vo",
                                  "port", 
                                  "dbtype", 
                                  "dbusername", 
                                  "dbpassword", 
                                  "mail-from", 
                                  "smtp-host"
                                  ],  
                                 user_options)
        
        
    def install_vo(self):
        set_default(self.user_options,"code",self.user_options['port'])
                         
        set_default(self.user_options,"uri","%s:%s" % (socket.gethostname(),self.user_options['port']))
        
        set_default(self.user_options,"timeout", "86400")
        
        set_default(self.user_options,"vo-aup-url", "file://%s/vo-aup.txt" % (vo_config_dir(self.user_options['vo'])))
                
        if self.user_options['vo'] == 'siblings':
            raise VomsConfigureError, "Cannot create a vo named siblings, that name is reserved!"
        
        self.create_configuration()
        
        if self.user_options.has_key("deploy-database"):
            ## Don't assume the user knows what he does
            if self.user_options.has_key("skip-database"):
                print """Will not deploy the database (even if the --deploy-database option was set)
                since the --skip-database option is set!"""
            else:
                self.deploy_db()
        
        if self.user_options.has_key("admincert"):
            if self.user_options.has_key("skip-database"):
                print "Will not add the admin since the --skip-database option is set"
            else: 
                self.add_default_admin()
        
        if self.user_options.has_key("read-access-for-authenticated-clients"):
            if self.user_options.has_key("skip-database"):
                print "Will not set read-only access for authenticated clients as the --skip-database option is set"
            else:
                self.enable_readonly_access()

        
    def create_configuration(self):    
        vo_conf_dir = vo_config_dir(self.user_options['vo'])
        voms_conf_dir = voms_config_dir(self.user_options['vo'])               
        
        
        if os.path.exists(vo_conf_dir):
            print "VO "+self.user_options['vo']+" is already configured on this host, will overwrite the configuration..."
            backup_dir_contents(vo_conf_dir)
            
        else:                       
            ## Set the deploy database option if the VO is 
            ## installed for the first time on this host and this
            ## is not meant as a replica
            if not self.user_options.has_key("skip-database"):
                self.user_options['deploy-database'] = True
                if isinstance(self, InstallMySqlVO):
                    self.user_options['createdb'] = True
                self.check_options()
            
            os.makedirs(vo_conf_dir)
        
        if not self.user_options.has_key("skip-voms-core"):
            if not os.path.exists(voms_conf_dir):
                os.makedirs(voms_conf_dir)
            else:
                backup_dir_contents(voms_conf_dir)
        
        self.write_database_properties()
        self.write_service_properties()
        self.write_context()
        
        shutil.copy(VomsConstants.vo_aup_template, vo_conf_dir)
        
        setup_permissions(os.path.join(vo_conf_dir, os.path.basename(VomsConstants.vo_aup_template)), 
                          0644, 
                          self.user_options['tomcat-group-id'])
        
        if not os.path.exists(VomsConstants.voms_siblings_context):
            self.write_siblings_context()
        
        self.write_vomses()
        
        if not self.user_options.has_key("skip-voms-core"):
            self.write_voms_properties()
        else:
            print "Skipping voms core configuration creation"
            
        ## Put logging configuration in place
        shutil.copy(VomsConstants.logging_conf_template, vo_conf_dir)
        setup_permissions(os.path.join(vo_conf_dir, os.path.basename(VomsConstants.logging_conf_template)), 
                          0644, 
                          self.user_options['tomcat-group-id'])
    
    def write_database_properties(self):
        m = {'DRIVER_CLASS':self.driver_class,
             'DIALECT':self.dialect,
             'USERNAME':self.user_options['dbusername'],
             'PASSWORD':self.user_options['dbpassword'],
             'JDBC_URL': self.build_url()
             }
     
        t = Template(open(VomsConstants.db_props_template,"r").read())
        
        ConfigureAction.write_and_close(self, 
                                        vo_dababase_properties_file(self.user_options['vo']), 
                                        t.sub(m))
        
        setup_permissions(vo_dababase_properties_file(self.user_options['vo']),
                          0640, 
                          self.user_options['tomcat-group-id'])
        
    
    def write_service_properties(self):
                            
        m = {'NOTIFICATION.EMAIL_ADDRESS': self.user_options['mail-from'],
             'NOTIFICATION.SMTP_SERVER': self.user_options['smtp-host'],
             'WEBUI.ENABLED': self.user_options['webui-enabled'],
             'CA.FILES': self.user_options['ca-files'],
             'READ_ACCESS' : str(self.user_options.has_key('read-access-for-authenticated-clients')),
             'VO_AUP_URL': self.user_options['vo-aup-url'],
             'ADMIN_SKIP_CA_CHECK' : str(self.user_options.has_key('skip-ca-check')),
             'AA.CERT' : self.user_options['aa-cert'],
             'AA.KEY' : self.user_options['aa-key'],
             'SAML.MAX_ASSERTION_LIFETIME' : self.user_options['saml-max-assertion-lifetime'],
             'HOSTNAME' : self.user_options['hostname']
             }
        
        t = Template(open(VomsConstants.service_props_template,"r").read())
        ConfigureAction.write_and_close(self, 
                                        vo_service_properties_file(self.user_options['vo']), 
                                        t.sub(m))
        
        setup_permissions(vo_service_properties_file(self.user_options['vo']), 
                          0644, 
                          self.user_options['tomcat-group-id'])
    
    
    def write_siblings_context(self):
        m = {'WAR_FILE': VomsConstants.voms_siblings_war }
        
        t = Template(open(VomsConstants.voms_siblings_context_template,"r").read())
        ConfigureAction.write_and_close(self, 
                                        VomsConstants.voms_siblings_context, 
                                        t.sub(m))
        
        setup_permissions(VomsConstants.voms_siblings_context, 
                          0644, 
                          self.user_options['tomcat-group-id'])
        
    def write_context(self):
        
        war_file = VomsConstants.voms_admin_war
        
        if self.user_options.has_key('use-skinny-war'):
            war_file = VomsConstants.voms_admin_war_nodeps

        m = {'VO_NAME': self.user_options['vo'],
             'WAR_FILE': war_file }
             
        t = Template(open(VomsConstants.context_template,"r").read())
        ConfigureAction.write_and_close(self,
                                        vo_context_file(self.user_options['vo']), 
                                        t.sub(m))
        
        setup_permissions(vo_context_file(self.user_options['vo']),
                          0644, 
                          self.user_options['tomcat-group-id'])
        
    
    def write_voms_properties(self):
        
        m = { 
             'CODE': self.user_options['code'],
             'DBNAME': self.user_options['dbname'],
             'LOGFILE': os.path.join(self.user_options['logdir'],"voms.%s " % self.user_options['vo']),
             'PASSFILE': os.path.join(VomsConstants.voms_conf_dir,self.user_options['vo'],"voms.pass"),
             'SQLLOC': os.path.join(self.user_options['libdir'],self.user_options['sqlloc']),
             'USERNAME': self.user_options['dbusername'],
             'VONAME': self.user_options['vo'],
             'PORT': self.user_options['port'],
             'URI': self.user_options['uri'],
             'TIMEOUT': self.user_options['timeout']}
        
        t = Template(open(VomsConstants.voms_template,"r").read())
        
        voms_conf = t.sub(m)
        
        if self.user_options.has_key('shortfqans'):
            voms_conf = voms_conf + "\n--shortfqans"
        
        if self.user_options['dbtype'] == 'mysql' and self.user_options['dbhost'] != 'localhost':
            voms_conf = voms_conf + "\n--dbhost=%s" % self.user_options['dbhost']
        
        if self.user_options.has_key('skip-ca-check'):
            voms_conf = voms_conf + "\n--skipcacheck"
        
        self.vlog("voms core configuration:\n%s" % voms_conf)
        
        self.write_and_close(voms_config_file(self.user_options['vo']), 
                             voms_conf)
        
        self.write_and_close(voms_pass_file(self.user_options['vo']), 
                             self.user_options['dbpassword']+"\n")
     
        os.chmod(voms_config_file(self.user_options['vo']),0640)
        os.chmod(voms_pass_file(self.user_options['vo']), 0640)
        
    
    def write_vomses(self):
        
        vomses_string  = '"%s" "%s" "%s" "%s" "%s"\n' % (
                                                         self.user_options['vo'],
                                                         self.user_options['hostname'],
                                                         self.user_options['port'],
                                                         self.user_options['ta.subject'],
                                                         self.user_options['vo']
                                                         )
        
        self.write_and_close(vomses_file(self.user_options['vo']), vomses_string)
    
    def deploy_db(self):
        print "Deploying database for %s vo" % self.user_options['vo']
        
        deploy_cmd = "%s  deploy --vo %s" % (VomsConstants.voms_db_deploy,self.user_options['vo'])
    
        status = os.system(deploy_cmd)
        
        if status != 0:
            raise VomsConfigureError, "Error deploying voms database!"
        
    def enable_readonly_access(self):
        print "Adding read access for authenticated clients..."
        add_cmd = "%s --vo %s grant-read-only-access" % (VomsConstants.voms_db_deploy, 
                                                        self.user_options['vo'])
        
        status = os.system(add_cmd)
        if status != 0:
            raise VomsConfigureError, "Error enabling read-only access for authenticated clients!"
        
    def add_default_admin(self):
        print "Adding default admin for %s vo" % self.user_options['vo']
        
        add_cmd = "%s --vo %s --cert %s add-admin " % (VomsConstants.voms_db_deploy,
                                                       self.user_options['vo'],
                                                       self.user_options['admincert'])
        status = os.system(add_cmd)
        
        if status != 0:
            raise VomsConfigureError, "Error adding default admin!"
        
        
        

class InstallOracleVO(InstallVOAction):
    
    def __init__(self,user_options):
        InstallVOAction.__init__(self,user_options)
        
        
        self.name = "install_oracle_vo"
        self.driver_class = VomsConstants.oracle_driver_class
        self.dialect = VomsConstants.oracle_dialect
        
        set_default(self.user_options,'dbhost','localhost')
        set_default(self.user_options,'dbport','1521')
        set_default(self.user_options,"sqlloc","libvomsoracle.so")
        
        self.required_options += ['dbname']
    
    def build_url(self):
       
        if self.user_options.has_key('use-thin-driver'):
            return "jdbc:oracle:thin:@//%s:%s/%s" % (self.user_options['dbhost'],
                                                    self.user_options['dbport'],
                                                    self.user_options['dbname'])
        else:
            return "jdbc:oracle:oci:@%s" % (self.user_options['dbname'])
    

class InstallMySqlVO(InstallVOAction):
    def __init__(self,user_options):
        InstallVOAction.__init__(self,user_options)
        
        self.name = "install_mysql_vo"
        self.driver_class = VomsConstants.mysql_driver_class
        self.dialect = VomsConstants.mysql_dialect
        
        set_default(self.user_options,"dbname","voms_"+self.user_options['vo'])
        set_default(self.user_options,'dbhost','localhost')
        set_default(self.user_options,'dbport','3306')
        
        set_default(self.user_options,"sqlloc","libvomsmysql.so")
        
        
    def check_options(self):
        if self.user_options.has_key('createdb'):
            self.required_options += ['dbauser']
        
        InstallVOAction.check_options(self)
        
        
    
    def build_url(self):
        return "jdbc:mysql://%s:%s/%s" % (self.user_options['dbhost'],
                                          self.user_options['dbport'],
                                          self.user_options['dbname'])
    
    def deploy_db(self):
        
        print "Deploying database for %s vo" % self.user_options['vo']
        
        if self.user_options.has_key('createdb'):
            
            print "Creating mysql db..."
            
            if self.user_options.has_key('dbapwdfile'):
                dbapwd = open(self.user_options['dbapwdfile']).read()
                self.user_options['dbapwd'] = dbapwd
             
            
            ## Support for mysql admin empty password (VDT request).
            if (not self.user_options.has_key('dbapwd')) or len(self.user_options['dbapwd']) == 0:
                print "WARNING: No password has been specified for the mysql root account! I will continue the db deployment assuming no password has been set for such account."
                mysql_cmd = "%s -u%s --host %s --port %s" % (self.user_options['mysql-command'],
                                         self.user_options['dbauser'],
                                         ## Fix for http://savannah.cern.ch/bugs/?54613
                                         self.user_options['dbhost'],
                                         self.user_options['dbport'])
            else:
                mysql_cmd = "%s -u%s -p%s --host %s --port %s" % (self.user_options['mysql-command'],
                                              self.user_options['dbauser'],
                                              self.user_options['dbapwd'],
                                              ## Fix for http://savannah.cern.ch/bugs/?54613
                                              self.user_options['dbhost'],
                                              self.user_options['dbport'])
            
            
            if len(self.user_options['dbusername']) > 16:
                raise VomsConfigureError, "MYSQL usernames can be up to 16 characters long! Choose a shorter username"
            
            
            mysql_proc = subprocess.Popen(mysql_cmd, shell=True, stdin=subprocess.PIPE, stderr=subprocess.PIPE)
            
            ## Check if database already exists
            print >>mysql_proc.stdin, "use %s;" % self.user_options['dbname']
            mysql_proc.stdin.close()
            
            status = exit_status(mysql_proc.wait())
            
            if status == 0:
                print "Schema for database '%s' already exists... will not create it" % self.user_options['dbname']
            else:          
                
                ## We received an error from the mysql command line client
                err_msg = mysql_proc.stderr.read()
                match = re.match("ERROR 1049", string.strip(err_msg))
                if match:
                    
                    ## The database for this vo is not there, let's create it
                    mysql_proc = subprocess.Popen(mysql_cmd, shell=True, stdin=subprocess.PIPE)
                    print >>mysql_proc.stdin, "create database %s;" % self.user_options['dbname']
                    print >>mysql_proc.stdin, "grant all privileges on %s.* to '%s'@'%s' identified by '%s' with grant option;" % (self.user_options['dbname'],
                                                                                                                       self.user_options['dbusername'],
                                                                                                                       socket.gethostname(),
                                                                                                                       self.user_options['dbpassword'])
                    
                    print >>mysql_proc.stdin, "grant all privileges on %s.* to '%s'@'%s' identified by '%s' with grant option;" % (self.user_options['dbname'],
                                                                                                                       self.user_options['dbusername'],
                                                                                                                       socket.getfqdn(),
                                                                                                                       self.user_options['dbpassword'])
            
                    print >>mysql_proc.stdin, "grant all privileges on %s.* to '%s'@'%s' identified by '%s' with grant option;" % (self.user_options['dbname'],
                                                                                                                       self.user_options['dbusername'],
                                                                                                                        'localhost',
                                                                                                                        self.user_options['dbpassword'])
            
                    print >>mysql_proc.stdin, "grant all privileges on %s.* to '%s'@'%s' identified by '%s' with grant option;" % (self.user_options['dbname'],
                                                                                                                       self.user_options['dbusername'],
                                                                                                                     'localhost.%',
                                                                                                                     self.user_options['dbpassword'])
                    print >>mysql_proc.stdin, "flush privileges;"
                    
                    mysql_proc.stdin.close()
                    status = exit_status(mysql_proc.wait())
                    
                    if status != 0:
                        raise VomsConfigureError, "Error creating mysql database! " + mysql_proc.stdout.read()
                
                else:
                    
                    ## We received an unexpected error from mysql, just report it and fail
                    raise VomsConfigureError, "Error checking mysql database existence!\nMysql error:\n" + err_msg
                    
                                
                
            
               
        
        InstallVOAction.deploy_db(self)

    
## The original, and reliable, python Template class
## implementation is not yet supported in python 2.2.3                 
class Template:   
    
    def __init__(self, template):
        self.template = template
        self.mappings = {}
        
    def sub(self, mappings):
        
        def match_helper(mo):
            key = mo.group(0)[1:-1]
            if mappings.has_key(key):
                if mappings[key] is None:
                    #print "Warning: No mapping found for key:", key
                    #return ""
                    raise ValueError, 'No mapping found for key:'+key
                return mappings[key]
            else:
                #print "Warning: No mapping found for key:", key
                #return ""
                raise ValueError, "Unknown mapping found in template: %s" % key
        
        delimiter = '@'
        pattern = r''+delimiter+'[_a-zA-Z][\._a-zA-Z0-9]*'+delimiter
        
        self.mappings = mappings
        
        if self.template is None:
            raise ValueError, "Undefined template text!"
        
        if len(self.mappings) == 0:
            raise ValueError, "Empty mappings!"
        
        return re.sub(pattern,match_helper,self.template)        
        
    
                     
class X509Helper:
    def __init__(self,filename, openssl_cmd=None):    
        self.filename= filename
        self.openssl_cmd = openssl_cmd
        self.parse()
    
    def parse(self):
        
        if self.openssl_cmd:
            openssl = self.openssl_cmd
        else:
            openssl = 'openssl'
        
        base_cmd = openssl+' x509 -in \'%s\' -noout ' % self.filename
        
        status,subject = commands.getstatusoutput(base_cmd+'-subject')
        if status:
            raise VomsConfigureError, "Error invoking openssl: "+ subject
        
        status,issuer = commands.getstatusoutput(base_cmd+'-issuer')
        if status:
            raise VomsConfigureError, "Error invoking openssl: "+ issuer
        
        
        status,email = commands.getstatusoutput(base_cmd+'-email')
        if status:
            raise VomsConfigureError, "Error invoking openssl: "+ email
        
        self.subject = re.sub(r'^subject= ','',subject.strip())
        self.issuer = re.sub(r'^issuer= ','',issuer.strip())
        self.subject = re.sub(r'/(E|e|((E|e|)(mail|mailAddress|mailaddress|MAIL|MAILADDRESS)))=','/Email=',self.subject)
        
        # Handle emailAddress also in the CA DN (Bug #36490)
        self.issuer = re.sub(r'/(E|e|((E|e|)(mail|mailAddress|mailaddress|MAIL|MAILADDRESS)))=','/Email=',self.issuer)
        
        # Handle also UID
        self.subject = re.sub(r'/(UserId|USERID|userId|userid|uid|Uid)=','/UID=',self.subject)
        
        self.email = email.strip()
        
        # Check that only first email address is taken from the certificate, the openssl -email command
        # returns one address per line
        emails = email.splitlines(False)
        if len(emails) > 0:
            self.email = emails[0]
        
    
    def __repr__(self):
        return 'Subject:%s\nIssuer:%s\nEmail:%s' % (self.subject, self.issuer, self.email)
        
          
def template_prefix():
    return os.path.join(voms_admin_prefix(),"share","voms-admin","templates")

def voms_log_dir():
    return os.path.join(voms_prefix(),"var","log","voms")
    
def voms_admin_prefix():
    if voms_admin_sysconfig_props.has_key('PREFIX'):
        return voms_admin_sysconfig_props['PREFIX']+"/usr"
    
    return "/usr" 

def voms_admin_conf_dir():
    if voms_admin_sysconfig_props.has_key('CONF_DIR'):
        return voms_admin_sysconfig_props['CONF_DIR']
    
    return "/etc/voms-admin"

def voms_prefix():
    if voms_admin_sysconfig_props.has_key('PREFIX'):
        return voms_admin_sysconfig_props['PREFIX']
    
    return "/"

def voms_conf_dir():
    if voms_admin_sysconfig_props.has_key('PREFIX'):
        return voms_admin_sysconfig_props['PREFIX']+"/etc/voms"
    
    return "/etc/voms"

def voms_lib_dir():
    prefix=voms_prefix()
    
    plat = platform.machine()
    libdir = "lib"
    
    if plat == "x86_64":
        ## FIXME: understand how this behaves in Debian
        libdir = "lib64"
            
    return os.path.join(prefix,"usr", libdir)

def catalina_home():
    if voms_admin_sysconfig_props.has_key('CATALINA_HOME'):
        return voms_admin_sysconfig_props['CATALINA_HOME']
    
    if os.environ.has_key('CATALINA_HOME'):
        return os.environ['CATALINA_HOME']
    
    return None
    
class VomsConstants:
    
    version = voms_admin_server_version
      
    db_props_template = os.path.join(template_prefix(),"voms.database.properties.template")
    service_props_template = os.path.join(template_prefix(),"voms.service.properties.template")
    
    context_template = os.path.join(template_prefix(),"context.xml.template")
    voms_template = os.path.join(template_prefix(),"voms.conf.template")
    
    voms_admin_conf_dir = voms_admin_conf_dir()
    voms_conf_dir = voms_conf_dir() 
    
    voms_siblings_context_template = os.path.join(template_prefix(),"siblings-context.xml.template")
    voms_siblings_context = os.path.join(voms_admin_conf_dir,"voms-siblings.xml")
    
    vo_aup_template = os.path.join(template_prefix(),"aup", "vo-aup.txt")
    logging_conf_template = os.path.join(template_prefix(), "logback.runtime.xml")
    
    voms_admin_war = os.path.join(voms_admin_prefix(), "share","webapps","glite-security-voms-admin.war")
    voms_admin_war_nodeps = os.path.join(voms_admin_prefix(), "share","webapps","glite-security-voms-admin-nodeps.war")
    
    voms_siblings_war = os.path.join(voms_admin_prefix(), "share","webapps","glite-security-voms-siblings.war")
    
    voms_admin_libs = glob.glob(os.path.join(voms_admin_prefix(),"share","voms-admin","tools","lib")+"/*.jar")
    voms_admin_classes = os.path.join(voms_admin_prefix(),"share","voms-admin","tools", "classes")
    voms_admin_jar = os.path.join(voms_admin_prefix(), "share","java","glite-security-voms-admin.jar")
       
    voms_db_deploy = os.path.join(voms_admin_prefix(),"sbin","voms-db-deploy.py")
    
    schema_deployer_class = "org.glite.security.voms.admin.persistence.deployer.SchemaDeployer"
    
    oracle_driver_class = "oracle.jdbc.driver.OracleDriver"
    oracle_dialect = "org.hibernate.dialect.Oracle9Dialect"
    
    mysql_driver_class = "org.gjt.mm.mysql.Driver"
    mysql_dialect = "org.hibernate.dialect.MySQLInnoDBDialect"
    
    long_options=["help",
              "version",
              "verbose",
              "command=",
              "vo=",
              "admincert=",
              "mail-from=",
              "smtp-host=",
              "port=",
              "dbtype=",
              "dbname=",
              "dbusername=",
              "dbpassword=",
              "dbhost=",
              "dbport=",
              "deploy-database",
              "undeploy-database",
              "skip-database",
              "oracle-tns-alias=",
              "use-oci-driver",
              "createdb",
              "dropdb",
              "dbauser=",
              "dbapwd=",
              "dbapwdfile=",
              "mysql-command=",
              "mysql-host=",
              "mysql-port=",
              "cert=",
              "key=",
              "certdir=",
              "code=",
              "libdir=",
              "logdir=",
              "sqlloc=",
              "config-owner=",
              "tomcat-group=",
              "voms-group=",
              "hostname=",
              "openssl=",
              "use-thin-driver",
              "disable-webui-requests",
              "uri=",
              "timeout=",
              "shortfqans",
              "read-access-for-authenticated-clients",
              "skip-voms-core",
              "vo-aup-url=",
              "admin-skip-ca-check",
              "use-skinny-war",
              "aa-cert=",
              "aa-key=",
              "saml-max-assertion-lifetime=",
              "skip-ca-check"]              

    short_options = "hvV";

    commands= ["install", 
               "remove", 
               "upgrade"]
    
    

        


            
            
        
        
        
    
        

        