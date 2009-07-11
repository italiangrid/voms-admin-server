<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>Confirmation required.</h1>

<p>An email has been sent to you with instructions on how to proceed
with the registration for the ${voName} VO.</p>

<div>
Please follow the instructions before the following date:
<ul>
  <li> 
    <span style="font-weight: bold">
    
      <s:property value="expirationDate"/>

    </span>
</li>
</ul>
or your request will be discarded by voms-admin.
</div>