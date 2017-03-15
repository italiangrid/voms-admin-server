#!/bin/bash
set -ex

VO_NAME_PREFIX=${VOMS_VO_NAME_PREFIX:-"test"}
VO_COUNT=${VOMS_VO_COUNT:-2}

upgrade_db() {
  VO_NAME=${VO_NAME_PREFIX}_$1
  if [[ -n "$VOMS_UPGRADE_DB" ]]; then
    voms-db-util upgrade --vo ${VO_NAME}
  fi
}

for i in $(seq 0 ${VO_COUNT}); do
  upgrade_db $i
done
