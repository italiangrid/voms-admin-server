#!/bin/bash
 
# determine the hostname
SERVICE_HOST=`hostname`
 
# configuration template and files
INFO_SERVICE_CONFIG=/etc/glite/info/service
GLUE2_VOMS_CORE_CONF_TEMPLATE="${INFO_SERVICE_CONFIG}/glite-info-glue2-voms.conf.template"
GLUE2_VOMS_CORE_CONF_FILE="${INFO_SERVICE_CONFIG}/glite-info-glue2-voms.conf"
GLUE2_VOMS_ADMIN_CONF_TEMPLATE="${INFO_SERVICE_CONFIG}/glite-info-glue2-voms-admin.conf.template"
GLUE2_VOMS_ADMIN_CONF_FILE="${INFO_SERVICE_CONFIG}/glite-info-glue2-voms-admin.conf"
GLUE_VOMS_CORE_CONF_TEMPLATE="${INFO_SERVICE_CONFIG}/glite-info-service-voms.conf.template"
GLUE_VOMS_CORE_CONF_FILE="${INFO_SERVICE_CONFIG}/glite-info-service-voms.conf"
GLUE_VOMS_ADMIN_CONF_TEMPLATE="${INFO_SERVICE_CONFIG}/glite-info-service-voms-admin.conf.template"
GLUE_VOMS_ADMIN_CONF_FILE="${INFO_SERVICE_CONFIG}/glite-info-service-voms-admin.conf"
 
# info providers files
INFO_PROVIDER_PATH=/var/lib/bdii/gip/provider
GLUE2_INFO_PROVIDER="${INFO_PROVIDER_PATH}/glite-info-glue2-provider-service-voms"
GLUE_VOMS_CORE_INFO_PROVIDER=${INFO_PROVIDER_PATH}/glite-info-provider-service-voms
GLUE_VOMS_ADMIN_INFO_PROVIDER=${INFO_PROVIDER_PATH}/glite-info-provider-service-voms-admin
 
# GLUE2 script
INFO_SERVICE_BIN_PATH=/usr/bin
GLUE2_SCRIPT="${INFO_SERVICE_BIN_PATH}/glite-info-glue2-voms"
 
# parse options
while getopts s:e name
do
	case $name in
		s) SITE_NAME=$OPTARG;;
		e) VOMS_ADMIN_INSTALL=true;;
		?) printf "Usage: %s: -s SITE_NAME [-e]\n" $0
			exit 2;;
  esac
done
 
# options -s is mandatory
if [ "x${SITE_NAME}" == "x" ]; then
	printf "$0: option s is mandatory"'\n'
	printf "Usage: %s: -s SITE_NAME [-e]\n" $0
	exit 1
fi
 
# are voms-admin providers to install?
if [ "x${VOMS_ADMIN_INSTALL}" == "xtrue" ]; then
	printf "Will install providers for voms-admin"'\n'
fi
 
# check that the templates for voms core exists
if [ ! -f ${GLUE2_VOMS_CORE_CONF_TEMPLATE} ]; then
	echo "The template file ${GLUE2_VOMS_CORE_CONF_TEMPLATE} was not found."
	exit 1
fi
if [ ! -f ${GLUE_VOMS_CORE_CONF_TEMPLATE} ]; then
	echo "The template file ${GLUE_VOMS_CORE_CONF_TEMPLATE} was not found."
	exit 1
fi
 
# check the glue2 voms script exists
if [ ! -f ${GLUE2_SCRIPT} ]; then
	echo "The script file ${GLUE2_SCRIPT} was not found."
fi
 
# create conf files for voms core
echo "Creating conf file ${GLUE2_VOMS_CORE_CONF_FILE}"
rm -rf ${GLUE2_VOMS_CORE_CONF_FILE} && cp ${GLUE2_VOMS_CORE_CONF_TEMPLATE} ${GLUE2_VOMS_CORE_CONF_FILE}
echo "Creating conf file ${GLUE_VOMS_CORE_CONF_FILE}"
rm -rf ${GLUE_VOMS_CORE_CONF_FILE} && cp ${GLUE_VOMS_CORE_CONF_TEMPLATE} ${GLUE_VOMS_CORE_CONF_FILE}
 
PROVIDER_CONF_FILES=${GLUE2_VOMS_CORE_CONF_FILE}
 
# voms-admin publication only if enabled
if [ "x${VOMS_ADMIN_INSTALL}" == "xtrue" ]; then
 
	# check that the template for voms admin exists
	if [ ! -f ${GLUE2_VOMS_ADMIN_CONF_TEMPLATE} ]; then
		echo "The glue2 template file ${GLUE2_VOMS_ADMIN_CONF_TEMPLATE} was not found."
		exit 1
	fi
	if [ ! -f ${GLUE_VOMS_ADMIN_CONF_TEMPLATE} ]; then
		echo "The template file ${GLUE_VOMS_ADMIN_CONF_TEMPLATE} was not found."
		exit 1
	fi
 
	# create conf files for voms admin
	echo "Creating conf file ${GLUE2_VOMS_ADMIN_CONF_FILE}"
 	rm -rf ${GLUE2_VOMS_ADMIN_CONF_FILE} && cp ${GLUE2_VOMS_ADMIN_CONF_TEMPLATE} ${GLUE2_VOMS_ADMIN_CONF_FILE}
	echo "Creating conf file ${GLUE_VOMS_ADMIN_CONF_FILE}"
	rm -rf ${GLUE_VOMS_ADMIN_CONF_FILE} && cp ${GLUE_VOMS_ADMIN_CONF_TEMPLATE} ${GLUE_VOMS_ADMIN_CONF_FILE}
 
	PROVIDER_CONF_FILES=${PROVIDER_CONF_FILES}","${GLUE2_ADMIN_CONF_FILE}
 
fi
 
echo "Create the ${INFO_PROVIDER_PATH} in case it doesn't exist"
mkdir -p ${INFO_PROVIDER_PATH}
 
# create the info provider file
echo "Create the ${GLUE2_INFO_PROVIDER} file"
rm -rf ${GLUE2_INFO_PROVIDER}
cat << EOF > ${GLUE2_INFO_PROVIDER}
#!/bin/sh
export VOMS_HOST=${SERVICE_HOST}
${GLUE2_SCRIPT} ${PROVIDER_CONF_FILES} $SITE_NAME
EOF
chmod +x ${GLUE2_INFO_PROVIDER}
 
echo "Create the ${GLUE_VOMS_CORE_INFO_PROVIDER} file"
rm -rf ${GLUE_VOMS_CORE_INFO_PROVIDER}
cat << EOF > ${GLUE_VOMS_CORE_INFO_PROVIDER}
#!/bin/sh
export PATH=$PATH:${INFO_SERVICE_BIN_PATH}
export VOMS_HOST=${SERVICE_HOST}
${INFO_SERVICE_BIN_PATH}/glite-info-service-voms-wrapper ${GLUE_VOMS_CORE_CONF_FILE} $SITE_NAME
EOF
chmod +x ${GLUE_VOMS_CORE_INFO_PROVIDER}
  
# voms-admin publication only if enabled
if [ "x${VOMS_ADMIN_INSTALL}" == "xtrue" ]; then
 
	echo "Create the ${GLUE_VOMS_ADMIN_INFO_PROVIDER} file"
	rm -rf ${GLUE_VOMS_ADMIN_INFO_PROVIDER}
	cat << EOF > ${GLUE_VOMS_ADMIN_INFO_PROVIDER}
#!/bin/sh
export PATH=$PATH:${INFO_SERVICE_SCRIPT}
export VOMS_ADMIN_HOST=${SERVICE_HOST}
${INFO_SERVICE_BIN_PATH}/glite-info-service ${GLUE_VOMS_ADMIN_CONF_FILE} $SITE_NAME
EOF
	chmod +x ${GLUE_VOMS_ADMIN_INFO_PROVIDER} 
fi