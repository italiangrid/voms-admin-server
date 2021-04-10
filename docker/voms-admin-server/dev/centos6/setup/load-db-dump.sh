#!/bin/bash
set -ex

SCRIPTS_PREFIX=${SCRIPTS_PREFIX:-/scripts}
VO_NAME_PREFIX=${VOMS_VO_NAME_PREFIX:-"test"}
VO_COUNT=${VOMS_VO_COUNT:-1}

load_db_dump(){

  VO_NAME=${VO_NAME_PREFIX}_$1
  dump_file=${VO_NAME}.dump.sql
  if [[ -f ${SCRIPTS_PREFIX}/${dump_file} ]]; then
    echo "Loading dump file ${SCRIPTS_PREFIX}/${dump_file} for VO ${VO_NAME}"
    cat ${SCRIPTS_PREFIX}/${dump_file} | mysql -hdb -u${VOMS_DB_USERNAME} -p${VOMS_DB_PASSWORD} ${VO_NAME}
  else
    echo "${dump_file} not found"
  fi
}

for i in $(seq 0 ${VO_COUNT}); do
  load_db_dump $i
done
