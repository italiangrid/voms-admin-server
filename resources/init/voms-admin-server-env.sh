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
###
#set -x

## voms installation prefix
PREFIX="${package.prefix}"

## jar file locations
VOMS_WS_LIBS="$PREFIX/var/lib/voms-admin/lib"

## jar deps with wildcards
VOMS_WS_JARS=$PREFIX'/var/lib/voms-admin/lib/*'

##  VOMS Java otpions
if [ -z $VOMS_WS_JAVA_OPTS ]; then
	VOMS_WS_JAVA_OPTS="-Xmx48m -XX:MaxPermSize=75m"
fi

## VOMSES Java options
if [ -z $VOMSES_JAVA_OPTS ]; then
  VOMSES_JAVA_OPTS="-Xmx32m -XX:MaxPermSize=32m"
fi

## The VOMS service main class 
VOMS_WS_MAIN_CLASS="org.glite.security.voms.admin.server.Main"

## The VOMSES index service main class
VOMSES_MAIN_CLASS="org.glite.security.voms.admin.server.vomses.VomsesMain"

## The VOMS service jar
VOMS_WS_JAR="$PREFIX/usr/share/java/voms-admin.jar"

## The VOMS service shutdown class
VOMS_WS_SHUTDOWN_CLASS="org.glite.security.voms.admin.server.ShutdownClient"

## The VOMS web service classpath
VOMS_WS_CP="$VOMS_WS_JARS:$VOMS_WS_JAR"

## Base VOMS startup command 
VOMS_WS_START_CMD="java $VOMS_WS_JAVA_OPTS -cp $VOMS_WS_CP $VOMS_WS_MAIN_CLASS"

## VOMSES app startup command
VOMSES_START_CMD="java $VOMSES_JAVA_OPTS -cp $VOMS_WS_CP $VOMSES_MAIN_CLASS"

## Base VOMS shutdown command
VOMS_WS_SHUTDOWN_CMD="java -cp $VOMS_WS_CP $VOMS_WS_SHUTDOWN_CLASS"

if [ -r "$PREFIX/etc/sysconfig/voms-admin" ]; then
	source "$PREFIX/etc/sysconfig/voms-admin"
fi
