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

## Oracle environment

ORACLE_ENV="LD_LIBRARY_PATH=$ORACLE_LIBRARY_PATH TNS_ADMIN=$TNS_ADMIN"

## Server startup Java options
if [ -z $VOMS_JAVA_OPTS ]; then
  VOMS_JAVA_OPTS="-Xmx256m -XX:MaxPermSize=512m"
fi

## The VOMS container jar
VOMS_SERVER_JAR="$PREFIX/usr/share/java/voms-container.jar"

## Base VOMS startup command 
VOMS_START_CMD="java $VOMS_JAVA_OPTS -jar $VOMS_SERVER_JAR"

if [ -r "$PREFIX/etc/sysconfig/voms-admin" ]; then
	source "$PREFIX/etc/sysconfig/voms-admin"
fi

if [ ! -d "$PREFIX/var/lock/subsys" ]; then
    mkdir -p $PREFIX/var/lock/subsys
fi

pid_file="$PREFIX/var/lock/subsys/voms-container"
deploy_dir="$PREFIX/usr/share/voms-admin/vo.d"
configured_vos=`find $CONF_DIR -maxdepth 1 -mindepth 1 -type d -exec basename {} \;`

deploy_vos() {
	for vo in $1; do
		touch $deploy_dir/$vo
	done
}

undeploy_vos() {
	for vo in $1; do
		rm -f $deploy_dir/$vo
	done
}

check_vo_name() {

	for v in $configured_vos; do
		[ "$v" = "$1" ] && return 0
	done
	
	echo "VO $1 is not configured on this host!"
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
	start_cmd="$ORACLE_ENV $VOMS_START_CMD"
	
	if [ -n "$VOMS_USER" ]; then
		if [ `id -u` -ne 0 ]; then
			failure "(you need to be root to start voms-admin service as the user $VOMS_USER)"
		else
			start_cmd="su $VOMS_USER -s /bin/bash -c '$ORACLE_ENV $VOMS_START_CMD'"
		fi
	fi
	
	eval "$start_cmd &"
	pid=$!
	[ $pid -eq 0 ] && failure "(container startup failed.)"
	create_pid_file "$pid"
	return 0
}

stop_container(){
	
	pid=$(find_pid)
	if [ -z $pid ]; then
		failure "(not running)"
	else
		kill $pid
		if [ $? -ne 0 ]; then
			failure "(shutdown error)"
		else
			remove_pid_file $vo
			return 0
		fi
	fi
}

deploy_dir_is_empty(){
	if [ -e "$deploy_dir/*" ]; then
		return 1;
	else
		return 0;
	fi
}

start() {
	
	echo -n "Starting voms-admin: "
	if [ -z "$configured_vos" ]; then
		failure "(no vos configured)"
		return $?
	fi
	
	check_container_is_running
	if [ $? -eq 0 ]; then
		failure "(already running)"
	else
		deploy_dir_is_empty
		if [ $? -eq 0 ]; then
			deploy_vos "$configured_vos"
		fi 
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
	
	echo -n "Stopping voms-admin: "
	check_container_is_running
	
	if [ $? -eq 0 ]; then
		stop_container || failure "(error stopping container)"
		success
	else 
		failure "(not running)"
	fi
}

restart() {
	stop_container && sleep 5 && start_container
}

status() {
	success "not implemented"
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
		start 
		;;
	
	stop)
		stop
		;;
	
	status)
		status "$2"
		;;
	
	restart)
		restart
		;;
	
	deploy)
	  deploy "$2"
	  ;;
	
	undeploy)
	  undeploy "$2"
	  ;;
	   
	*)
		echo "Usage: $0 {start|stop|restart|status|deploy|undeploy}"
		RETVAL=1
		;;
esac

exit $RETVAL