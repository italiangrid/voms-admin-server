#!/bin/bash
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
