package com.santanu.gossipZ.service;

import com.santanu.gossipZ.entity.UserEntity;
import com.santanu.gossipZ.model.User;

import java.util.List;

public interface UserService {

    // Retrieve list of all users
    List<User> getAllUser();

    // Retrieve a user by its id
    User getUserById(Long userId);

    // Create new User
    User addUser(User newUser);

    // Update a user
    User updateUser(Long userId,User newUser);

    // Delete an User
    boolean deleteUser(Long userId);
}
