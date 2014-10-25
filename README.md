# VOMS Admin server

The Virtual Organization Membership Service is a Grid attribute authority which
serves as central repository for VO user authorization information, providing
support for sorting users into group hierarchies, keeping track of their roles
and other attributes in order to issue trusted attribute certificates and
assertions used in the Grid environment for authorization purposes.

The VOMS Admin service is a web application providing tools for administering
the VOMS VO structure. It provides an intuitive web user interface for daily
administration tasks.

## Build instructions

You will need maven 3 to build this project.

  mvn package

will build the artifacts.

### Eclipse import instructions

To import the project in Eclipse for development, do as follows:

  mvn clean eclipse:clean
  mvn eclipse:eclipse

From Eclipse menu, select “Import Existing Maven projects...”, and
point it to this project root directory.


## RPM package build

See README.md in ./package/centos directory.
