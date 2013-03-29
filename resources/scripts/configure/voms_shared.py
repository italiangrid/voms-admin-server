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
import string
__voms_version__ = "${pom.version}"
__voms_prefix__ = "${package.prefix}"

import re, commands, exceptions, os.path, glob, platform

def mysql_util_cmd(command, options):
    db_cmd = "%s %s --dbauser %s --dbusername %s --dbpassword %s --dbname %s --dbhost %s --dbport %s --mysql-command %s" % (VOMSDefaults.voms_mysql_util,
                                                                                                                            command,
                                                                                                                            options.dbauser,
                                                                                                                            options.dbusername,
                                                                                                                            options.dbpassword,
                                                                                                                            options.dbname,
                                                                                                                            options.dbhost,
                                                                                                                            options.dbport,
                                                                                                                            options.mysql_command)
    
    if options.dbapwdfile:
        dbapwd = open(options.dbapwdfile).read()
        options.dbapwd = dbapwd
        
    if options.dbapwd:
        db_cmd += " --dbapwd=%s" % options.dbapwd
    
    return db_cmd

def voms_add_admin_cmd(vo, cert, ignore_email=False):
    if ignore_email:
        return "%s %s" % (__voms_db_util_base_cmd(vo, "add-admin"), "--cert %s --ignore-cert-email" % cert)
    else:
        return "%s %s" % (__voms_db_util_base_cmd(vo, "add-admin"), "--cert %s" % cert)

def voms_ro_auth_clients_cmd(vo):
    return __voms_db_util_base_cmd(vo, "grant-read-only-access")
 
def voms_deploy_database_cmd(vo):
    return __voms_db_util_base_cmd(vo, "deploy")

def voms_undeploy_database_cmd(vo):
    return __voms_db_util_base_cmd(vo, "undeploy")

def __voms_db_util_base_cmd(vo, command):
    return "%s %s --vo %s" % (VOMSDefaults.voms_db_util, command, vo)

def voms_version():
    if __voms_version__ == "${pom.version":
        return "unset"
    return __voms_version__

def voms_prefix():
    if __voms_prefix__ == "${package.prefix}":
        return "/opt/voms"
    else:
        return __voms_prefix__

def template_prefix():
    return os.path.join(voms_prefix(), "usr","share", "voms-admin","templates")

def admin_conf_dir(vo=None):
    if vo is None:
        return os.path.join(voms_prefix(), "etc", "voms-admin")
    else:
        return os.path.join(voms_prefix(), "etc", "voms-admin", vo)

def core_conf_dir(vo=None):
    if vo is None:
        return os.path.join(voms_prefix(), "etc","voms")
    else:
        return os.path.join(voms_prefix(), "etc","voms", vo)

def admin_db_properties_path(vo):
    return os.path.join(admin_conf_dir(vo), "database.properties")

def admin_service_properties_path(vo):
    return os.path.join(admin_conf_dir(vo), "service.properties")

def admin_service_endpoint_path(vo):
    return os.path.join(admin_conf_dir(vo), "service-endpoint")

def admin_logging_conf_path(vo):
    return os.path.join(admin_conf_dir(vo), "logback.xml")

def vomses_path(vo):
    return os.path.join(admin_conf_dir(vo), "vomses")

def lsc_path(vo):
    return os.path.join(admin_conf_dir(vo), "lsc")

def aup_path(vo):
    return os.path.join(admin_conf_dir(vo), "vo-aup.txt")

def voms_log_path():
    return os.path.join(voms_prefix(),"var", "log", "voms")

def voms_conf_path(vo):
    return os.path.join(core_conf_dir(), vo, "voms.conf")

def voms_pass_path(vo):
    return os.path.join(core_conf_dir(), vo, "voms.pass")

def voms_lib_dir():
    prefix=voms_prefix()
    
    plat = platform.machine()
    libdir = "lib"
    
    if plat == "x86_64":
        ## FIXME: understand how this behaves in Debian
        libdir = "lib64"
            
    return os.path.join(prefix,"usr", libdir)

class VOMSDefaults:
    db_props_template = os.path.join(template_prefix(), "database.properties")
    service_props_template = os.path.join(template_prefix(),"service.properties")
    voms_template = os.path.join(template_prefix(),"voms.conf")
        
    vo_aup_template = os.path.join(template_prefix(),"vo-aup.txt")
    logging_conf_template = os.path.join(template_prefix(), "logback.xml")
    
    voms_admin_war = os.path.join(voms_prefix(), "usr","share","webapps","voms-admin.war")
    
    voms_admin_libs = glob.glob(os.path.join(voms_prefix(),"var", "lib","voms-admin","lib")+"/*.jar")
    voms_admin_classes = os.path.join(voms_prefix(),"var", "lib","voms-admin","tools")
    voms_admin_jar = os.path.join(voms_prefix(), "usr", "share","java","voms-admin.jar")
       
    voms_db_util = os.path.join(voms_prefix(),"usr", "sbin","voms-db-util")
    voms_mysql_util = os.path.join(voms_prefix(), "usr", "sbin", "voms-mysql-util")
    
    schema_deployer_class = "org.glite.security.voms.admin.persistence.deployer.SchemaDeployer"
    
    oracle_driver_class = "oracle.jdbc.driver.OracleDriver"
    oracle_dialect = "org.hibernate.dialect.Oracle9Dialect"
    
    mysql_driver_class = "org.gjt.mm.mysql.Driver"
    mysql_dialect = "org.hibernate.dialect.MySQLInnoDBDialect"



def parse_sysconfig():
    sysconfig_filename = os.path.join(voms_prefix(), 
                                      "etc", "sysconfig", "voms-admin")
    
    helper = PropertyHelper(sysconfig_filename)
    return helper

def get_oracle_env():
    sysconfig = parse_sysconfig()
    template_str = "LD_LIBRARY_PATH=$ORACLE_LIBRARY_PATH TNS_ADMIN=$TNS_ADMIN"
    template = string.Template(template_str)
    return template.substitute(sysconfig)
    
class VOMSError(exceptions.RuntimeError):   
    pass

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
            raise VOMSError, "Error invoking openssl: "+ subject
        
        status,issuer = commands.getstatusoutput(base_cmd+'-issuer')
        if status:
            raise VOMSError, "Error invoking openssl: "+ issuer
        
        
        status,email = commands.getstatusoutput(base_cmd+'-email')
        if status:
            raise VOMSError, "Error invoking openssl: "+ email
        
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