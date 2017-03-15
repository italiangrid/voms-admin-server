#!/bin/bash
set -ex

VO_NAME_PREFIX=${VOMS_VO_NAME_PREFIX:-"test"}
VO_COUNT=${VOMS_VO_COUNT:-1}
BASE_CORE_PORT=${VOMS_BASE_CORE_PORT:-15000}

configure_vo(){

  VO_NAME=${VO_NAME_PREFIX}_$1
  voms-configure install \
    --vo ${VO_NAME} \
    --dbtype mysql \
    --deploy-database \
    --dbname ${VO_NAME} \
    --dbusername $VOMS_DB_USERNAME \
    --dbpassword $VOMS_DB_PASSWORD \
    --dbhost db \
    --mail-from $VOMS_MAIL_FROM \
    --smtp-host mail \
    --membership-check-period 60 \
    --hostname ${VOMS_HOSTNAME} \
    --core-port $((${BASE_CORE_PORT}+$1)) \
    ${CONFIGURE_VO_OPTIONS}
}

for i in $(seq 0 ${VO_COUNT}); do
  configure_vo $i
done

if [[ -f "/etc/voms-admin/voms-admin-server.properties" ]]; then
  sed -i -e "s#localhost#${VOMS_HOSTNAME}#g" \
    /etc/voms-admin/voms-admin-server.properties
fi
