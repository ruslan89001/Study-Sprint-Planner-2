package com.studysprint.userservice.service;

import com.studysprint.userservice.dto.*;


public interface UserService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    UserResponse getUserById(Long userId);

    UserResponse updateUser(Long userId, UpdateUserRequest request);

    UserPreferencesResponse getUserPreferences(Long userId);

    UserPreferencesResponse updateUserPreferences(Long userId, UpdateUserPreferencesRequest request);
}
