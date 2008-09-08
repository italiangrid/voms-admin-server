<?xml version="1.0" encoding="UTF-8"?>
<%@ page contentType="text/html; charset=UTF-8" %>
<%--
<!DOCTYPE response [

<!ELEMENT response (panel+) >
<!ELEMENT panel EMPTY >
<!ATTLIST panel 
        id      ID      #REQUIRED
        status  CDATA   #REQUIRED >
]>
--%>
<response>
    <panel id="${panelId}" status="${panelStatus}"/>
</response>
