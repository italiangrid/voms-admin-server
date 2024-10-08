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
#     Andrea Ceccanti (INFN)

import argparse
import sys
import subprocess
import re
import socket

usage = """%prog [options] command

Commands:
  create_db:        creates a MySQL database and read/write grants for the VOMS service
                    based on the given options
  drop_db:          drops a MySQL database
  grant_rw_access:  Creates a read/write grant on an existing VOMS database for the user
                    specified in the options
  grant_ro_access:  Creates a read-only grant on an existing VOMS database for the user
                    specified in the options
"""
parser = argparse.ArgumentParser(usage=usage)


def setup_cl_options():
    parser.add_argument(
        "--dbauser",
        dest="dbauser",
        help="Sets MySQL administrator user to USER",
        metavar="USER",
        default="root"
    )
    parser.add_argument(
        "--dbapwd",
        dest="dbapwd",
        help="Sets MySQL administrator password to PWD",
        metavar="PWD"
    )
    parser.add_argument(
        "--dbapwdfile",
        dest="dbapwdfile",
        help="Reads MySQL administrator password from FILE",
        metavar="FILE")
    parser.add_argument(
        "--dbusername",
        dest="username",
        help="Sets the VOMS MySQL username to be created as USER",
        metavar="USER")
    parser.add_argument(
        "--vomshost",
        dest="voms_host",
        help="Sets the HOST where VOMS is running",
        metavar="HOST"
    )
    parser.add_argument(
        "--dbpassword",
        dest="password",
        help="Sets the VOMS MySQL password for the user to be created as PWD",
        metavar="PWD"
    )
    parser.add_argument(
        "--dbname",
        dest="dbname",
        help="Sets the VOMS database name to DBNAME", metavar="DBNAME"
    )
    parser.add_argument(
        "--dbhost",
        dest="host",
        help="Sets the HOST where MySQL is running", metavar="HOST",
        default="localhost"
    )
    parser.add_argument(
        "--dbport",
        dest="port",
        help="Sets the PORT where MySQL is listening", metavar="PORT",
        default="3306"
    )
    parser.add_argument(
        "--mysql-command",
        dest="command",
        help="Sets the MySQL command to CMD",
        metavar="CMD", default="mysql"
    )


def error_and_exit(msg):
    print(msg, file=sys.stderr)
    sys.exit(1)


def build_mysql_command_preamble(options):
    if options.dbapwdfile:
        try:
            with open(options.dbapwdfile, encoding="utf-8") as f:
                dbapwd = f.read()
        except IOError as e:
            error_and_exit(e.strerror)
    else:
        dbapwd = options.dbapwd

    mysql_cmd = "{command} -u{dbauser} --host {host} --port {port}"
    mysql_cmd = mysql_cmd.format(**options)
    if dbapwd:
        mysql_cmd = f"{mysql_cmd} -p{dbapwd}"
    return mysql_cmd


def db_exists(options):
    mysql_cmd = build_mysql_command_preamble(options)
    with subprocess.Popen(mysql_cmd, shell=True, stdin=subprocess.PIPE,
                          stderr=subprocess.PIPE) as mysql_proc:
        try:
            print(f"use {options.dbname};", file=mysql_proc.stdin)
        except IOError as e:
            err_msg = mysql_proc.stderr.read()
            error_and_exit(
                f"Error checking database existence: {e}. {err_msg}")

        status = mysql_proc.wait()
        if status == 0:
            return True

        err_msg = mysql_proc.stderr.read()
    match = re.match("ERROR 1049", err_msg.strip())
    if match:
        return False
    error_and_exit(f"Error checking schema existence: {err_msg}")


def create_db(options):
    print(f"Creating database {options.dbname}")
    if db_exists(options):
        print(f"Schema for database {options.dbname} already exists, "
              "will not create it...")
    else:
        mysql_cmd = build_mysql_command_preamble(options)
        # The database is not there, let's create it
        with subprocess.Popen(mysql_cmd, shell=True,
                              stdin=subprocess.PIPE) as mysql_proc:
            print("create database {options.dbname};", file=mysql_proc.stdin)
            status = mysql_proc.wait()
        if status != 0:
            error_and_exit("Error creating MySQL database {options.dbname}: "
                           "mysql_proc.stdout.read()")

    grant_rw_access(options)
    print("Done.")


def drop_db(options):
    print(f"Dropping database {options.dbname}")
    if not db_exists(options):
        print(f"Schema for database {options.dbname} does not exist, "
              "exiting...")
        sys.exit(1)
    else:
        mysql_cmd = build_mysql_command_preamble(options)
        with subprocess.Popen(
                mysql_cmd, shell=True, stdin=subprocess.PIPE) as mysql_proc:
            print(f"drop database {options.dbname};", file=mysql_proc.stdin)
            status = mysql_proc.wait()
            if status != 0:
                output = mysql_proc.stdout.read()
                msg = f"Error dropping MySQL database {options.dbname}: {output}"
                error_and_exit(msg)
    print("Done.")


def grant_rw_access(options):
    print(f"Granting user {options.username} read/write access on "
          f"database {options.dbname}")
    mysql_cmd = build_mysql_command_preamble(options)

    if len(options.username) > 16:
        error_and_exit(
            "MySQL database accont names cannot be longer than 16 characters.")

    if db_exists(options):
        with subprocess.Popen(mysql_cmd, shell=True,
                              stdin=subprocess.PIPE) as mysql_proc:
            hosts = ['localhost', 'localhost.%',
                     socket.gethostname(), socket.getfqdn()]

            if options.voms_host:
                hosts = [options.voms_host, options.voms_host + '.%']

            for host in hosts:
                query = f"grant all privileges on {options.dbname}.* to " \
                    f"'{options.username}'@'{host}' " \
                    f"identified by '{options.password}' " \
                    f"with grant option;"
                print(query, file=mysql_proc.stdin)
            print("flush privileges;", file=mysql_proc.stdin)
            status = mysql_proc.wait()
        if status != 0:
            error_and_exit(
                f"Error granting read/write access to user {options.username} "
                f"on database {options.dbname}: {mysql_proc.stdout.read()}")


def grant_ro_access(options):
    print(f"Granting user {options.username} read-only access on database "
          f"{options.dbname}")
    mysql_cmd = build_mysql_command_preamble(options)

    if len(options.username) > 16:
        error_and_exit("MySQL database accont names cannot be longer than "
                       "16 characters.")

    if db_exists(options):
        with subprocess.Popen(mysql_cmd, shell=True,
                              stdin=subprocess.PIPE) as mysql_proc:
            hosts = ['localhost', 'localhost.%',
                     socket.gethostname(), socket.getfqdn()]

            if options.voms_host:
                hosts = [options.voms_host, options.voms_host + '.%']

            for host in hosts:
                print(
                    f"grant select on {options.dbname}.* to "
                    f"'{options.username}'@'{host}' "
                    "identified by 'options.password';",
                    file=mysql_proc.stdin)
            print("flush privileges;", mysql_proc.stdin)
            # mysql_proc.stdin.close()
            status = mysql_proc.wait()

        if status != 0:
            error_and_exit(
                "Error granting read-only access to user "
                f"{options.username} on database {options.dbname}: "
                f"{mysql_proc.stdout.read()}")


supported_commands = {
    'create_db': create_db,
    'drop_db': drop_db,
    'grant_rw_access': grant_rw_access,
    'grant_ro_access': grant_ro_access
}

required_options = ["username", "password", "dbname"]


def check_mysql_command(options):
    test_cmd = f"{options.command} --version"
    with subprocess.Popen(test_cmd, shell=True, stdout=subprocess.PIPE,
                          stderr=subprocess.PIPE) as proc:
        (out, err) = proc.communicate()
        combined_output = f"{out} {err}"
        status = proc.wait()
        if status != 0:
            error_and_exit(
                f"Error executing {options.command}: {combined_output.strip()}."
                " Check your MySQL client installation.")


def check_args_and_options(options, args):
    if len(args) != 1 or args[0] not in supported_commands.keys():
        str_commands = '\n\t'.join(supported_commands.keys())
        error_and_exit("Please specify a single command among the following:"
                       f"\n\t{str_commands}")

    missing_options = []

    if not options.username:
        missing_options.append("--dbusername")
    if not options.password:
        missing_options.append("--dbpassword")
    if not options.dbname:
        missing_options.append("--dbname")

    if len(missing_options) != 0:
        str_missing_options = "\n\t".join(missing_options)
        error_and_exit("Please specify the following missing options:\n\t"
                       f"{str_missing_options}")


def main():
    setup_cl_options()
    (options, args) = parser.parse_args()
    check_args_and_options(options, args)
    check_mysql_command(options)

    supported_commands[args[0]](options)


if __name__ == '__main__':
    main()
