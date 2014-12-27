<%-- 
    Document   : menu
    Created on : Dec 27, 2014, 6:02:23 PM
    Author     : Bat-El
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello ,</h1>
        <%
            session.getAttribute("firstName");
            session.getAttribute("lastName");
        %>
    </body>
</html>
