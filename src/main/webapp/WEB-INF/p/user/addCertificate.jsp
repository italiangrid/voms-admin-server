<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<h1>Add a certificate</h1>

<h2>User information</h2>

<div>
  Name: <s:property value="name"/> <s:property value="surname"/>
</div>

<div>
  Email: <s:property value="emailAddress"/>
</div>


<s:form action="save-certificate" enctype="multipart/form-data" namespace="/user" method="POST">
  <s:token/>
  <s:hidden name="userId" value="%{model.id}"/>
  <s:textfield name="subject" size="45" label="Subject" labelposition="top"/>
  <s:select name="caSubject" list="#request.trustedCas" listKey="subjectString" listValue="subjectString"/>
  <s:file name="certificateFile"/>
  <s:submit value="%{'Add certificate'}"/>  
</s:form>