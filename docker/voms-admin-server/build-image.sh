#!/bin/bash

VOMS_ADMIN_SERVER_IMAGE=${VOMS_ADMIN_SERVER_IMAGE:-"italiangrid/voms-admin-server-dev"}

docker build --no-cache=true -t ${VOMS_ADMIN_SERVER_IMAGE} .
