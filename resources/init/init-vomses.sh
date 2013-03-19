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
#   Andrea Ceccanti (INFN)
#
### BEGIN INIT INFO
# Provides:          vomses
# Required-Start:    $network $remote_fs
# Required-Stop:     $network $remote_fs
# Default-Start:     3 4 5
# Default-Stop:      0 1 6
# Short-Description: The vomses index service 
# Description:       The vomses index service 
### END INIT INFO
#
### Chkconfig section
#
# chkconfig: 345 97 97
# description: VOMSES index service startup script
# processname: vomses
###

## voms installation prefix
PREFIX="${package.prefix}"

source "$PREFIX/usr/share/voms-admin/scripts/voms-admin-server-env.sh"

pidfile_name="$PREFIX/var/lock/subsys/vomses.pid"

if [ ! -d "$PREFIX/var/lock/subsys" ]; then
    mkdir -p $PREFIX/var/lock/subsys
fi

vomses_start_cmd="$PREFIX/usr/sbin/vomses"

check_pid(){
    
    ps -p $1 > /dev/null 2>&1

    if [ $? -eq 0 ]; then
        ps -p $1 | grep "$VOMSES_MAIN_CLASS" > /dev/null 2>&1
        [ $? -eq 0 ] && return 0
    fi

    return 1  
}


find_pid(){

  
  if [ -f $pidfile_name ]; then
    
    read pid < $pidfile_name
    ps -p $pid >/dev/null 2>&1
    
    if [ $? -ne 0 ]; then
      return 1
    else
      echo "$pid"
      return 0
    fi
  
  else
    pid=`ps -efww | grep "$VOMSES_MAIN_CLASS" | grep -v grep | awk '{print $2}'`
    if [ -z $pid ]; then
      return 1
    else
            echo "$pid" > $pidfile_name
      echo "$pid"
      return 0
    fi
  fi
}


start(){

    echo -n "Starting vomses: "
  pid=$(find_pid)
  if test "x$pid" != "x"; then
    check_pid $pid
    value=$?
    
    if [ $value -eq 0 ]; then
      failure "(already running)"
    else
      remove_pid_file
      failure "(removed stale lock file)"
    fi
  else
     
    ## Start the service
    start_cmd="$vomses_start_cmd &"
    
    if [ -n "$VOMS_USER" ]; then
      if [ `id -u` -ne 0 ]; then
        failure "(you need to be root to start the vomses service as the user $VOMS_USER)"
      else
        start_cmd="su $VOMS_USER -s /bin/bash -c '$vomses_start_cmd' &"
      fi
    fi 
    
    eval $start_cmd
    pid=$!
    [ $pid -eq 0 ] && failure "(startup failed.)"
    create_pid_file "$pid" && sleep 1 && success
  fi
  
}


remove_pid_file(){
  rm -f $pidfile_name
  return $?
}

create_pid_file(){
  echo "$1" > $pidfile_name
  return $?
}

stop() {
  echo -n "Stopping vomses: "
  pid=$(find_pid)
    
  if [ -z $pid ]; then
    failure "(not running)"
    return 1
  fi 
  
  kill -KILL $pid
  if [ $? -ne 0 ]; then
    failure "(shutdown error)"
  else
    remove_pid_file
    success
  fi 
}

restart() {

  stop && sleep 2 && start 

}


status() {
  echo -n "Checking vomses status: "
  pid=$(find_pid)
  
  if [ -z $pid ]; then
    failure "(not running)"
    return 1
  else
    success "running ($pid)"
  fi 
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
    status 
    ;;
  
  restart)
    restart 
    ;;
  *)
    echo "Usage: $0 {start|stop|restart|status}"
    RETVAL=1
    ;;
esac

exit $RETVAL