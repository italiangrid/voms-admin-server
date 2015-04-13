<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<tiles2:useAttribute name="dialogID" id="dialogID"/>

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