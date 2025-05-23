#!/usr/bin/env python3
#
# Copyright (c) Members of the EGEE Collaboration. 2006-2009.
# See http://www.eu-egee.org/partners/ for details on the copyright holders.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Authors:
# 	Andrea Ceccanti (INFN)
#

import sys
import os
import argparse
from voms_shared import VOMSDefaults, X509Helper, get_oracle_env

usage = """%(prog)s [options] command

Commands:
  check-connectivity:  checks database connection

  deploy:    deploys the VOMS database for a given VO
  undeploy:  undeploys the VOMS database for a given VO
  upgrade:   upgrades the VOMS database for a given VO

  add-admin:    creates an administrator with full privileges for a given VO
  remove-admin: removes an administrator from a given VO

  grant-read-only-access:  creates ACLs so that VO structure is readable for any authenticated user
"""

parser = argparse.ArgumentParser(usage=usage)
commands = ["deploy", "undeploy", "upgrade", "check-connectivity",
            "grant-read-only-access", "add-admin", "remove-admin"]

parser.add_argument("command", choices=commands)


def setup_cl_options():
    parser.add_argument(
        "--vo",
        dest="vo",
        help="the VO for which database operations are performed",
        metavar="VO"
    )
    parser.add_argument(
        "--dn",
        dest="admin_dn",
        help="the DN of the administrator certificate", metavar="DN"
    )
    parser.add_argument(
        "--ca",
        dest="admin_ca",
        help="the DN of the CA that issued the administrator certificate",
        metavar="DN"
    )
    parser.add_argument(
        "--email",
        dest="admin_email",
        help="the EMAIL address of the administrator", metavar="EMAIL"
    )
    parser.add_argument(
        "--cert",
        dest="admin_cert",
        help="the x.509 CERTIFICATE of the administrator being created",
        metavar="CERTIFICATE"
    )
    parser.add_argument(
        "--ignore-cert-email",
        dest="admin_ignore_cert_email",
        action="store_true",
        help="ignores the email address in the certificate passed in with the --cert option"
    )


def error_and_exit(msg):
    print(msg, file=sys.stderr)
    sys.exit(1)


def build_classpath():
    jars = VOMSDefaults.voms_admin_libs

    if len(jars) == 0:
        raise ValueError("voms-admin jar files not found!")

    jars.append(VOMSDefaults.voms_admin_jar)
    jars.append(VOMSDefaults.voms_admin_classes)

    return ":".join(jars)


def do_basic_command(options, command):
    cmd = f"{get_oracle_env()} "\
        f"java -cp {build_classpath()} {VOMSDefaults.schema_deployer_class} " \
        f"--command {command} --vo {options.vo}"

    status = os.system(cmd)
    sys.exit(os.WEXITSTATUS(status))


def do_add_admin(options):
    email = None

    if options.admin_cert:
        cert = X509Helper(options.admin_cert)
        dn = cert.subject
        ca = cert.issuer

        if not options.admin_ignore_cert_email:
            email = cert.email
    else:
        dn = options.admin_dn
        ca = options.admin_ca
        email = options.admin_email

    if not email:
        print("WARNING: No email was specified for this administrator! "
              "The new administrator will not receive VOMS Admin notifications")

    cmd = f"{get_oracle_env()} " \
        f"java -cp {build_classpath()} {VOMSDefaults.schema_deployer_class} " \
        f"--command add-admin " \
        f"--vo {options.vo} --dn {dn} --ca {ca}"
    if email:
        cmd = f"{cmd} --email '{email}'"

    status = os.system(cmd)
    sys.exit(os.WEXITSTATUS(status))


def do_remove_admin(options):
    if options.admin_cert:
        cert = X509Helper(options.admin_cert)
        dn = cert.subject
        ca = cert.issuer
    else:
        dn = options.admin_dn
        ca = options.admin_ca
    cmd = f"{get_oracle_env()} " \
        f"java -cp {build_classpath()} {VOMSDefaults.schema_deployer_class} " \
        f"--command remove-admin " \
        f"--vo {options.vo} --dn {dn} --ca {ca}"
    status = os.system(cmd)
    sys.exit(os.WEXITSTATUS(status))


def check_args(options):
    if not options.vo:
        error_and_exit("Please specify a VO with the --vo option.")

    if options.command in ("add-admin", "remove-admin"):
        if (not options.admin_dn or not options.admin_ca) and not options.admin_cert:
            error_and_exit(
                "Please specify an administrator either providing a certificate"
                " with the --cert option, or with the --dn and --ca options.")

        if options.admin_cert and (options.admin_dn or options.admin_ca):
            error_and_exit(
                "The --cert option and --dn,--ca are mutually exclusive.")

        if not options.admin_cert and options.admin_ignore_cert_email:
            error_and_exit(
                "The --ignore-cert-email must be used together with the --cert "
                "option.")


def main():
    setup_cl_options()
    args = parser.parse_args()
    check_args(args)

    command = args.command

    if command == "add-admin":
        do_add_admin(args)
    elif command == "remove-admin":
        do_remove_admin(args)
    else:
        do_basic_command(args, command)


if __name__ == '__main__':
    main()
