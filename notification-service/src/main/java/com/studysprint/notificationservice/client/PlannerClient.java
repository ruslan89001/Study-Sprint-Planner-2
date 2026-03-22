package com.studysprint.notificationservice.client;

import com.studysprint.notificationservice.dto.client.TodaySprintResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PlannerClient {

    private final RestClient restClient;

    public PlannerClient(@Value("${services.planner-service.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public TodaySprintResponse getTodayPlan(Long userId) {
        return restClient.get()
                .uri("/api/sprints/user/{userId}/today", userId)
                .retrieve()
                .body(TodaySprintResponse.class);
    }
}