<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<div role="tabpanel">
  
  <!-- Nav tabs -->
  <ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#pi" aria-controls="pi" role="tab" data-toggle="tab">Personal information</a></li>
    <li role="presentation"><a href="#certificates" aria-controls="certificates" role="tab" data-toggle="tab">Certificates</a></li>
    <li role="presentation"><a href="#groups" aria-controls="groups" role="tab" data-toggle="tab">Groups & Roles</a></li>
    <li role="presentation"><a href="#attributes" aria-controls="attributes" role="tab" data-toggle="tab">Attributes</a></li>
  </ul>
  
  <!-- Tab panes -->
  
  <div class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="pi"><tiles2:insertTemplate template="personalInfo.jsp"/></div>
    <div role="tabpanel" class="tab-pane" id="certificates"><tiles2:insertTemplate template="certs.jsp"/></div>
    <div role="tabpanel" class="tab-pane" id="groups">Groups and roles</div>
    <div role="tabpanel" class="tab-pane" id="attributes">Attributes</div>
  </div>
</div>