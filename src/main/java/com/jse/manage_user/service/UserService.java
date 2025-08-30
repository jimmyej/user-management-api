package com.jse.manage_user.service;

import com.jse.manage_user.model.request.UserRequest;
import com.jse.manage_user.model.response.UserResponse;

import java.util.Set;

public interface UserService {
    Set<UserResponse> getAllUsers();
    UserResponse getUserByEmail(String email);
    boolean existsEmail(String email);
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(String email, UserRequest request);
    boolean deactivateUser(String email);
}
