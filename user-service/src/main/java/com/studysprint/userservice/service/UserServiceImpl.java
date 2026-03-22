package com.studysprint.userservice.service;

import com.studysprint.userservice.dto.*;
import com.studysprint.userservice.entity.User;
import com.studysprint.userservice.entity.UserPreference;
import enums.UserRole;
import com.studysprint.common.exception.BadRequestException;
import com.studysprint.common.exception.NotFoundException;
import com.studysprint.userservice.repository.UserPreferenceRepository;
import com.studysprint.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("User with this email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(request.getPassword())
                .role(UserRole.STUDENT)
                .build();

        User savedUser = userRepository.save(user);

        UserPreference preference = UserPreference.builder()
                .dailyStudyHours(2)
                .preferredStudyTime("EVENING")
                .notificationsEnabled(true)
                .user(savedUser)
                .build();

        userPreferenceRepository.save(preference);

        return RegisterResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getPasswordHash().equals(request.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        return LoginResponse.builder()
                .token(UUID.randomUUID().toString())
                .userId(user.getId())
                .build();
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return mapToUserResponse(user);
    }

    @Override
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        boolean emailBelongsToAnotherUser = userRepository.findByEmail(request.getEmail())
                .filter(foundUser -> !foundUser.getId().equals(userId))
                .isPresent();

        if (emailBelongsToAnotherUser) {
            throw new BadRequestException("Email is already used by another user");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    @Override
    public UserPreferencesResponse getUserPreferences(Long userId) {
        UserPreference preference = userPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User preferences not found"));

        return mapToUserPreferencesResponse(preference);
    }

    @Override
    public UserPreferencesResponse updateUserPreferences(Long userId, UpdateUserPreferencesRequest request) {
        UserPreference preference = userPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User preferences not found"));

        preference.setDailyStudyHours(request.getDailyStudyHours());
        preference.setPreferredStudyTime(request.getPreferredStudyTime());
        preference.setNotificationsEnabled(request.getNotificationsEnabled());

        UserPreference updatedPreference = userPreferenceRepository.save(preference);
        return mapToUserPreferencesResponse(updatedPreference);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    private UserPreferencesResponse mapToUserPreferencesResponse(UserPreference preference) {
        return UserPreferencesResponse.builder()
                .userId(preference.getUser().getId())
                .dailyStudyHours(preference.getDailyStudyHours())
                .preferredStudyTime(preference.getPreferredStudyTime())
                .notificationsEnabled(preference.getNotificationsEnabled())
                .build();
    }
}
