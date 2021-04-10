#!/bin/bash
set -ex

source /etc/sysconfig/voms-admin

OJDBC_JAR=${OJDBC_JAR:-${ORACLE_LIBRARY_PATH}/ojdbc6.jar}
ORACLE_ENV="LD_LIBRARY_PATH=$ORACLE_LIBRARY_PATH TNS_ADMIN=$TNS_ADMIN"

TNS_ADMIN=/home/voms LD_LIBRARY_PATH=${ORACLE_LIBRARY_PATH} java -cp ${OJDBC_JAR}:'/var/lib/voms-admin/lib/*':/usr/share/java/voms-admin.jar org.glite.security.voms.admin.integration.orgdb.tools.OrgDBUtil -c /etc/voms-admin/test_0/orgdb.properties $@
