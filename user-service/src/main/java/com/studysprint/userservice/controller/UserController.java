package com.studysprint.userservice.controller;

import com.studysprint.userservice.dto.*;
import com.studysprint.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable Long userId,
                                   @Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(userId, request);
    }

    @GetMapping("/{userId}/preferences")
    public UserPreferencesResponse getUserPreferences(@PathVariable Long userId) {
        return userService.getUserPreferences(userId);
    }

    @PutMapping("/{userId}/preferences")
    public UserPreferencesResponse updateUserPreferences(@PathVariable Long userId,
                                                         @Valid @RequestBody UpdateUserPreferencesRequest request) {
        return userService.updateUserPreferences(userId, request);
    }
}
