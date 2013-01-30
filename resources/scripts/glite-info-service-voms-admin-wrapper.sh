#!/bin/bash

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
