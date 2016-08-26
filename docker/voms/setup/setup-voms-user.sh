#!/bin/bash
set -ex

VOMS_USER=${VOMS_USER:-voms}
VOMS_USER_UID=${VOMS_USER_UID:-1234}

# Add voms user to the sudoers
echo '%wheel ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers
adduser --uid ${VOMS_USER_UID} ${VOMS_USER}
usermod -a -G wheel ${VOMS_USER}
