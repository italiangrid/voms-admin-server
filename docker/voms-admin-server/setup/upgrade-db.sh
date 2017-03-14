#!/bin/bash
set -ex

if [[ -n "$VOMS_UPGRADE_DB" ]]; then
  voms-db-util upgrade --vo test
fi
