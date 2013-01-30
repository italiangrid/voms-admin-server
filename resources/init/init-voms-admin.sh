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
### BEGIN INIT INFO
# Provides:          voms-admin
# Required-Start:    $network $remote_fs
# Required-Stop:     $network $remote_fs
# Default-Start:     3 4 5
# Default-Stop:      0 1 6
# Short-Description: The voms-admin service 
# Description:       The voms-admin service 
### END INIT INFO
#
### Chkconfig section
#
# chkconfig: 2345 97 97
# description: VOMS Admin service startup script
# processname: voms-admin
###
#set -x

## voms installation prefix
PREFIX="${package.prefix}"

source "$PREFIX/usr/share/voms-admin/scripts/voms-admin-server-env.sh"

voms_start_cmd="$PREFIX/usr/sbin/voms-admin-server"

if [ ! -d "$PREFIX/var/lock/subsys" ]; then
    mkdir -p $PREFIX/var/lock/subsys
fi

configured_vos=`ls $CONF_DIR`

check_vo_name() {

	for v in $configured_vos; do
		[ "$v" = "$1" ] && return 0
	done
	
	echo "VO $1 is not configured on this host!"
	exit 1

}

start() {
	if [ -z $1 ]; then
		vos=$configured_vos
	else
		check_vo_name $1
		vos=$1
	fi
	
	for vo in $vos ; do
	
		echo -n "Starting voms-admin( $vo ): "
		
        pid=$(find_pid $vo)

        if test "x$pid" != "x"; then
            check_pid "$pid" "$vo"
            value=$?

            if [ $value -eq 0 ]; then
                failure "(already running)"
            else
                remove_pid_file $vo 
                failure "(removed stale lock file)"
            fi
		else
			## Start the service
			start_cmd="$voms_start_cmd --vo $vo &"
			
			if [ -n "$VOMS_USER" ]; then
				
				if [ `id -u` -ne 0 ]; then
					failure "(you need to be root to start voms-admin service as the user $VOMS_USER)"
				else
					start_cmd="su $VOMS_USER -s /bin/bash -c '$voms_start_cmd --vo $vo' &"
				fi
			fi
			
			## Start the service 
			eval $start_cmd
			pid=$!
			
			[ $pid -eq 0 ] && failure "(startup failed.)"
			create_pid_file "$pid" "$vo" && sleep 1 && success
		fi
	done
}


check_pid(){
    
    ps -p $1 > /dev/null 2>&1

	[ $? -eq 0 ] && return 0

    return 1  
}

remove_pid_file(){
    ## This function takes one argument:
    ### $1 the VO name

    rm -f $PREFIX/var/lock/subsys/voms-admin.$1
    return $?
}

create_pid_file(){
    ## This function takes two args:
    ### $1 the pid 
    ### $2 the VO name
    echo "$1" > $PREFIX/var/lock/subsys/voms-admin.$2 
    return $?
}

find_pid(){

	local __vo=$1
	
	if [ -f $PREFIX/var/lock/subsys/voms-admin.$__vo ]; then
		
		read pid < $PREFIX/var/lock/subsys/voms-admin.$__vo
		ps -p $pid >/dev/null 2>&1
		
		if [ $? -ne 0 ]; then
			return 1
		else
			echo "$pid"
			return 0
		fi
	
	else
		pid=`ps -efww | grep "$VOMS_WS_MAIN_CLASS --vo $__vo" | grep -v grep | awk '{print $2}'`
		if [ -z $pid ]; then
			return 1
		else
            create_pid_file "$pid" "$__vo"
			echo "$pid"
			return 0
		fi
	fi
}


stop() {
	
	if [ -z $1 ]; then
		## Stop all configured VOs
		vos=$configured_vos
	else
		check_vo_name $1
		vos=$1
	fi
	
	for vo in $vos ; do
		echo -n "Stopping voms-admin( $vo ): "
		
		pid=$(find_pid $vo)
		
		if [ -z $pid ]; then
			failure "(not running)"
			continue
		fi
		
		## Stop the service 
		$VOMS_WS_SHUTDOWN_CMD --vo $vo
		
		if [ $? -ne 0 ]; then
			
			kill -KILL $pid
			
			if [ $? -ne 0 ]; then
				failure "(shutdown error)"
			else
                remove_pid_file $vo
			fi
		else
            remove_pid_file $vo
            success
		fi
	done
}

restart() {
	if [ -z $1 ]; then
		vos=$configured_vos
	else
		vos=$1
		check_vo_name $1
	fi
	
	for vo in $vos ; do
		stop  $vo && sleep 5 && start $vo
	done
}

status() {
	if [ -z $1 ]; then
		vos=$configured_vos
	else
		check_vo_name $1
		vos=$1
	fi
	
	found_failure=0
	for vo in $vos ; do
		echo -n "Checking voms-admin status( $vo ): "
		
		pid=$(find_pid $vo)
		
		if [ -z $pid ]; then
			failure "(not running)"
			found_failure=1
			continue
		else
			success "running ($pid)"
		fi
	done
	
	[ $found_failure -eq 0 ] || exit 1
}

success()
{

    if [ ! -z "$1" ]; then
        echo -n "$1"
    fi
    
    RES_COL=60
    echo -en "\\033[${RES_COL}G"
    echo -n "[  "
    echo -en "\\033[1;32m"
    echo -n OK
    echo -en "\\033[0;39m"
    echo -n "  ]"
    echo -ne "\r"
    echo
    
    return 0
}

failure()
{
    rc=$?

    if [ ! -z "$1" ]; then
        echo -n "$1"
    fi

    RES_COL=60
    echo -en "\\033[${RES_COL}G"
    echo -n "["
    echo -en "\\033[1;31m"
    echo -n FAILED
    echo -en "\\033[0;39m"
    echo -n "]"
    echo -ne "\r"
    echo

    return $rc
}

case "$1" in
	start)
		start "$2"
		;;
	
	stop)
		stop "$2"
		;;
	
	status)
		status "$2"
		;;
	
	restart)
		restart "$2"
		;;
	*)
		echo "Usage: $0 {start|stop|restart|status}"
		RETVAL=1
		;;
esac

exit $RETVAL