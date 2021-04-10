<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016

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

<tiles2:importAttribute name="dialgID"/>

<%--  
<tiles2:useAttribute name="dialogID" id="dialogID"/>
--%>
 
<div id="${dialogID}" class="modal fade" tabindex="-1"
  role="dialog" aria-labelledby="${dialogID}Label"
  aria-hidden="true">

  <div class="modal-dialog">
      <div class="modal-content">
      </div>
  </div>
  
  <script type="text/javascript">
   $('#${dialogID}').on('hidden.bs.modal', function(e){
     cleanupValidationErrors('#${dialogID} .modal-content');
   });
  </script>
</div>