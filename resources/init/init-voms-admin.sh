set -e

## voms installation prefix
PREFIX="${voms_prefix}"

## jar file locations
VOMS_WS_LIBS="$PREFIX/usr/share/voms-admin/tools/lib"

## VOMS Java otpions
if [ -z $VOMS_WS_JAVA_OPTS ]; then
	VOMS_WS_JAVA_OPTS="-Xmx256m"
fi

## The VOMS service main class 
VOMS_WS_MAIN_CLASS="org.glite.security.voms.admin.server.Main"

## ':' separated list of VOMS dependencies
VOMS_WS_DEPS=`ls -x $VOMS_WS_LIBS/*.jar | tr '\n' ':'`

## The VOMS web service classpath
VOMS_WS_CP="$VOMS_WS_DEPS"

## Base VOMS startup command 
VOMS_WS_START_CMD="java $VOMS_WS_JAVA_OPTS -cp $VOMS_WS_DEPS $VOMS_WS_MAIN_CLASS --prefix $PREFIX --vo VO_NAME"

## Base VOMS shutdown command
VOMS_WS_SHUTDOWN_CMD="