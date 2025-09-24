package com.User;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.util.*;

import com.User.Model.GetUser;

@WebServlet("/UpdateUserServlet")
public class UpdateUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

  
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
  
    	
    	HttpSession session = request.getSession();
        GetUser user = (GetUser) session.getAttribute("loginsuccess");
        if (user == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        String field = request.getParameter("field");
        String value = request.getParameter(field);
    
        // Only allow certain fields to be updated for security
        List<String> allowedFields = Arrays.asList("fullname", "email", "mobile");
        if (!allowedFields.contains(field)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid field");
            return;
        }
        
     // Prevent null or empty value update
        if (value == null || value.trim().isEmpty()) {
            request.setAttribute("updateError", "Field cannot be empty.");
            RequestDispatcher rd = request.getRequestDispatcher("UserProfile.jsp");
            rd.forward(request, response);
            return;
        }
        
        String url="jdbc:mysql://localhost:3306/flightbooking";
        String db_username="root";
        String db_password="root";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, db_username, db_password);
            String sql = "update users SET "+field+"=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, value);
            ps.setInt(2, user.getId());
            int count=ps.executeUpdate();

            // If update was successful, update the session user object
            if (count > 0) {
                if (field.equals("fullname")) {
                    user.setFullname(value);
                } else if (field.equals("email")) {
                    user.setEmail(value);
                } else if (field.equals("mobile")) {
                    user.setMobile(value);
                }
                session.setAttribute("loginsuccess", user);
            }
               RequestDispatcher rd=request.getRequestDispatcher("UserProfile.jsp");
               rd.forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
         
        }
    }
}
