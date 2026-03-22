package com.studysprint.notificationservice.client;

import com.studysprint.notificationservice.dto.client.UserPreferencesResponse;
import com.studysprint.notificationservice.dto.client.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(@Value("${services.user-service.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public UserResponse getUser(Long userId) {
        return restClient.get()
                .uri("/api/users/{userId}", userId)
                .retrieve()
                .body(UserResponse.class);
    }

    public UserPreferencesResponse getPreferences(Long userId) {
        return restClient.get()
                .uri("/api/users/{userId}/preferences", userId)
                .retrieve()
                .body(UserPreferencesResponse.class);
    }
}