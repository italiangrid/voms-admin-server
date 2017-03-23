#!/bin/bash
set -ex

VOMS_GIT_REPO=${VOMS_GIT_REPO:-file:///voms}
VOMS_MYSQL_GIT_REPO=${VOMS_MYSQL_GIT_REPO:-file:///voms-mysql-plugin}

export CFLAGS=${VOMS_CFLAGS:--g -O0}
export CXXFLAGS=${VOMS_CXXFLAGS:--g -O0}

if [[ -n ${VOMS_BUILD_FROM_SOURCES} ]]; then
  git clone ${VOMS_GIT_REPO} voms
  pushd voms

  if [[ -n ${VOMS_GIT_BRANCH} ]]; then
    git checkout ${VOMS_GIT_BRANCH} 
  fi

  ./autogen.sh

  configure_cmd="./configure --program-prefix= \
      --prefix=/usr \
      --exec-prefix=/usr \
      --bindir=/usr/bin \
      --sbindir=/usr/sbin \
      --sysconfdir=/etc \
      --datadir=/usr/share \
      --includedir=/usr/include \
      --libdir=/usr/lib64 \
      --libexecdir=/usr/libexec \
      --localstatedir=/var \
      --sharedstatedir=/var/lib \
      --mandir=/usr/share/man \
      --infodir=/usr/share/info \
      --with-debug"

  eval ${configure_cmd}
  make && sudo make install
  popd
fi

if [[ -n ${VOMS_BUILD_MYSQL_PLUGIN} ]]; then
  git clone ${VOMS_MYSQL_GIT_REPO} voms-mysql-plugin
  pushd voms-mysql-plugin

  if [[ -n ${VOMS_MYSQL_GIT_BRANCH} ]]; then
    git checkout ${VOMS_MYSQL_GIT_BRANCH}
  fi

  ./autogen.sh

  ./configure --program-prefix= \
    --prefix=/usr \
    --exec-prefix=/usr \
    --bindir=/usr/bin \
    --sbindir=/usr/sbin \
    --sysconfdir=/etc \
    --datadir=/usr/share \
    --includedir=/usr/include \
    --libdir=/usr/lib64 \
    --libexecdir=/usr/libexec \
    --localstatedir=/var \
    --sharedstatedir=/var/lib \
    --mandir=/usr/share/man \
    --infodir=/usr/share/info
  make && sudo make install
  popd
fi

# Start VOMS server
voms -conf /etc/voms/test/voms.conf
tail -f /var/log/voms/voms.test
