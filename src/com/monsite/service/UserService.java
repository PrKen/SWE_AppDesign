package com.monsite.service;

import com.monsite.model.User;
import com.monsite.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticateUser(String username, String password) throws SQLException {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public void updateUserStatus(User user) throws SQLException {
        userRepository.updateStatus(user);
    }

    public User findByUsername(String username) throws SQLException {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) throws SQLException {
        userRepository.save(user);
    }

    public List<User> getAllUsers() throws SQLException {
        return userRepository.findAll();
    }
}