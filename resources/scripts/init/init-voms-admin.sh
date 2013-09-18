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

## return value
RETVAL=0

## voms installation prefix
PREFIX="${package.prefix}"

if [ -r "${PREFIX%/}/etc/sysconfig/voms-admin" ]; then
	source "${PREFIX%/}/etc/sysconfig/voms-admin"
fi

## Server startup Java options
if [ -z "$VOMS_JAVA_OPTS" ]; then
  VOMS_JAVA_OPTS="-Xms256m -Xmx512m -XX:MaxPermSize=512m"
fi

## The VOMS container jar
VOMS_SERVER_JAR="${PREFIX%/}/usr/share/java/voms-container.jar"

if [ -z "$VOMS_DEBUG_PORT" ]; then
	VOMS_DEBUG_PORT=8998
fi

if [ -z "$VOMS_DEBUG_SUSPEND" ]; then
	VOMS_DEBUG_SUSPEND="n"
fi

if [ ! -z "$VOMS_DEBUG" ]; then
	VOMS_JAVA_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=$VOMS_DEBUG_PORT,suspend=$VOMS_DEBUG_SUSPEND $VOMS_JAVA_OPTS"
fi

## Base VOMS startup command 
VOMS_START_CMD="java $VOMS_JAVA_OPTS -jar $VOMS_SERVER_JAR"

## Oracle environment
ORACLE_ENV="LD_LIBRARY_PATH=$ORACLE_LIBRARY_PATH TNS_ADMIN=$TNS_ADMIN"

if [ ! -d "${PREFIX%/}/var/lock/subsys" ]; then
    mkdir -p ${PREFIX%/}/var/lock/subsys
fi

if [ -z "$VOMS_STATUS_PORT" ]; then
	VOMS_STATUS_PORT=8088
fi

pid_file="${PREFIX%/}/var/lock/subsys/voms-container"
deploy_dir="${PREFIX%/}/usr/share/voms-admin/vo.d"
configured_vos=`find $CONF_DIR -maxdepth 1 -mindepth 1 -type d -exec basename {} \;`

print_vo_status(){
	status_vo=$(echo $1 | tr -d ' ')
	
	vo_status=$(curl -s http://localhost:$VOMS_STATUS_PORT/status)
	
	if [ $? -ne 0 ]; then
		failure "error getting vo status"
		exit 1
	fi
	
	if [ ! -z "$status_vo" ]; then
		echo -n "VO($status_vo): ";
		check_vo_name "$status_vo"
	fi

	while read -r line; do
		IFS=":" read -ra vo_entry <<< "$line";
		vo_name=$(echo "${vo_entry[0]}" | tr -d ' ');
		vo_state=$(echo "${vo_entry[1]}" | tr -d ' ');
		if [ -z "$status_vo" ]; then
			echo -n "VO ($vo_name):";
			[ "$vo_state" == "active" ] && up "(running)" || down "(not running)" ;
		else
			if [ "$vo_name" == "$status_vo" ]; then
				[ "$vo_state" == "active" ] && up "(running)" || down "(not running)" ;
			fi
		fi
	done <<< "$vo_status"
}

deploy_vos() {
	vos=$1
	
	if [ -z "$vos" ]; then
		vos=$configured_vos
	fi
	
	for vo in $vos; do
		if [ $# -gt 1 ]; then
			echo -n "Deploying vo($vo): "
		fi
		check_vo_name "$vo"
		if [ -f $deploy_dir/$vo ]; then
			if [ $# -gt 1 ]; then
				success "already deployed"
			fi
		else
			touch $deploy_dir/$vo
			if [ $# -gt 1 ]; then
				success
			fi
		fi
	done
}

undeploy_vos() {
	vos=$1
	
	if [ -z "$vos" ]; then
		vos=$configured_vos
	fi
	
	for vo in $vos; do
		if [ $# -gt 1 ]; then
			echo -n "Undeploying vo($vo): "
		fi
		if [ -f $deploy_dir/$vo ]; then
			rm -f $deploy_dir/$vo
			if [ $# -gt 1 ]; then
				success
			fi
		else
			if [ $# -gt 1 ]; then
				success "was not deployed"
			fi
		fi
	done
}

check_vo_name() {

	for v in $configured_vos; do
		[ "$v" = "$1" ] && return 0
	done
	
	failure "not configured"
	exit 1

}

check_container_is_running() {

	pid=$(find_pid)

	if test "x$pid" != "x"; then
		return 0
	fi 
	
	return 1
}

start_container(){
	
	## Start the container
	start_cmd="$ORACLE_ENV $VOMS_START_CMD &"
	
	if [ -n "$VOMS_USER" ]; then
		if [ `id -u` -ne 0 ]; then
			failure "(you need to be root to start voms-admin service as the user $VOMS_USER)"
		else
			start_cmd="su $VOMS_USER -s /bin/bash -c '$ORACLE_ENV $VOMS_START_CMD &'"
		fi
	fi
	
	eval "$start_cmd"
	pid=$(find_pid)
	[ $pid -eq 0 ] && failure "(container startup failed.)"
	return 0
}

stop_container(){
	
	pid=$(find_pid)
	if [ -z $pid ]; then
		failure "(not running)"
	else
		kill $pid 2>&1 >/dev/null
		if [ $? -ne 0 ]; then
			failure "(shutdown error)"
		else
			remove_pid_file $vo
			return 0
		fi
	fi
}

deploy_dir_is_empty(){
	deployed_vos=`find $deploy_dir -maxdepth 1 -mindepth 1 -type f -exec basename {} \;`
	if [ -z "$deployed_vos" ]; then
		return 0;
	else
		return 1;
	fi
}

start() {
  
	if [ -z "$configured_vos" ]; then
	  	echo -n "Starting voms-admin: "
		failure "(no vos configured)"
		return $?
	fi
	
	check_container_is_running
	if [ $? -eq 0 ]; then
    	if [ -z "$1" ]; then
	    	echo -n "Starting voms-admin: "
		  	failure "(already running)"
    	else
      		deploy_vos "$1" "verbose"
    	fi
	else
		if [  ! -z "$1" ]; then
      		deploy_vos "$1" "verbose"
    	else
      		deploy_dir_is_empty
		  	if [ $? -eq 0 ]; then
			  deploy_vos "$configured_vos" "verbose"
		  	else
				deployed_vos=`find $deploy_dir -maxdepth 1 -mindepth 1 -type f -exec basename {} \; | sort`
				echo "Deployed VOs:"
				for v in $deployed_vos; do
					echo "	$v" 
				done
			fi
    	fi
	  	echo -n "Starting voms-admin: "
		start_container && success
	fi 
}

remove_pid_file(){
    rm -f $pid_file
    return $?
}

create_pid_file(){
    ## This function takes one argument:
    ### $1 the pid 
    echo "$1" > $pid_file
    return $?
}

search_pid_in_ps(){
	pid=`ps -efww | grep "$VOMS_START_CMD" | grep -v grep | awk '{print $2}'`
	if [ -z $pid ]; then
		return 1
	else
		create_pid_file "$pid"
		return 0
	fi	
}

find_pid(){
	if [ -f $pid_file ]; then
		pid=$(<$pid_file)
		ps -p $pid >/dev/null 2>&1
		
		if [ $? -ne 0 ]; then
			search_pid_in_ps
			val=$?
			if [ $val -eq 0 ]; then
				echo "$(<$pid_file)"
				return 0
			else
				return 1
			fi 
		else
			echo "$pid"
			return 0
		fi
	else
		search_pid_in_ps
		val=$?
		if [ $val -eq 0 ]; then
			echo "$(<$pid_file)"
			return 0
		else
			return 1
		fi
	fi
}


stop() {
	
  vos=$1

	check_container_is_running
	
	if [ $? -eq 0 ]; then
    	if [ ! -z "$vos" ]; then
      		undeploy_vos "$vos" "verbose"
    	else
	    	echo -n "Stopping voms-admin: "
		  	stop_container || failure "(error stopping container)"
		  	success
    	fi
	else 
    	if [ ! -z "$vos" ]; then
      		undeploy_vos "$vos" "verbose"
    	fi
	 	echo -n "Stopping voms-admin: "
		failure "(not running)"
	fi
}

restart() {
	echo -n "Stopping VOMS Admin:"
	stop_container && success
	sleep 5
	echo -n "Starting VOMS Admin:"
	start_container && success
}

status() {
	check_container_is_running
	if [ $? -eq 0 ]; then
		if[ -z $1 ]; then
			success "VOMS admin container"
		fi 
		print_vo_status "$1"
	else
		RETVAL=1
		failure "VOMS admin container:(not running)"
	fi
}

down() {
    if [ ! -z "$1" ]; then
        echo -n "$1"
    fi
    
    RES_COL=60
    echo -en "\\033[${RES_COL}G"
    echo -n "[ "
    echo -en "\\033[1;31m"
    echo -n DOWN
    echo -en "\\033[0;39m"
    echo -n " ]"
    echo -ne "\r"
    echo
    
		RETVAL=1

    return 0
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

up()
{

    if [ ! -z "$1" ]; then
        echo -n "$1"
    fi
    
    RES_COL=60
    echo -en "\\033[${RES_COL}G"
    echo -n "[  "
    echo -en "\\033[1;32m"
    echo -n UP
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
		restart
		;;
	
	deploy)
	  deploy_vos "$2" "verbose"
	  ;;
	
	undeploy)
	  undeploy_vos "$2" "verbose"
	  ;;
	   
	*)
		echo "Usage: $0 {start|stop|restart|status|deploy|undeploy}"
		RETVAL=1
		;;
esac

exit $RETVAL
