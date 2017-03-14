#!/bin/bash
set -ex

yum clean all
yum install -y hostname epel-release
yum -y update
yum -y install yum-utils which make createrepo \
  wget rpm-build git tar \
  redhat-rpm-config \
  autoconf automake cmake gdb gcc-c++ libtool sudo \
  expat-devel pkgconfig openssl-devel gsoap-devel \
  mysql-devel voms-server voms-mysql-plugin \
  libxslt docbook-style-xsl doxygen bison

debuginfo-install -y voms-server voms-mysql-plugin

# Align it with centos7 rpmbuild naming
sed -i -e "s#^%dist .el6#%dist .el6.centos#" /etc/rpm/macros.dist

mkdir /code
chown -R voms:voms /code
cp /gdb-init ~voms/.gdbinit
chown voms:voms ~voms/.gdbinit
