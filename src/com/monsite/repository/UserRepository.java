package com.monsite.repository;

import com.monsite.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("idusers"));
                user.setLastname(rs.getString("lastname"));
                user.setFirstname(rs.getString("firstname"));
                user.setAge(rs.getString("age"));
                user.setStatus(rs.getString("status"));
                users.add(user);
            }
        }
        return users;
    }

    public void save(User user) throws SQLException {
        String query = "INSERT INTO users (lastname, firstname, age, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, user.getLastname());
            pstmt.setString(2, user.getFirstname());
            pstmt.setString(3, user.getAge());
            pstmt.setString(4, user.getStatus());
            pstmt.executeUpdate();
        }
    }
}