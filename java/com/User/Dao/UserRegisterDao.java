package com.User.Dao;

import com.User.Model.GetUser;
import java.sql.*;

public class UserRegisterDao {

    private final String url = "jdbc:mysql://localhost:3306/flightbooking";
    private final String db_username = "root";
    private final String db_password = "root";

    public boolean registerUser(GetUser user) {
        String sql = "INSERT INTO users (fullname, email, mobile, password) VALUES (?, ?, ?, ?)";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (
                Connection con = DriverManager.getConnection(url, db_username, db_password);
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ) {
                ps.setString(1, user.getFullname());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getMobile());
                ps.setString(4, user.getPassword());
                int count = ps.executeUpdate();

                if (count > 0) {
                    // Optionally set generated user ID
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            user.setId(rs.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
