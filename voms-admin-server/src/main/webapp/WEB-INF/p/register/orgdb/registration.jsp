<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>
Welcome to the registration page for the <span class="voName">${voName}</span>
Virtual Organization!
</h1>

<p>
To apply for membership in this Virtual Organization, you must
be registered in the CERN Human Resource database and have your membership
there linked to the <strong>${voName}</strong> experiment.
</p>

<p>
The VO registration process requires that you know the email address linked
to your CERN Human Resource record. To find out your CERN Human Resource 
record, use the <a href="https://phonebook.cern.ch/phonebook/" target="_blank">
CERN Phonebook service</a> before proceeding with the registration.
<p>

<p>
Please enter the following information:
</p>

<tiles2:insertTemplate template="searchResults.jsp"/>

