set -e

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
VOMS_WS_JAR="$PREFIX/usr/share/java/glite-security-voms-admin.jar"

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

start() {
	if [ -z $1 ]; then
		## Start all configured VOs 
		echo "To be implemented"
	else
		
		## Check whether the VOMS admin service instance is running
		
		## Start the service
		$VOMS_WS_START_CMD --vo $1
	fi

}

stop() {

	if [ -z $1 ]; then
		## Stop all configured VOs
		echo "To be implemented"
	else
		
		## Stop the service if running
		$VOMS_WS_SHUTDOWN_CMD --vo $1
	fi
}

restart() {
	echo "To be implemented"
}

status() {

	echo "To be implemented"
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