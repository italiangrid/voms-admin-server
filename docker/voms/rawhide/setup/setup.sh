#!/bin/bash
set -ex

dnf clean all
dnf install -y hostname yum-utils make createrepo \
  wget rpm-build git tar \
  redhat-rpm-config \
  autoconf automake cmake gdb gcc-c++ libtool sudo \
  expat-devel pkgconfig openssl-devel gsoap-devel \
  mysql-devel voms-server voms-mysql-plugin \
  libxslt docbook-style-xsl doxygen bison

# debuginfo-install -y voms-server voms-mysql-plugin

mkdir /code
chown -R voms:voms /code
cp /gdb-init ~voms/.gdbinit
chown voms:voms ~voms/.gdbinit
