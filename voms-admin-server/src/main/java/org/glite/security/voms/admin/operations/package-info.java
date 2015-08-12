/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

