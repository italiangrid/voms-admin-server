<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>Confirmation required.</h1>

<p>An email has been sent to you with instructions on how to proceed
with the registration for the ${voName} VO.</p>

<p>
Please follow the instructions before the following date:
</p>
<ul style="width: 100%; margin: 1em 0 1em 0;">
  <li style="text-align: center;"> 
    <span style="font-weight: bold; font-size: 20px;">
      <s:text name="format.datetime">
        <s:param value="model.expirationDate"/>
      </s:text>
    </span>
</li>
</ul>
or your request will be discarded by voms-admin.