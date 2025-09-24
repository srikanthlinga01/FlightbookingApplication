<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.User.Model.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Profile</title>
<%response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
    %>
</head>
<body>

<%
    GetUser user = (GetUser) session.getAttribute("loginsuccess");
    if (user == null) {
        response.sendRedirect("index.jsp");
    }
%>
<h2>Edit Profile</h2>
<form action="UpdateUserServlet" method="post">
    <input type="hidden" name="userid" value="<%= user.getId() %>"/>
    
            Name:<input type="text" name="fullname" value="" required />
            Email:<input type="email" name="email" value="" required />
            Mobile:<input type="text" name="mobile" value="" required />
            
    <button type="submit">Update Profile</button>
    <a href="welcome.jsp">Cancel</a>
</form>

</body>
</html>
