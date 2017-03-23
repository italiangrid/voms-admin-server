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


<s:form id="createGroupForm" action="create-group" namespace="/group"
	theme="bootstrap" cssClass="form-horizontal">
	
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title">Create a new VO Group</h4>
	</div>
	<div class="modal-body">
	 <s:token />
	 <s:select list="#request['voGroups']" name="parentGroupName"
		  label="Parent group" />
	
	 <s:textfield name="groupName" label="Group name" placeholder="Enter the group name..."/>
	 <s:textarea name="description" label="Group description" placeholder="Enter a description for the group..."/>
   </div>
   
   <div class="modal-footer">
	    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	    <sj:submit cssClass="btn btn-primary" 
	      formIds="createGroupForm" 
	      value="%{'Create group'}" 
	      validate="true" validateFunction="bootstrapValidation"/>
    </div>
</s:form>

