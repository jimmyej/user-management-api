package com.jse.manage_user.service;

import com.jse.manage_user.model.request.UserRequest;
import com.jse.manage_user.model.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);
}
