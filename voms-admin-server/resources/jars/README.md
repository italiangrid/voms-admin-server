Embedding the commons-io 1.3.2 jar here since we have a jar conflict.

The voms-container (and canl) require 1.3.2 to work properly, while the latest
struts (and in particular the fileupload functionality used for certificate
upload) requires version 2.2.

Things seem to work fine if 2.2 is embedded in the war, while 1.3.2 is in the
classpath of the voms-container jar, but since maven doesn't allow to manage
multiple versions of a dependency in the build of a component, and the tarball
is build in the voms-admin-server component, the easiest way of including the
1.3.2 jar in the tarball is by explicitly include the file from this directory.
