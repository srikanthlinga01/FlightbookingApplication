package com.User;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.User.Dao.LoginDao;
import com.User.Model.GetUser;

@WebServlet("/login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            LoginDao dao = new LoginDao();

            // 1. Check if email exists and password matches
            GetUser user = dao.checkCredentials(email, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("loginsuccess", user);
                response.sendRedirect("FlightSearchServlet");
            } else {
                // Use request attribute (not session) and forward
                request.setAttribute("loginError", "Username or password incorrect.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // For debugging; use a logger in production
            request.setAttribute("loginError", "An unexpected error occurred. Please try again.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
