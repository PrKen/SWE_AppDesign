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

    public List<User> getAllUsers() throws SQLException {
        return userRepository.findAll();
    }

    public void saveUser(User user) throws SQLException {
        userRepository.save(user);
    }
}