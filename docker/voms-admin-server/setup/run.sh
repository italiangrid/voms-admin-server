#!/bin/bash
set -ex

SCRIPTS_PREFIX=${SCRIPTS_PREFIX:-/scripts}
VO_NAME_PREFIX=${VOMS_VO_NAME_PREFIX:-"test"}
VO_COUNT=${VOMS_VO_COUNT:-1}

VOMS_BASE_CORE_PORT=${VOMS_BASE_CORE_PORT:-15000}

# Wait for database service to be up
mysql_host=db
mysql_port=3306 

echo -n "waiting for TCP connection to $mysql_host:$mysql_port..."

while ! nc -w 1 $mysql_host $mysql_port 2>/dev/null
do
  echo -n .
  sleep 1
done

echo 'Database server is up.'

## Create database schemas
/bin/bash ${SCRIPTS_PREFIX}/create-schemas.sh

if [[ -n "${VOMS_ADMIN_SERVER_PACKAGE_URL}" ]]; then
  yum -y remove voms-admin-server
  yum -y install ${VOMS_ADMIN_SERVER_PACKAGE_URL}
fi

# Install requested voms-admin-server version
if [[ -n "${VOMS_ADMIN_SERVER_VERSION}" ]]; then
  yum -y remove voms-admin-server
  yum install -y voms-admin-server-${VOMS_ADMIN_SERVER_VERSION}
fi

# Install requested voms-admin client
if [[ -n "${VOMS_ADMIN_CLIENT_VERSION}" ]]; then
  yum -y remove voms-admin-client
  yum install -y voms-admin-client-${VOMS_ADMIN_CLIENT_VERSION}
fi

cp ${SCRIPTS_PREFIX}/tnsnames.ora /home/voms
chown voms:voms /home/voms/tnsnames.ora

## Setup certificates
cp /etc/grid-security/hostcert.pem  /etc/grid-security/vomscert.pem
cp /etc/grid-security/hostkey.pem  /etc/grid-security/vomskey.pem
chown voms:voms /etc/grid-security/voms*.pem

# Do this or voms-admin webapp will fail silently and always return 503
mkdir -p /etc/grid-security/vomsdir

## Preconfigure using existing package, if requested, or if just skipping
## the deployment of a tarball
if [[ -z ${VOMS_DEPLOY_TARBALL} ]]; then
  VOMS_PRE_CONFIGURE=y
fi

if [[ -n ${VOMS_LOAD_DB_DUMP} ]]; then
  /bin/bash ${SCRIPTS_PREFIX}/load-db-dump.sh
fi

if [[ -n ${VOMS_PRE_CONFIGURE} ]]; then
  echo "Running preconfiguration..."
  CONFIGURE_VO_OPTIONS=${VOMS_PRE_CONFIGURE_OPTIONS} /bin/bash ${SCRIPTS_PREFIX}/configure-vos.sh
fi

if [[ -n "${VOMS_DEPLOY_TARBALL}" ]]; then
  echo "Istalling VOMS development tarball from /code"

  ## Install new code
  ls -l /code

  old_jars=$(ls /var/lib/voms-admin/lib/*.jar)
  for j in ${old_jars}; do
    echo "Removing: $j" && rm -rf $j
  done

  old_jars=$(ls /var/lib/voms-admin/lib/)
  echo "Old jars after removal: ${old_jars}"

  tar -C / -xvzf /code/voms-admin-server/target/voms-admin-server.tar.gz

  chown -R voms:voms /var/lib/voms-admin/work /var/log/voms-admin

  if [[ -n "$VOMS_UPGRADE_DB" ]]; then
    echo "Running database upgrade..."
    /bin/bash ${SCRIPTS_PREFIX}/upgrade-db.sh
  fi

  skip_configuration=false

  ## Skip configuration if requested
  [ -n "$VOMS_SKIP_CONFIGURE" ] && skip_configuration=true

  ## But only if configuration for the VO exists
  for i in $(seq 0 ${VO_CONT}); do
    VO_NAME=${VO_NAME_PREFIX}_$i
    [ ! -e "/etc/voms-admin/${VO_NAME}/service.properties" ] && skip_configuration=false
  done

  if [[ "$skip_configuration" = "false" ]]; then
      echo "Running configuration..."
      CONFIGURE_VO_OPTIONS=${VOMS_CONFIGURE_OPTIONS} /bin/bash ${SCRIPTS_PREFIX}/configure-vos.sh
  fi
fi

# Setup logging so that everything goes to stdout
cp ${SCRIPTS_PREFIX}/logback.xml /etc/voms-admin/voms-admin-server.logback

for i in $(seq 0 ${VO_COUNT}); do
    VO_NAME=${VO_NAME_PREFIX}_$i
    cp ${SCRIPTS_PREFIX}/logback.xml /etc/voms-admin/${VO_NAME}/logback.xml
    chown voms:voms /etc/voms-admin/${VO_NAME}/logback.xml
done

chown voms:voms /etc/voms-admin/voms-admin-server.logback

# Setup orgdb.properties, if the orgdb volume is mounted, only for the 
# first configured VO
if [ -e "/orgdb/orgdb.properties" ]; then
  cp /orgdb/orgdb.properties /etc/voms-admin/test_0/orgdb.properties
  chown voms:voms /etc/voms-admin/test_0/orgdb.properties

  # Just a newline
  echo >> /etc/voms-admin/test_0/service.properties
  cat ${SCRIPTS_PREFIX}/orgdb.template >> /etc/voms-admin/test_0/service.properties
fi

# Deploy test vos
for i in $(seq 0 ${VO_COUNT}); do
    VO_NAME=${VO_NAME_PREFIX}_$i
    touch "/var/lib/voms-admin/vo.d/$VO_NAME"
done

# Set log levels
VOMS_LOG_LEVEL=${VOMS_LOG_LEVEL:-INFO}
JAVA_LOG_LEVEL=${JAVA_LOG_LEVEL:-ERROR}

VOMS_JAVA_OPTS="-Dvoms.dev=true -DVOMS_LOG_LEVEL=${VOMS_LOG_LEVEL} -DJAVA_LOG_LEVEL=${JAVA_LOG_LEVEL} ${VOMS_JAVA_OPTS}"

if [ -n "$ENABLE_YOUR_KIT" ]; then
  YOUR_KIT_OPTS=${VOMS_YOUR_KIT_OPTS:-"-javaagent:/yourkit/libyjpagent.so,delay=10000,port=20001"}
  VOMS_JAVA_OPTS="${VOMS_YOUR_KIT_OPTS} ${VOMS_JAVA_OPTS}"
fi

if [ -n "$ENABLE_JREBEL" ]; then
  if [ -n "$ENABLE_YOUR_KIT" ]; then
    echo "YourKit is enabled... will ignore Jrebel"
  else
    JREBEL_OPTS=${VOMS_JREBEL_OPTS:-"-javaagent:/jrebel/jrebel.jar -Drebel.stats=false -Drebel.usage_reporting=false -Drebel.struts2_plugin=true -Drebel.tiles2_plugin=true"}
    VOMS_JAVA_OPTS="${JREBEL_OPTS} ${VOMS_JAVA_OPTS}"
  fi
fi

if [ -z "$VOMS_DEBUG_PORT" ]; then
  VOMS_DEBUG_PORT=1044
fi

if [ -z "$VOMS_DEBUG_SUSPEND" ]; then
  VOMS_DEBUG_SUSPEND="n"
fi

if [ ! -z "$VOMS_DEBUG" ]; then
  VOMS_JAVA_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=$VOMS_DEBUG_PORT,suspend=$VOMS_DEBUG_SUSPEND $VOMS_JAVA_OPTS"
fi

if [ -n "$ENABLE_JMX" ]; then
  JMX_PORT=${VOMS_JMX_PORT:-6002}
  VOMS_JAVA_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=${JMX_PORT} -Dcom.sun.management.jmxremote.rmi.port=${JMX_PORT} -Djava.rmi.server.hostname=dev.local.io -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false $VOMS_JAVA_OPTS"
fi

VOMS_JAR=${VOMS_JAR:-/usr/share/java/voms-container.jar}
VOMS_MAIN_CLASS=${VOMS_MAIN_CLASS:-org.italiangrid.voms.container.Container}
ORACLE_LIBRARY_PATH=${ORACLE_LIBRARY_PATH:-/usr/lib64/oracle/11.2.0.3.0/client/lib64}
OJDBC_JAR=${OJDBC_JAR:-${ORACLE_LIBRARY_PATH}/ojdbc6.jar}
TNS_ADMIN=${TNS_ADMIN:-/home/voms}

ORACLE_ENV="LD_LIBRARY_PATH=$ORACLE_LIBRARY_PATH TNS_ADMIN=$TNS_ADMIN"

if [ -z "$VOMS_SKIP_JAVA_SETUP" ]; then
  /bin/bash ${SCRIPTS_PREFIX}/setup-java.sh
fi

java -version

## Add test0 admin
for i in $(seq 0 ${VO_COUNT}); do
    VO_NAME=${VO_NAME_PREFIX}_$i
    voms-db-util add-admin --vo ${VO_NAME} \
      --cert /usr/share/igi-test-ca/test0.cert.pem \
      || echo "Error creating test0 admin. Does it already exist?"
done

# Start service
if [ -n "$VOMS_DEV_MODE" ]; then
  echo "su voms -s /bin/bash -c \"$ORACLE_ENV java $VOMS_JAVA_OPTS -cp $VOMS_JAR:$OJDBC_JAR $VOMS_MAIN_CLASS $VOMS_ARGS\"" > /root/start-voms-admin.sh
  echo "VOMS Admin startup command saved in /root/start-voms-admin.sh . User docker exec to enter this container and start/stop the service as you like"
  sleep infinity
else
  su voms -s /bin/bash -c "$ORACLE_ENV java $VOMS_JAVA_OPTS -cp $VOMS_JAR:$OJDBC_JAR $VOMS_MAIN_CLASS $VOMS_ARGS"
fi
