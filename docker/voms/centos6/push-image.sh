#!/bin/bash
set -ex

VOMS_IMAGE=${VOMS_IMAGE:-"italiangrid/voms-dev:centos6"}

# The current script directory
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}"  )" && pwd  )"
cd ${DIR}

if [ -n ${DOCKER_REGISTRY_HOST} ]; then
    docker tag ${VOMS_IMAGE} ${DOCKER_REGISTRY_HOST}/${VOMS_IMAGE}
    docker push ${DOCKER_REGISTRY_HOST}/${VOMS_IMAGE}
else 
    docker push ${VOMS_IMAGE}
fi
