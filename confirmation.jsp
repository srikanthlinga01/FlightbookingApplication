<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Confirmation</title>
    <!-- Redirect to MyBookingsServlet after 5 seconds -->
    <meta http-equiv="refresh" content="5;url=MyBookingsServlet">
</head>
<body>
    <h2>${confirmationMessage}</h2>
    <p>Don't Close the window/Press Back Button.You will be redirected to Main page in few seconds.</p>
    <p>If you are not redirected automatically, <a href="welcome.jsp">click here</a>.</p>
</body>
</html>
