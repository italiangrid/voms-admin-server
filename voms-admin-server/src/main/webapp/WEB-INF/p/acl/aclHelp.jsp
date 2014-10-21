<%--

    Copyright (c) Members of the EGEE Collaboration. 2006-2009.
    See http://www.eu-egee.org/partners/ for details on the copyright holders.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    Authors:
    	Andrea Ceccanti (INFN)

--%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div id="aclHelpPane">
    <h1>The VOMS Admin Authorization framework</h1>
      <p>
    In VOMS-Admin, each operation that access the VOMS database is authorized via the VOMS-Admin Authorization framework.
    <br /> 
    For instance, only authorized admins have the rights to add users or create groups for a specific VO. 
    More specifically, Access Control Lists (ACLs) are linked to VOMS contexts to enforce authorization decisions on such contexts. 
    In this framework, a Context is either a VOMS group, or a VOMS role within a group.<br/> 
    Each Context as an ACL, which is a set of ACL  entries, i.e., (VOMS Administrator, VOMSPermission) couples.
  </p>
  
  
  <p>
    A <span style="font-weight: bold">VOMS Administrator</span> may be:
    
  </p>
  <ul>
      <li>A VO administrator registered in the VO VOMS database;</li>
      <li>A VO user;</li>
      <li>A VOMS FQAN that describe, for instance, all the members of a specific VOMS group or all the members
        that have a specific role in a given group;
      </li>
      <li>Any authenticated user, i.e. any user that authenticates at the service presenting a valid certificate signed
      by a trusted CA.</li>
  </ul>
  
  TO BE COMPLETED!
</div>