#!/bin/bash

set -ex

JRE_HOME=${JRE_HOME:-/usr/lib/jvm/jre-1.8.0-openjdk.x86_64}
JDK_HOME=${JDK_HOME:-/usr/lib/jvm/java-1.8.0-openjdk.x86_64}

update-alternatives --set java ${JRE_HOME}/bin/java
update-alternatives --set javac ${JDK_HOME}/bin/javac
