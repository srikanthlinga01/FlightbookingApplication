package com.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        String url = "jdbc:mysql://localhost:3306/flightbooking";
        String db_username = "root";
        String db_password = "root";
        boolean deleted = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (
                Connection con = DriverManager.getConnection(url, db_username, db_password);
                PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE email=?")
            ) {
                ps.setString(1, email);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    HttpSession session = request.getSession(false);
                    if (session != null) session.invalidate();
                    deleted = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (deleted) {
            response.sendRedirect("UserRegistration.jsp?msg=deleted");
        } else {
            response.sendRedirect("welcome.jsp?msg=notdeleted");
        }
    }
}
