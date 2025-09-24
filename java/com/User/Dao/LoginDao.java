package com.User.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.User.Model.GetUser;

public class LoginDao {

    // Database connection details
    private final String url = "jdbc:mysql://localhost:3306/flightbooking";
    private final String db_username = "root";
    private final String db_password = "root";

    // Check if the email exists in the users table
    public boolean emailExists(String email) {
        boolean exists = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (
                Connection con = DriverManager.getConnection(url, db_username, db_password);
                PreparedStatement stmt = con.prepareStatement("SELECT 1 FROM users WHERE email = ?")
            ) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    exists = rs.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

    // Check if the email and password match a user, return the user if so, else null
    public GetUser checkCredentials(String email, String password) {
        GetUser user = null;
        String sql = "SELECT id, fullname, email, mobile FROM users WHERE email = ? AND password = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (
                Connection con = DriverManager.getConnection(url, db_username, db_password);
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int userid = rs.getInt("id");
                        String fullname = rs.getString("fullname");
                        String userEmail = rs.getString("email");
                        String mobile = rs.getString("mobile");
                        user = new GetUser(userid, fullname, userEmail, mobile);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
