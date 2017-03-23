# VOMS Admin server

The Virtual Organization Membership Service is a Grid attribute authority which
serves as central repository for VO user authorization information, providing
support for sorting users into group hierarchies, keeping track of their roles
and other attributes in order to issue trusted attribute certificates and
assertions used in the Grid environment for authorization purposes.

The VOMS Admin service is a web application providing tools for administering
the VOMS VO structure. It provides an intuitive web user interface for daily
administration tasks.

## Build with Maven

Requirements:

- Java 8
- Maven 3

Build it with the following command:

  mvn package

## Eclipse import instructions

To import the project in Eclipse for development, do as follows:

  mvn clean eclipse:clean
  mvn eclipse:eclipse

From Eclipse menu, select “Import Existing Maven projects...”, and
point it to this project root directory.

## Dockerized development environment

Reauirements:

- Docker >= v.1.12.0
- Docker-compose >= v.1.9.0

Running

  docker-compose up

Will setup a running voms-admin-server instance, running a single VO named test_0
