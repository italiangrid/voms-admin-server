<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="suspended">
  <h3 class="text-danger"><s:property value="suspensionReason"/></h3>
</s:if>

<!-- Memberhip end time information -->

<s:if test="not #attr.disableMembershipEndTime">
    <s:if test="hasExpired()">
      <span class="text-danger">Membership expired <s:property value="daysSinceExpiration" /> days ago.</span>
    </s:if>
    <s:else>
      <s:if test="daysBeforeEndTime <= 15">
        <span class="text-warning"><s:property value="daysBeforeEndTime" /> days to membership expiration.</span>
      </s:if>
      <s:else>
        <span class="text-success"><i class="glyphicon glyphicon-time"></i> <s:property value="daysBeforeEndTime" /> days to membership expiration.</span>
      </s:else>
    </s:else>
</s:if>
<span>&nbsp;</span>

<!-- AUP signature information -->

	<s:if test="hasPendingSignAUPTasks()">
	  <s:set var="daysBeforeExpiration" value="getPendingSignAUPTask(#attr.defaultAUP).daysBeforeExpiration"/>
	   <s:if test="#daysBeforeExpiration < 0">
	    <s:set var="bgClass" value="bg-error"/>
	   </s:if>
	   <s:else>
	    <s:set var="bgClass" value="bg-warning"/>
	   </s:else>
	   
	   <s:if test="#daysBeforeExpiration < 0">
	    <div class="bg-error">
	      <i class="glyphicon  glyphicon-envelope"></i>User has a pending Sign AUP request which as expired. 
	    </div>
	   </s:if>
	   <s:elseif test="#daysBeforeExpiration > 1">
	      <div class="bg-warning">
	      <i class="glyphicon  glyphicon-envelope"></i>User has a pending Sign AUP request which expires in <s:property value="#daysBeforeExpiration"/> days. 
	      </div>
	   </s:elseif>
	   <s:else>
	      <div class="bg-warning">
	      <i class="glyphicon  glyphicon-envelope"></i>User has a pending Sign AUP request which expires today. 
	      </div>
	   </s:else>
	</s:if>
	<s:else>
	   <s:if test="aupAcceptanceRecords.empty">
	    <div class="bg-warning">
	      User never signed the AUP.
	    </div> 
	   </s:if>
	   <s:else>
	    <s:set var="currentAccRec" value="aupAcceptanceRecords.{? #this.aupVersion == #attr.defaultAUP.activeVersion}"/>
	     <s:if test="#currentAccRec.empty">
	      User never signed the current active AUP version.
	     </s:if>
	     <s:else>
	      <s:iterator value="aupAcceptanceRecords.{? #this.aupVersion == #attr.defaultAUP.activeVersion}">
	        <s:if test="not valid">
	          User signature has been invalidated. A request to sign the AUP will be sent to the user as soon as the membership check task
	          runs again.
	        </s:if>
	        <s:else>
	          <span class="text-success"><i class="glyphicon glyphicon-check"></i> <s:property value="daysBeforeExpiration" /> days to AUP signature expiration.</span> 
	        </s:else>
	      </s:iterator>
	     </s:else> 
	   </s:else>
	</s:else>