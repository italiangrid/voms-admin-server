#!/bin/bash

# init function for the voms-admin service publisher
# 

# the script needs the vo name

# the vo
if [ x$1 == "x" ]; then
    echo No VO name defined
    exit 1
fi
VOMS_VO=$1

# the hostname
VOMS_ADMIN_HOST=${VOMS_ADMIN_HOST:-`hostname -f`}

# the port
VOMS_CONF=/etc/voms-admin/$VOMS_VO/service-endpoint
if [ -r $VOMS_CONF ]; then
    VOMS_ADMIN_PORT=`cut -d: -f2 $VOMS_CONF`
    VOMS_ADMIN_PORT=${VOMS_ADMIN_PORT:-UNDEFINEDVALUE}
else
    echo No configuration file $VOMS_CONF found for VO $VOMS_VO
    exit 2
fi

# the pid file
VOMS_ADMIN_PID_FILE=/var/lock/subsys/voms-container

# Write to stdout - will be imported by the info provider
echo VOMS_ADMIN_HOST=$VOMS_ADMIN_HOST
echo VOMS_ADMIN_PORT=$VOMS_ADMIN_PORT
echo VOMS_ADMIN_PID_FILE=$VOMS_ADMIN_PID_FILE
echo VOMS_VO=$VOMS_VO
