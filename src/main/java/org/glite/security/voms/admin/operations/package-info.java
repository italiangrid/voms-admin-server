/**
* In VOMS-Admin, each operation that access the VOMS database is authorized via the VOMS-Admin Authorization framework.
* More specifically, Access Control Lists (ACLs) are linked to VOMS contexts to enforce authorization decisions on such contexts. 
* In this framework, a Context is either a VOMS group, or a VOMS role scoped within a group. 
* <br>
* Each Context as an ACL, which is a set of access control entries, i.e., (VOMS Administrator, VOMSPermission) couples.
* 
* This package contains the classes that implement the VOMS Admin authorization framework.
*  
**/
package org.glite.security.voms.admin.operations;

