#!/bin/bash
set -ex

UMD_REPO_RPM_URL="http://repository.egi.eu/sw/production/umd/4/centos7/x86_64/updates/umd-release-4.1.3-1.el7.centos.noarch.rpm"

yum clean all
yum install -y hostname epel-release
yum -y install ${UMD_REPO_RPM_URL}
yum -y update
yum -y install openssl java-1.8.0-openjdk-devel nc mysql voms-admin-server voms-admin-client igi-test-ca

