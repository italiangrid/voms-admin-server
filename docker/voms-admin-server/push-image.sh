#!/bin/bash
set -ex

VOMS_ADMIN_SERVER_IMAGE=${VOMS_ADMIN_SERVER_IMAGE:-"italiangrid/voms-admin-server"}

# The current script directory
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}"  )" && pwd  )"
cd ${DIR}

if [ -n ${DOCKER_REGISTRY_HOST} ]; then
    docker tag  ${VOMS_ADMIN_SERVER_IMAGE} ${DOCKER_REGISTRY_HOST}/${VOMS_ADMIN_SERVER_IMAGE}
    docker push ${DOCKER_REGISTRY_HOST}/${VOMS_ADMIN_SERVER_IMAGE}
else 
    docker push ${VOMS_ADMIN_SERVER_IMAGE}
fi
