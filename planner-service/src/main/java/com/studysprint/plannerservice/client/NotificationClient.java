package com.studysprint.plannerservice.client;

import com.studysprint.plannerservice.dto.client.NotificationResponse;
import com.studysprint.plannerservice.dto.client.SprintNotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NotificationClient {

    private final RestClient restClient;

    public NotificationClient(@Value("${services.notification-service.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public NotificationResponse notifySprintCreated(SprintNotificationRequest request) {
        return restClient.post()
                .uri("/api/internal/notifications/sprint-created")
                .body(request)
                .retrieve()
                .body(NotificationResponse.class);
    }

    public NotificationResponse notifySprintReplanned(SprintNotificationRequest request) {
        return restClient.post()
                .uri("/api/internal/notifications/sprint-replanned")
                .body(request)
                .retrieve()
                .body(NotificationResponse.class);
    }
}