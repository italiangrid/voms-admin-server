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
# Authors:
# 	Andrea Ceccanti (INFN)
#


# This is a wrapper for use on a voms-admin server; it calls the info provider
# once for each configured VO

if [ x$2 == "x" ]; then
    echo Not enough arguments
    echo "Usage: glite-info-service-voms-wrapper <config-file> <site-ID> [<service-ID>]"
    exit 1
fi

config=$1
sitename=$2

# Construct the base UniqueID in the same format as the info provider
host=`hostname -f`
check=`cksum $config | cut -d" " -f 1`
uniqueid=${3:-${host}_org.glite.voms_${check}}

# The arguments are the same as for the info provider itself, i.e.
# config file, site name and optional service unique ID

# Get the list of configured VOs (plus any other directories which happen
# to be in the voms directory!)

vos=`find /etc/voms-admin/ -type d -printf "%f "`

for vo in $vos ; do

# Real VOs will have a voms.conf file in the directory
    if test -f /etc/voms-admin/$vo/service.properties ; then

# Set a variable to define the VO for this invocation
	export GLITE_INFO_SERVICE_VO=$vo
# The UniqueID needs to have the VO name appended to make it unique
	glite-info-service $config $sitename ${uniqueid}_$vo

    fi

done
