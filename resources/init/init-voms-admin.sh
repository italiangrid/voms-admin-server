# set -x

## voms installation prefix
PREFIX="${package.prefix}"

## jar file locations
VOMS_WS_LIBS="$PREFIX/usr/share/voms-admin/tools/lib"

## VOMS Java otpions
if [ -z $VOMS_WS_JAVA_OPTS ]; then
	VOMS_WS_JAVA_OPTS="-Xmx256m"
fi

## The VOMS service main class 
VOMS_WS_MAIN_CLASS="org.glite.security.voms.admin.server.Main"

## The VOMS service jar
VOMS_WS_JAR="$PREFIX/usr/share/java/voms-admin.jar"

## The VOMS service shutdown class
VOMS_WS_SHUTDOWN_CLASS="org.glite.security.voms.admin.server.ShutdownClient"

## ':' separated list of VOMS dependencies
VOMS_WS_DEPS="`ls -x $VOMS_WS_LIBS/*.jar | tr '\n' ':'`$VOMS_WS_JAR"

## The VOMS web service classpath
VOMS_WS_CP="$VOMS_WS_DEPS"

## Base VOMS startup command 
VOMS_WS_START_CMD="java $VOMS_WS_JAVA_OPTS -cp $VOMS_WS_DEPS $VOMS_WS_MAIN_CLASS"

## Base VOMS shutdown command
VOMS_WS_SHUTDOWN_CMD="java -cp $VOMS_WS_DEPS $VOMS_WS_SHUTDOWN_CLASS"

if [ -r "$PREFIX/etc/sysconfig/voms-admin" ]; then
	source "$PREFIX/etc/sysconfig/voms-admin"
fi

if [ ! -d "$PREFIX/var/lock/subsys" ]; then
    mkdir -p $PREFIX/var/lock/subsys
fi

configured_vos=`ls $CONF_DIR`

start() {
	if [ -z $1 ]; then
		vos=$configured_vos
	else
		vos=$1
	fi
	
	for vo in $vos ; do
	
		echo -n "Starting voms-admin( $vo ): "
		
        pid=$(find_pid $vo)

        if test "x$pid" != "x"; then
            check_pid $pid
            value=$?

            if [ $value -eq 0 ]; then
                failure "(already running)"
            else
                remove_pid_file $vo 
                failure "(removed stale lock file)"
            fi
		else
			## Start the service
			$VOMS_WS_START_CMD --vo $vo &
			pid=$!
			
			[ $pid -eq 0 ] && failure "(startup failed.)"
            create_pid_file "$pid" "$vo" && sleep 5 && success
		fi
	done
}


check_pid(){
    
    ps -p $1 > /dev/null 2>&1

    if [ $? -eq 0 ]; then
        ps -p $1 | grep "$VOMS_WS_MAIN_CLASS --vo $__vo" > /dev/null 2>&1
        [ $? -eq 0 ] && return 0
    fi

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
	echo "To be implemented"
}

status() {

	echo "To be implemented"
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
