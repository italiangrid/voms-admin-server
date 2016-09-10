#!/bin/bash
set -ex

voms-configure install \
  --vo test \
  --dbtype mysql \
  --deploy-database \
  --dbname $VOMS_DB_NAME \
  --dbusername $VOMS_DB_USERNAME \
  --dbpassword $VOMS_DB_PASSWORD \
  --dbhost db \
  --mail-from $VOMS_MAIL_FROM \
  --smtp-host mail \
  --membership-check-period 60 \
  --hostname ${VOMS_HOSTNAME} \
  --core-port 15000 \
  ${CONFIGURE_VO_OPTIONS}

if [[ -f "/etc/voms-admin/voms-admin-server.properties" ]]; then
  sed -i -e "s#localhost#dev.local.io#g" \
    /etc/voms-admin/voms-admin-server.properties
fi
