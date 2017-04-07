#!/bin/bash

VOMS_IMAGE=${VOMS_IMAGE:-"italiangrid/voms-dev:centos6"}
docker build -t ${VOMS_IMAGE} .
