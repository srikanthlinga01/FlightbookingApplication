package com.User;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.User.Dao.LoginDao;
import com.User.Dao.UserRegisterDao;
import com.User.Model.GetUser;

@WebServlet("/UserRegisterServlet")
public class UserRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^\\+\\d{1,3}\\d{10}$"); // e.g., +919876543210

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String fullname = request.getParameter("fullname");
            String email = request.getParameter("email");
            String mobile = request.getParameter("mobile");
            String password = request.getParameter("password");
          

            // Retain entered values except password
            request.setAttribute("prevFullname", fullname);
            request.setAttribute("prevEmail", email);
            request.setAttribute("prevMobile", mobile);

            // 1. Check for empty fields
            if (fullname == null || fullname.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                mobile == null || mobile.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
                request.setAttribute("regError", "Please fill out all fields.");
                request.getRequestDispatcher("UserRegistration.jsp").forward(request, response);
                return;
            }

            // 2. Validate email format
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                request.setAttribute("regError", "Invalid email format.");
                request.getRequestDispatcher("UserRegistration.jsp").forward(request, response);
                return;
            }

            // 3. Validate mobile number format
            if (!MOBILE_PATTERN.matcher(mobile).matches()) {
                request.setAttribute("regError", "Invalid mobile number. Use country code, e.g., +919876543210");
                request.getRequestDispatcher("UserRegistration.jsp").forward(request, response);
                return;
            }

            // 5. Check if email already exists
            LoginDao loginDao = new LoginDao();
            if (loginDao.emailExists(email)) {
                request.setAttribute("regError", "User already exists with this email.");
                request.getRequestDispatcher("UserRegistration.jsp").forward(request, response);
                return;
            }

            // 6. Register user in DB
            GetUser user = new GetUser();
            user.setFullname(fullname);
            user.setEmail(email);
            user.setMobile(mobile);
            user.setPassword(password);

            UserRegisterDao regDao = new UserRegisterDao();
            boolean registered = regDao.registerUser(user);

            if (registered) {
            	request.getSession().setAttribute("infoMessage", "Registration successful! Please login.");
            	response.sendRedirect("index.jsp");
            } else {
                request.setAttribute("regError", "Registration failed. Please try again.");
                request.getRequestDispatcher("UserRegistration.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("regError", "An unexpected error occurred. Please try again.");
            request.getRequestDispatcher("UserRegistration.jsp").forward(request, response);
        }
    }
}
