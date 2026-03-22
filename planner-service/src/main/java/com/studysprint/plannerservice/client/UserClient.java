package com.studysprint.plannerservice.client;

import com.studysprint.plannerservice.dto.client.UserPreferencesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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

    public UserPreferencesResponse getPreferences(Long userId) {
        return restClient.get()
                .uri("/api/users/{userId}/preferences", userId)
                .retrieve()
                .body(UserPreferencesResponse.class);
    }
}